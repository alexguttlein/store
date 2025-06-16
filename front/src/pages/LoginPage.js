import Body from '../components/common/Body';
import Login from "../components/user/Login";
import { useNavigate, useLocation, Link} from 'react-router-dom';
import React, {useState} from "react";
import {Alert, Container, Spinner, Button} from "react-bootstrap";
import { useUser } from '../contexts/UserProvider';
import { useFlash } from '../contexts/FlashProvider';

// Devuelve el JSX
const getLoginPageJSX = (
    handleLogin,
    isLoading,
    errorMessage
  ) => {
    return (
      <Body>
        <Container className='my-5'>
          <span className='fw-bold display-4'>Iniciar Sesion</span>
          <Button variant="link" as={Link} to="/sign-up" className="text-decoration-none">
                        Crear cuenta
                    </Button>
          {errorMessage && <Alert variant='danger'>{errorMessage}</Alert>}
  
          <Login onSubmit={handleLogin} />
          <p>Olvidaste tu contraseña? <Link to="/reset-request">Click aqui</Link>.</p>

          {isLoading && <Spinner animation='border' />}
        </Container>
      </Body>
    )
  }

export default function LoginPage() {
    const {login} = useUser()
    
    const [errorMessage, setErrorMessage] = useState('')
    const [isLoading, setIsLoading] = useState(false);
    
    const navigate = useNavigate();
    const location = useLocation();
    const flash = useFlash()

    const handleLogin = async (formData) => {
        setIsLoading(true);
        setErrorMessage('')

        const { email, password } = formData;

        const result = await login(email, password)

        if (result !== 'ok') {
            flash('Usuario o contraseña incorrectos', 'danger')
        } else {
            flash('Inicio de sesion exitoso.', 'success', 5)
            
            let next = '/products';
            if (location.state && location.state.next) {
              next = location.state.next;
            }
            navigate(next);
        }

        setIsLoading(false);
        return;
    };

    return getLoginPageJSX(handleLogin, isLoading, errorMessage)
}
