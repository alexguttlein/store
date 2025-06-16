import { useState } from 'react';
import { Card, Button, Form, Row, Col } from 'react-bootstrap';


export default function ProductUserForm({ findMatchingVariant, currentStock, onSubmit }) {
    const [formErrors, setFormErrors] = useState({});

    const validateForm = (matchingVariant) => {
        const errors = {};
        if (!matchingVariant) {
            errors.attributes = "Combinación de atributos inválida";
        }

        return errors;
    };


    const handleOnSubmit = async event => {
        event.preventDefault();
        const matchingVariant = findMatchingVariant();
        const errors = validateForm(matchingVariant);
        setFormErrors(errors);

        if (Object.keys(errors).length === 0) {
            onSubmit({ id:matchingVariant.id });
        }
    };

    return (
        <Form onSubmit={handleOnSubmit} noValidate>                            
            <Card.Text className="fw-bold m-2 text-secondary" aria-live="polite"> Stock: {currentStock}</Card.Text>
            {formErrors["attributes"] && <Form.Text className="text-danger">{formErrors["attributes"]}</Form.Text>}
            <Row className="mt-3 align-items-end">
                <Col md={4} xs={12}>
                    <Button variant="primary" type="submit" className="w-100 rounded shadow-sm" disabled={currentStock < 1}>
                        <i className="bi bi-cart-plus"> </i>
                        Agregar al carrito
                    </Button>
                </Col>
            </Row>
        </Form>    
    )
}
