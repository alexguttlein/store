import { useEffect, useRef, useState } from 'react'
import {Button, Col, Form, Row} from "react-bootstrap";
import InputField from '../common/InputField'

const validateForm = data => {
    const errors = {}

    if (!data.password)
        errors.password = 'El campo Contraseña no puede estar vacío'
    if (data.password.length > 50) errors.password = 'El campo Contraseña debe ser menor a 50 caracteres'
    
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(data.email)) {
      errors['email'] = 'Por favor, ingrese un email válido.'
    }
    if (data.email > 50) errors.email = 'El campo Email debe ser menor a 50 caracteres'

    return errors;
}


export default function Login({ onSubmit }) {
    const fieldRefs = {
        email: useRef(),
        password: useRef(),
    };

    const [formErrors, setFormErrors] = useState({})

    useEffect(() => {
        fieldRefs.email.current.focus()
      }, [fieldRefs.email])

    const handleOnSubmit = async (event) => {
        event.preventDefault();

        const formData = Object.keys(fieldRefs).reduce((data, key) => {
            data[key] = fieldRefs[key].current.value;
            return data;
        }, {});

        const errors = validateForm(formData)
        setFormErrors(errors)

        if (Object.keys(errors).length === 0) {
            onSubmit(formData)
          }
    };

    return (
        <Form onSubmit={handleOnSubmit} noValidate>
            <Row>
                <Col md={6}>

                    <InputField
                        name="email" label="Correo electronico" type="email"
                        error={formErrors.email} fieldRef={fieldRefs.email}
                    />

                    <InputField
                        name="password" label="Contraseña" type="password"
                        error={formErrors.password} fieldRef={fieldRefs.password} />

                    <Button variant="primary" type="submit"  className="display-4 fw-bold mt-4">Iniciar Sesion</Button>
                </Col>
            </Row>
        </Form>
    );
}