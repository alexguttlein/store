import { useParams } from "react-router-dom";
import Body from "../components/common/Body";
import Product from "../components/products/Product";
import Cart from "../components/cart/Cart";
import { Button, Spinner, Container, Alert } from "react-bootstrap";
import { Link } from "react-router-dom";

import { useCart } from "../contexts/CartProvider";
import { useEffect } from "react";

/*  Componente ProductPage:
    Devuelve el JSX de la vista detallada del producto. 
    Define la funcion que sera llamada al enviar el form de agregar producto al carrito luego de ser validado
    Obtiene el producto productId del servidor
*/

export default function CartPage() {
  const { cart, fetchCart } = useCart();
  useEffect(() => {
    fetchCart();
  }, []);

  if (cart === undefined) return <Spinner animation="border" />;
  return (
    <Body>
      <Container className="my-5">
        <Cart cart={cart} />
        <Button
          variant="link"
          as={Link}
          to="/products"
          className="text-decoration-none"
          size="lg"
        >
          Volver a lista de productos
        </Button>
      </Container>
    </Body>
  );
}
