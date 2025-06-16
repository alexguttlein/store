import { Card, Container } from 'react-bootstrap';
import AttributeAcordion from './AttributesAcordion';
import { useSearchParams } from 'react-router-dom';

import { useState, useEffect, useCallback, useMemo } from 'react';
import ProductUserForm from './ProductUserForm';
import ProductAdminForm from './ProductAdminForm';

import placeHolderImage from '../../res/placeHolderImage.png'

export default function Product({ isAdmin, product, onAddProductToCartSubmit, onEditStockSubmit, onCreateVariantSubmit }) {
    const [searchParams, setSearchParams] = useSearchParams();
    const productId = Number(searchParams.get("productId"));
    const variant = product.variants.find(x => x.id === productId);

    const initialSelectedAttributes = useMemo(() =>
        variant
            ? Object.keys(variant.attributes).reduce((acc, key) => {
                acc[key] = variant.attributes[key];
                return acc;
            }, {})
            : Object.keys(product.attributes).reduce((acc, key) => {
                acc[key] = product.attributes[key][0];
                return acc;
            }, {}), [variant, product.attributes]
    );

    const [selectedAttributes, setSelectedAttributes] = useState(initialSelectedAttributes);

    const findMatchingVariant = () =>
        product.variants.find((variant) =>
            Object.keys(selectedAttributes).every(
                (key) => variant.attributes[key] === selectedAttributes[key]
            )
        );

    const checkAttributesExist = () =>
        Object.keys(product.attributes).every(key =>
            product.attributes[key].includes(selectedAttributes[key])
        );

    const [currentStock, setCurrentStock] = useState(() => {
        const matchingVariant = product.variants.find(variant =>
            Object.keys(initialSelectedAttributes).every(
                key => variant.attributes[key] === initialSelectedAttributes[key]
            )
        );
        return matchingVariant ? matchingVariant.stock : "Combinacion sin stock";
    });

    useEffect(() => {
        const matchingVariant = product.variants.find((variant) =>
            Object.keys(selectedAttributes).every(
                (key) => variant.attributes[key] === selectedAttributes[key]
            )
        );
        setCurrentStock(matchingVariant ? matchingVariant.stock : "Combinacion sin stock");
        if (matchingVariant) {
            const newParams = new URLSearchParams(searchParams);
            newParams.set("productId", matchingVariant.id);
            setSearchParams(newParams, { replace: true });
        }
    }, [selectedAttributes, product.variants, searchParams, setSearchParams]);

    const handleAttributeChange = useCallback((key, value) => {
        setSelectedAttributes(prev => ({ ...prev, [key]: Number(value) }));
    }, []);

    const handleNewVariantSubmit = () => {
        const formData = { "ids": [] };

        Object.keys(selectedAttributes).forEach(key => {
            formData["ids"].push(selectedAttributes[key]);
        });

        onCreateVariantSubmit(formData);
    };

    return (
        <Card className="shadow-sm p-3 mb-4 rounded">
            <Card.Header className="border-3 rounded-top" style={{ backgroundColor: 'transparent' }}>
               <h2 className="fw-bold text-primary display-6 text-center">{product.name}</h2>
            </Card.Header>

            {product.photo !== "" &&
            <Card.Img
                variant="top"
                src={product.photo !== "" ? product.photo : placeHolderImage}
                alt={`Foto de ${product.name}`}
                style={{
                height: '300px',
                objectFit: 'contain',
                borderRadius: '8px',
                marginBottom: '1rem',
                }}
            />
            }
            <Card.Body>
                {Object.keys(product.attributes).length > 0 &&
                    <Card.Text className="fw-bold text-dark">Elegir Variante:</Card.Text>
                }

                <Container className="p-3">
                    <AttributeAcordion attributes={product.attributes} attributesIds={product.attributesIds} defaultAttributes={initialSelectedAttributes} handleAttributeChange={handleAttributeChange}></AttributeAcordion>
                    {!isAdmin ?
                        <ProductUserForm currentStock={currentStock} findMatchingVariant={findMatchingVariant} onSubmit={onAddProductToCartSubmit}></ProductUserForm>
                        :
                        <ProductAdminForm checkAttributesExist={checkAttributesExist} currentStock={currentStock} findMatchingVariant={findMatchingVariant}
                            onEditStockSubmit={onEditStockSubmit}
                            onCreateVariantSubmit={handleNewVariantSubmit}
                        ></ProductAdminForm>
                    }
                </Container>
            </Card.Body>
        </Card>
    );
}


