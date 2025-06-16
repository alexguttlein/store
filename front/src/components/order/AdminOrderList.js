import AdminOrderCard from "./AdminOrderCard"

import {Container, Row, Col} from 'react-bootstrap'

/*
    Define el layout de como se van a mostrar los productCards y pasa el correspondiente producto a cada uno de ellos
    Recibe como prop la lista de productos a mostrar
*/

export default function AdminOrderList({ orderList, sendOrder,processOrder }) {
    const handleSendOrder = (id) => {
        sendOrder(id)
    } 

    const handleProcessOrder = (id) => {
        processOrder(id)
    } 

    return (
        <Container>
            <Row className="g-4 my-3">

                {orderList.map(order => (
                    <Col as="article"xs={12} key={order.id} className="d-flex align-items-stretch">
                        <AdminOrderCard order={order} sendOrder={handleSendOrder} processOrder={handleProcessOrder} />
                    </Col>
                ))}
            </Row>
        </Container>
    );
}