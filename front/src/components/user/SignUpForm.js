import { Button, Col, Form, Row } from 'react-bootstrap'
import InputField from '../common/InputField'
import InputFieldSelect from '../common/InputFieldSelect'
import useSignUpForm from '../../hooks/useSignUpForm'

export default function SignUpForm({ onSubmit, isLoading }) {
  const { formErrors, fieldRefs, handleOnSubmit } = useSignUpForm(onSubmit);

  return (
    <Form onSubmit={handleOnSubmit} noValidate>
      <Row>
        <Col md={6}>
          <InputField
            name='name'
            label='Nombre'
            error={formErrors.name}
            fieldRef={fieldRefs.name}
          />

          <InputField
            name='lastname'
            label='Apellido'
            error={formErrors.lastname}
            fieldRef={fieldRefs.lastname}
          />

          <InputField
            name='age'
            label='Edad'
            type='number'
            error={formErrors.age}
            fieldRef={fieldRefs.age}
          />

          <InputFieldSelect
            name='gender'
            label='Genero'
            options={['Genero...', 'Hombre', 'Mujer', 'Otro']}
            error={formErrors.gender}
            fieldRef={fieldRefs.gender}
          />

          <InputField
            name='address'
            label='Direccion'
            type='text'
            error={formErrors.address}
            fieldRef={fieldRefs.address}
          />

          <Form.Group controlId='formFile' className='mb-3'>
            <Form.Label>Foto de perfil</Form.Label>
            <Form.Control type='file' ref={fieldRefs.picture} />
          </Form.Group>
          <Form.Text className='text-danger'>{formErrors.avatar}</Form.Text>
        </Col>

        <Col md={6}>
          <InputField
            name='email'
            label='Correo electronico'
            type='email'
            error={formErrors.email}
            fieldRef={fieldRefs.email}
          />

          <InputField
            name='password'
            label='Contraseña'
            type='password'
            error={formErrors.password}
            fieldRef={fieldRefs.password}
          />

          <Button variant='primary' type='submit' className='w-100 mt-3' disabled={isLoading}>
            {isLoading ? 'Registrando...' : 'Registrame'}
          </Button>
        </Col>
      </Row>
    </Form>
  )
}