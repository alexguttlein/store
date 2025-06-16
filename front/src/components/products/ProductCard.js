import {Card, Figure} from 'react-bootstrap'
import { Link } from 'react-router-dom'
import placeHolderImage from '../../res/placeHolderImage.png'

/*
    Muestra la informacion resumida de un producto que se vera en la lista de productos
    Define como se vera la tarjeta dependiendo del estado del producto 
*/

function capitalize(s) {
  return s && String(s[0]).toUpperCase() + String(s).slice(1);
}

export default function ProductCard({product}) {
  const attributesToShow = 2
  const attributesSpans = []

  for (const [key, value] of Object.entries(product.attributes).slice(0, attributesToShow)) {
      attributesSpans.push(
        <span className='fw-bold d-block' key={key}>{capitalize(key) + ": " + value}</span>
      )
  }

  if (Object.keys(product.attributes).length > attributesToShow){
    attributesSpans.push(
      <span className='d-block'>{
        "+" + (Object.keys(product.attributes).length - attributesToShow) + " atributos"}
        </span>
    )
  }
    
  return (
    <Card
      as={Link}
      to={'/products/' + product.alternativeId + '?productId='+product.id} 
      style={{
        cursor: 'pointer',
        textDecoration: 'none',
      }}
      className='product-card shadow-sm mb-3'
    >
      <Card.Header
        className="d-flex justify-content-between align-items-center border-3 rounded-top"
        style={{ backgroundColor: 'transparent', padding: '1rem' }}
      >
        <h3 className='fw-bold'>{capitalize(product.name)}</h3>
        <span
          className={`badge ${
            product.stock > 0 ? 'bg-success text-light' : 'bg-danger text-light'
          }`}
        >
          {product.stock > 0 ? 'En stock' : 'Sin Stock'}
        </span>
      </Card.Header>

      <Card.Img
        variant="top"
        src={product.photo !== "" ? product.photo : placeHolderImage}
        alt={`Foto de ${product.name}`}
        style={{
          height: '200px', 
          objectFit: 'cover', 
          borderBottom: '2px solid #e1e4e8'
        }}
      />
      <Card.Body className="p-3" border>
        
        {attributesSpans}
          
      </Card.Body>
    </Card>
  );
};
