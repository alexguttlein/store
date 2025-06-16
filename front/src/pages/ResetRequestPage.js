import {useState, useEffect, useRef} from "react";
import { useFlash } from '../contexts/FlashProvider';
import Body from "../components/common/Body";
import { useApi } from '../contexts/ApiProvider';
import { useNavigate } from 'react-router-dom';
import InputField from "../components/common/InputField";
import { Form, Button } from "react-bootstrap";

export default function ResetRequestPage() {
  const [formErrors, setFormErrors] = useState({});
  const emailField = useRef();
  const navigate = useNavigate();

  const {api} = useApi();
  const flash = useFlash();

  useEffect(() => {
    emailField.current.focus();
  }, []);

  const onSubmit = async (event) => {
    event.preventDefault();
    const response = await api.post('/reset-password', {
      email: emailField.current.value,
    });
    if (!response.ok) {
      setFormErrors({email: response.body});
    }
    else {
      emailField.current.value = '';
      setFormErrors({});
      flash(
        'Se envio un mail con instrucciones para recuperar la cuenta.', 'info'
      );

      window.location.href = response.body.url;
    }
  };

  return (
    <Body>
      <h2 className="fw-bold display-6 text-center mb-4">Actualizar contraseña</h2>
      <Form onSubmit={onSubmit} className="p-3 shadow-sm rounded bg-light">
        <InputField
          name="email"
          label="Email"
          error={formErrors.email}
          fieldRef={emailField}
        />
        <div className="d-grid mt-3">
          <Button variant="primary" type="submit">
            Recuperar contraseña
          </Button>
        </div>
      </Form>
    </Body>
  );
}