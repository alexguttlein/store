import { Modal, Button, Container } from 'react-bootstrap';
import { useState } from 'react';

function capitalize(s) {
    return s && String(s[0]).toUpperCase() + String(s).slice(1);
  }
  
function AttributeModal({ show, onHide, attributes }) {
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Atributos del producto</Modal.Title>
      </Modal.Header>
      <Modal.Body>
          { attributes.map((attr, index) => (
            <div key={index}>
              <p>{capitalize(attr.attributeName)}: {attr.value}</p>
            </div>  
          ))}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>Cerrar</Button>
      </Modal.Footer>
    </Modal>
  );
}

export default AttributeModal;
