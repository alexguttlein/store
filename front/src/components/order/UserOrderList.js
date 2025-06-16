import UserOrderCard from "./UserOrderCard"

import {Container, Row, Col} from 'react-bootstrap'

/*
    Define el layout de como se van a mostrar los productCards y pasa el correspondiente producto a cada uno de ellos
    Recibe como prop la lista de productos a mostrar
*/

export default function UserOrderList({ orderList, cancelOrder }) {
    const onCancelOrder = (id) => {
        cancelOrder(id)
    } 

    return (
        <Container>
            <Row className="g-4 my-3">

                {orderList.map(order => (
                    <Col as="article"xs={12} key={order.id} className="d-flex align-items-stretch">
                        <UserOrderCard order={order} cancelOrder={onCancelOrder} />
                    </Col>
                ))}
            </Row>
        </Container>
    );
}