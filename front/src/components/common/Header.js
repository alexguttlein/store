import { Navbar, Container, Spinner, NavDropdown, Nav } from "react-bootstrap";
import { useUser } from "../../contexts/UserProvider";
import { useCart } from "../../contexts/CartProvider";
import { useNavigate , Link} from "react-router-dom";

/*    
    JSX de la barra de navegacion
*/

export default function Header() {
  const { user, logout, isAdmin } = useUser();
  const { itemsQty } = useCart();
  const navigate = useNavigate();

  if (user === undefined)
    return (
      <Navbar data-bs-theme="dark" className="bg-body-tertiary" sticky="top">
        <Container>
          <Spinner animation="border" />
        </Container>
      </Navbar>
    );

  return (
    <Navbar
      data-bs-theme="dark"
      className="bg-body-tertiary"
      sticky="top"
      expand="lg"
    >
      <Container>
        <Navbar.Brand as={Link} to="/" className="d-flex align-items-center">
          <i className="bi bi-code-slash me-2"></i> Tp Grupo 11
        </Navbar.Brand>

        <Navbar.Text className="ms-3 text-light">
          Ingeniería de Software 1
        </Navbar.Text>

        <Navbar.Toggle aria-controls="navbar-content" />

        <Navbar.Collapse id="navbar-content" className="justify-content-end">
          <Nav>
            {user && (
              <>
                <NavDropdown.Item
                  onClick={() => {
                    console.log("Navigating to orders");
                    navigate("/order");
                  }}
                  className="text-light d-flex align-items-center me-4"
                >
                <i className=" me-1"></i>Ver pedidos

                </NavDropdown.Item>
                {!isAdmin() &&
                <NavDropdown.Item
                  onClick={() => {
                    console.log("Navigating to cart");
                    navigate("/cart");
                  }}
                  className="text-light d-flex align-items-center me-4"
                >
                  <button className="btn position-relative btn-outline-light d-flex align-items-center">
                    <i className="bi bi-cart2 me-2"></i> Carrito
                    <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                      {itemsQty ? itemsQty : ""}
                    </span>
                  </button>
                </NavDropdown.Item>
                }
                <NavDropdown.Item
                  onClick={logout}
                  className="text-light d-flex align-items-center me-4"
                >
                  <i className="bi bi-box-arrow-in-left me-1"></i>Cerrar sesión
                </NavDropdown.Item>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}
