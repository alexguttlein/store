import { useState, useEffect, useRef } from 'react';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import { useNavigate, useLocation } from 'react-router-dom';
import Body from '../components/common/Body';
import { useApi } from '../contexts/ApiProvider';
import { useFlash } from '../contexts/FlashProvider';
import InputField from '../components/common/InputField';

export default function ResetPage() {
  const [formErrors, setFormErrors] = useState({});
  const passwordField = useRef();
  const navigate = useNavigate();
  const { search } = useLocation();
  const {api} = useApi();
  const flash = useFlash();
  const token = new URLSearchParams(search).get('token');

  useEffect(() => {
    if (!token) {
      navigate('/');
    }
    else {
      passwordField.current.focus();
    }
  }, [token, navigate]);

  const onSubmit = async (event) => {
    event.preventDefault();
    if (passwordField.current.value == null) {
        setFormErrors({password: "Ingrese la nueva contraseña"});
    }
    else {
      const response = await api.put('/reset-password', {
        token,
        new_password: passwordField.current.value
      });
      if (response.ok) {
        setFormErrors({});
        flash('Contraseña actualizada correctamente.', 'success');
        navigate('/login');
      }
      else {
        if (response.body) {
          setFormErrors({password: response.body});
        }
        else {
          flash('Error al actualizar la contraseña.', 'danger');
          navigate('/reset-request');
        }
      }
    }
  };

  return (
    <Body>
      <div className="container py-4">
        <h2 className="fw-bold display-6 text-center mb-4">Actualizar contraseña</h2>
        <Form onSubmit={onSubmit} className="p-4 rounded shadow-sm bg-light">
          <InputField
            name="password"
            label="Nueva contraseña"
            type="password"
            error={formErrors.password}
            fieldRef={passwordField}
          />
          <div className="d-grid mt-3">
            <Button variant="primary" type="submit">
              Actualizar contraseña
            </Button>
          </div>
        </Form>
      </div>
    </Body>
  );
}