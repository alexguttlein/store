import Form from 'react-bootstrap/Form';


/*    
    Un campo de formulario de tipo select.    
*/

export default function InputFieldSelect(
    { value, name, label, options, names, error, fieldRef, onChange }
) {
    return (
        <Form.Group controlId={name} className="InputFieldSelect">
            {label && <Form.Label>{label}</Form.Label>}
            <Form.Select aria-label={name} ref={fieldRef} onChange={onChange} defaultValue={value}>
                {options.length === 0 ?
                    <p>There are no options.</p>
                    :
                    options.map(opt => {
                        return (
                            <option key={opt} value={opt}>{names ? names[opt] : opt}</option>
                        );
                    })
                }
            </Form.Select>
            <Form.Text className="text-danger">{error}</Form.Text>
        </Form.Group>
    );
}