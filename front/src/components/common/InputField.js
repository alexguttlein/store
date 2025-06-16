import Form from 'react-bootstrap/Form';

/*    
    Un campo de formulario basico.
    
*/

export default function InputField(
    { name, label, type, value, defaultValue, placeholder, error, fieldRef, onChange }
) {
    return (
        <Form.Group controlId={name} className="InputField" onChange={onChange}>
            {label && <Form.Label>{label}</Form.Label>}
            <Form.Control
                defaultValue={defaultValue}
                value={value}
                type={type || 'text'}
                placeholder={placeholder}
                ref={fieldRef}
            />
            <Form.Text className="text-danger">{error}</Form.Text>
        </Form.Group>
    );
}