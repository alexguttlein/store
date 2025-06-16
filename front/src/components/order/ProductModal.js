import { Modal, Button, Container } from 'react-bootstrap';
import { useState } from 'react';

function capitalize(s) {
    return s && String(s[0]).toUpperCase() + String(s).slice(1);
  }
  
function ProductModal({ show, onHide, items }) {
  const [currentProductIndex, setCurrentProductIndex] = useState(0);

  const renderProductAttributes = (attributes) =>
    attributes.map((attr, index) => (
      <div key={index}>
        <p>{capitalize(attr.attributeName)}: {attr.value}</p>
      </div>
    ));

  const renderOrderItems = () => {
    const currentItem = items[currentProductIndex];
    return (
      <div className="mb-3">
        <p><strong>Nombre: {currentItem.product.productName}</strong></p>
        <p>Cantidad: {currentItem.amount}</p>
        <p>Atributos:</p>
        <Container>{renderProductAttributes(currentItem.product.attributes)}</Container>
      </div>
    );
  };

  const handleNextProduct = () => {
    if (currentProductIndex < items.length - 1) {
      setCurrentProductIndex(currentProductIndex + 1);
    }
  };

  const handlePrevProduct = () => {
    if (currentProductIndex > 0) {
      setCurrentProductIndex(currentProductIndex - 1);
    }
  };

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Detalles del pedido</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {renderOrderItems()}
        <div className="d-flex justify-content-between mt-3">
          <Button variant="primary" onClick={handlePrevProduct} disabled={currentProductIndex === 0}>
            <i className="bi bi-caret-left-fill me-1"></i> Anterior
          </Button>
          <Button variant="primary" onClick={handleNextProduct} disabled={currentProductIndex === items.length - 1}>
            Siguiente <i className="bi bi-caret-right-fill me-1"></i>
          </Button>
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>Cerrar</Button>
      </Modal.Footer>
    </Modal>
  );
}

export default ProductModal;
