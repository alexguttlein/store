import Body from '../components/common/Body';
import { useEffect, useState } from 'react'
import { useNavigate, useLocation, useSearchParams } from 'react-router-dom';

import { Container, Alert, Spinner, Button } from 'react-bootstrap'
import { useApi } from '../contexts/ApiProvider'
import { useFlash } from '../contexts/FlashProvider'

const ConfirmPage = () => {
  const {api} = useApi();
  const [isLoading, setIsLoading] = useState(false);
  const [token, setToken] = useState();
  const [email, setEmail] = useState();
  const [searchParams, setSearchParams] = useSearchParams();

  const navigate = useNavigate();
  const location = useLocation();
  const flash = useFlash();

  useEffect(() => {
    setToken(searchParams.get('token') || null);
    setEmail(searchParams.get('email') || null);
  }, [searchParams])

  const handleConfirmation = async () => {
    setIsLoading(true);

    try {
      const data = await api.post('/confirm', {token: token});

      if (!data.ok) {
        flash(data.body.message || 'Error inesperado al conectarse con el server', 'danger');
      } else {
        flash('Cuenta confirmada exitosamente.', 'success');
        const next = location.state?.next || '/login';
        navigate(next);
      }
    } catch (error) {
      flash('Error inesperado al conectarse con el server', 'danger');
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) return <Spinner animation="border" />;

  return (
    <Body>
      <Container className="my-5 text-center">
        <h1 className="fw-bold display-4 mb-4">Confirmación de Email</h1>

        <p className="fw-bold mb-4">
          Cuenta creada exitosamente. Un email de confirmación ha sido enviado a <strong>{email}</strong>. Haz clic en el botón para confirmarla.
        </p>

        <Button variant="primary" onClick={handleConfirmation} className="px-4 py-2">
          Confirmar Cuenta
        </Button>
      </Container>
    </Body>
  );
};

export default ConfirmPage;