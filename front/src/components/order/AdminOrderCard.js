import { Card, Button, ButtonGroup } from 'react-bootstrap';
import { useState } from 'react';
import ProductModal from './ProductModal';

export default function AdminOrderCard({ order, sendOrder, processOrder }) {
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

  const handleOpenModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

  const renderActionButton = (text, action, variant = "secondary") => (
    <Button
      variant={variant}
      size="sm"
      className="me-2"
      onClick={() => action(order.id)}
    >
      {text}
    </Button>
  );

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
          <div className="mb-2">
            <span className="fw-bold">Usuario:</span> {order.owner}
          </div>
          <div className="mb-3">
            <span className="fw-bold">Fecha de confirmación:</span> {order.date}
          </div>

          <div className="d-flex justify-content-between gap-3">
           {order.items.length !== 0 && 

            <Button variant="link" size="sm" onClick={handleOpenModal}>
              Ver productos
            </Button>
            }
            <ButtonGroup>
            {order.status === "PROCESS" && 
              renderActionButton("Marcar como enviado", sendOrder)}
            {order.status === "CONFIRMED" &&
              renderActionButton("Marcar como en proceso", processOrder)}
            </ButtonGroup>
          
          </div>
        </Card.Body>
      </Card>

      {order.items.length !== 0 && 
      <ProductModal show={showModal} onHide={handleCloseModal} items={order.items} />
      }
    </>
  );
}
