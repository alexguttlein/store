import Container from "react-bootstrap/Container";
import {Link} from "react-router-dom";
import {Button, Col, Row} from "react-bootstrap";

/*    
    Devuelve el JSX de la pagina de inicio cuando el usuario no esta logueado
*/

export default function About() {
    return (
        <Container className="my-5 text-center">
            <Row className="mb-4">
                <Col>
                    <p className="display-4 fw-bold">Trabajo Práctico</p>
                    <p className="display-6 text-primary">Ingeniería del Software 1</p>
                </Col>
            </Row>

            <Row className="mb-5">
                <Col>
                    <p className="fs-5 text-muted mb-3">Integrantes:</p>
                    <ul className="list-unstyled fs-5">
                        <li>Alexis Daniel Guttlein Gareis</li>
                        <li>Joaquin Lonardi</li>
                        <li>Juan Cruz Robledo Puch</li>
                        <li>Martin Fernandez Lahore</li>
                        <li>Tomas Benitez Potochek</li>
                        <li>Agustín Braida</li>
                    </ul>
                </Col>
            </Row>

            <Row>
                <Col>
                    <Button variant="primary" as={Link} to="/sign-up" className="me-3" size="lg">
                    Registrarme
                    </Button>
                    <Button variant="link" as={Link} to="/login" className="text-decoration-none" size="lg">
                        <i className="bi bi-box-arrow-in-right">  </i>

                        Ya tengo cuenta
                    </Button>
                </Col>
            </Row>
        </Container>
    )

}