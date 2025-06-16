import Body from '../components/common/Body';
import SignUp from '../components/user/SignUpForm'
import { useState } from 'react'
import { useNavigate, useLocation, Link } from 'react-router-dom';

import { Container, Alert, Spinner, Button } from 'react-bootstrap'
import { useApi } from '../contexts/ApiProvider'
import { useFlash } from '../contexts/FlashProvider'

/*  Componente SignUpPage:
    Devuelve el JSX de la pagina de registro.
    Define la funcion que sera llamada al enviar el form
*/

const SignUpPage = () => {
  const {api} = useApi();
  const [isLoading, setIsLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();
  const location = useLocation();
  const flash = useFlash();

  const handleSignUp = async (formData) => {
    setIsLoading(true);
    setErrorMessage('');

    try {
      const data = await api.post('/register', {
        username: formData.email,
        email: formData.email,
        password: formData.password,
        edad: formData.age,
        genero: formData.gender,
        domicilio: formData.address,
        apellido: formData.lastname,
        foto: formData.picture
      });

      if (!data.ok) {
        setErrorMessage(data.body.message || 'Error inesperado al conectarse con el server');
      } else {
        flash('Cuenta creada exitosamente.', 'success');
        const next = '/confirm?token=' + data.body.token + "&email=" + data.body.user.email;
        navigate(next);
      }
    } catch (error) {
      setErrorMessage(error.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Body>
      <Container className='my-5'>
        <span className='fw-bold display-4'>Registro</span>
        <Button variant="link" as={Link} to="/login" className="text-decoration-none">
          <i className="bi bi-box-arrow-in-right" /> Ya tengo cuenta
        </Button>
        {errorMessage && <Alert variant='danger'>{errorMessage}</Alert>}
        <SignUp onSubmit={handleSignUp} isLoading={isLoading} />
        {isLoading && <Spinner animation='border' />}
      </Container>
    </Body>
  );
};

export default SignUpPage;