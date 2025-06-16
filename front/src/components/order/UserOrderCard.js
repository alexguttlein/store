import { Card, Button } from 'react-bootstrap';
import { useState } from 'react';
import ProductModal from './ProductModal';

export default function UserOrderCard({ order, cancelOrder }) {
  const [showModal, setShowModal] = useState(false);

  const statusSpans = {
    CONFIRMED: "Confirmado",
    PROCESS: "En proceso",
    SENT: "Enviado",
    CANCELED: "Cancelado",
  };

  const statusStyles = {
    CONFIRMED: "bg-secondary",
    PROCESS: "bg-primary",
    SENT: "bg-success",
    CANCELED: "bg-danger",
  };

  const handleCancelOrder = () => {
    cancelOrder(order.id);
  };

  const handleShowProducts = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  return (
    <>
      <Card className="order-card shadow-sm mb-3">
        <Card.Header className="d-flex justify-content-between align-items-center border-3 rounded-top bg-transparent p-3">
          <h3 className="fw-bold">Id: {order.id}</h3>
          <span className={`badge ${statusStyles[order.status]}`}>
            {statusSpans[order.status]}
          </span>
        </Card.Header>

        <Card.Body className="p-3">
          <div className="mb-3">
            <span className="fw-bold">Fecha de confirmación:</span> {order.date}
          </div>

          <div className="d-flex justify-content-between gap-3">
            {order.items.length !== 0 && 

            <Button variant="link" size="sm" onClick={handleShowProducts}>
              Ver productos
            </Button>
            }
            {order.isCancelable && (
              <Button variant="danger" size="sm" onClick={handleCancelOrder}>
                Cancelar Pedido
              </Button>
            )}
          </div>
        </Card.Body>
      </Card>

      {order.items.length !== 0 && 
      <ProductModal show={showModal} onHide={handleCloseModal} items={order.items} />
      }
      </>
  );
}
