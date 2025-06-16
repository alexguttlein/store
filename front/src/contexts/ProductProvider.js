import React, { createContext, useContext, useState, useCallback } from 'react';
import { useApi } from './ApiProvider';
import MockProducts from '../MockProducts';

const ProductContext = createContext();
const mockProducts = new MockProducts();

export default function ProductProvider({ mock, children }) {
    const [product, setProduct] = useState();
    const {api} = useApi();

    const parseVariants = (body) => {
        return body.map((variant) => ({
            id: variant.id,
            stock: variant.stock,
            attributes: variant.attributes.reduce((acc, attr) => {
                acc[attr.attributeName] = attr.id;
                return acc;
            }, {})
        }));
    };

    const parseAttributes = (body) => {
        const attributesData = {};
        body.forEach(variant => {
            variant.attributes.forEach(attr => {
                if (!attributesData[attr.attributeName]) {
                    attributesData[attr.attributeName] = [];
                }
                if (!attributesData[attr.attributeName].includes(attr.id)) {
                    attributesData[attr.attributeName].push(attr.id);
                }
                
            });
        });
        return attributesData;
    };

    const parseAttributeIds = (body) => {
        return body.reduce((acc, variant) => {
            variant.attributes.forEach(attr => {   
                acc[attr.id] = attr.value;
            });
            return acc;
        }, {});
    };

    const fetchProduct = useCallback(async (productId) => {
        if (mock) {
            const response = await mockProducts.getProductsById(productId);
            setProduct(response);
        } else {
            const response = await api.get(`/alternative/${productId}/products`);

            if (response.ok) {
                if (Object.keys(response.body).length === 0){
                    setProduct(null)
                    return
                }

                const productData = {
                    name: response.body[0]?.alternative?.alternativeName || "",
                    photo: response.body[0]?.alternative?.alternativePhoto || "",
                    alternativeID: response.body[0]?.alternative?.id ?? "",
                    variants: parseVariants(response.body),
                    attributes: parseAttributes(response.body),
                    attributesIds: parseAttributeIds(response.body)
                };
                setProduct(productData);
            } else {
                setProduct(null);
            }
        }
    }, [api, mock]);

    return (
        <ProductContext.Provider value={{ product, fetchProduct }}>
            {children}
        </ProductContext.Provider>
    );
}

export const useProduct = () => useContext(ProductContext);
