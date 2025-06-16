import { useState, useEffect, useCallback } from 'react';
import InputField from '../common/InputField';
import { Card, Button, Container, Form, Row, Col } from 'react-bootstrap';


export default function ProductUserForm({ checkAttributesExist, findMatchingVariant, currentStock, onEditStockSubmit, onCreateVariantSubmit}) {
    const [formErrors, setFormErrors] = useState({});
    const [newStock, setNewStock] = useState(currentStock);

    useEffect(() => {
        setNewStock(currentStock);
    }, [currentStock]);
    
    const createVariant = isNaN(currentStock)

    const validateForm = (stock, matchingVariant) => {
        const errors = {};
        if (createVariant){
            if (!checkAttributesExist())
                errors.attributes = "Combinación de atributos inválida";
            return errors
        }

        if (!matchingVariant) {
            errors.attributes = "Combinación de atributos inválida";
        }

        if (isNaN(parseInt(stock, 10)) || Number(stock) < 0) {
            errors.amount = "Cantidad inválida";
        }
        return errors;
    };


    const handleOnSubmitEdit = async event => {
        event.preventDefault();
        const matchingVariant = findMatchingVariant();
        const errors = validateForm(newStock, matchingVariant);
        setFormErrors(errors);

        if (Object.keys(errors).length === 0) {
            onEditStockSubmit({ id: matchingVariant.id, newStock});
        }
    };

    const handleOnSubmitCreate = async event => {
        event.preventDefault();
        const matchingVariant = findMatchingVariant();
        const errors = validateForm(matchingVariant);
        setFormErrors(errors);
        
        if (Object.keys(errors).length === 0) {
            onCreateVariantSubmit();
        }
    };

    return (
        <Form onSubmit={createVariant ? handleOnSubmitCreate : handleOnSubmitEdit} noValidate>                    
            <Row className="mt-3 align-items-end">

                <Col md={8} xs={12}>
                {!isNaN(currentStock) && 

                <InputField
                            name="stock"
                            label="Cambiar stock:"
                            type="number"
                            value={newStock}
                            onChange={(e) => setNewStock(e.target.value)}
                            error={formErrors.stock}
                            className="shadow-sm rounded"
                />
                }

                </Col>
                <Col md={4} xs={12}>
                <Button variant="primary" type="submit" className="w-100 rounded shadow-sm">
                    {createVariant ?  "Crear variante" : "Setear stock"}
                </Button>
                </Col>
            </Row>                       
        </Form> 
    )
}