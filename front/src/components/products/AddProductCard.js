import Card from 'react-bootstrap/Card'
import { Link } from 'react-router-dom'

export default function AddProductCard() {    
  return (
    <Card
      as={Link}
      to={'/products/new'} 
      style={{
        cursor: 'pointer',
        textDecoration: 'none',
        opacity: 1,
        pointerEvents: 'auto',
      }}
      className='product-card shadow-sm mb-3'
    >
      <Card.Header
        className="d-flex justify-content-between border-3 rounded-top"
        style={{ backgroundColor: 'transparent', padding: '1rem' }}
      >
        <h3 className='fw-bold'>Agregar Producto</h3>
      </Card.Header>

      <Card.Body 
        className="d-flex justify-content-center align-items-center"
        style={{ padding: '1rem'}} 
      >
        <i className="bi bi-plus-circle text-dark" style={{ fontSize: '3rem', color: '#6c757d' }}></i>
      </Card.Body>
    </Card>
  );
}
