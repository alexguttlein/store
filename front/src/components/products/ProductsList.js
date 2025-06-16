import ProductCard from './ProductCard'

import {Col} from 'react-bootstrap'

/*
    Define el layout de como se van a mostrar los productCards y pasa el correspondiente producto a cada uno de ellos
    Recibe como prop la lista de productos a mostrar
*/

export default function ProductsList({ isAdmin, productsList }) {
    return (
        <>
        {productsList.map(product => (
            <Col as="article" key={product.id} xs={12} sm={6} md={6} lg={3} className="d-flex align-items-stretch">
                <ProductCard product={product} />
            </Col>
        ))}
        </>
    );
}