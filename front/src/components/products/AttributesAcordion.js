import InputFieldSelect from '../common/InputFieldSelect';
import {Accordion} from 'react-bootstrap';


function capitalize(s) {
    return s && String(s[0]).toUpperCase() + String(s).slice(1);
}

export default function AttributeAcordion({attributes, attributesIds, defaultAttributes, handleAttributeChange}) {
    return (
        <Accordion 
        defaultActiveKey={Object.keys(attributes).map((_, idx) => idx.toString())} 
        className="mb-3" 
        alwaysOpen
    >
        {Object.keys(attributes).map((key, idx) => (
            <Accordion.Item eventKey={idx.toString()} key={key} className="border-1 rounded">
                <Accordion.Header>{capitalize(key)}</Accordion.Header>
                <Accordion.Body>
                    <InputFieldSelect
                        value={defaultAttributes[key]}
                        name={key}
                        options={attributes[key]}
                        names={attributesIds}
                        onChange={(e) => handleAttributeChange(key, e.target.value)}
                    />
                </Accordion.Body>
            </Accordion.Item>
        ))}
        </Accordion>    

    )
}