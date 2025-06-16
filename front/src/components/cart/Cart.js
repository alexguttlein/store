import {
  Card,
  Button,
  Container,
} from "react-bootstrap";

import { useState, useEffect } from "react";
import { useCart } from "../../contexts/CartProvider";
import { useApi } from "../../contexts/ApiProvider";
import { useFlash } from '../../contexts/FlashProvider';
import AttributeModal from "./AttributeModal";

export default function Cart({ cart }) {
  const [products, setProducts] = useState(null);
  const {fetchCart } = useCart();
  const {api} = useApi();
  const flash = useFlash()
  const [activeModal, setActiveModal] = useState(null);

  const deleteProduct = async (productId) => {
    try {
      const data = await api.delete(`/cart_products/${productId}`);

      if (!data.ok) {
        flash(
          data.body.message || 'Error inesperado al conectarse con el server'
        )
      } else {
        fetchCart();
      }
    } catch (error) {
      flash(error.message)
    }
  };

  const increaseProduct = async (productId) => {
    try {
      const data = await api.post('/cart_products/' + productId)

      if (!data.ok) {
        flash(
          data.body.message ||'Error inesperado al conectarse con el server'
        )
      } else {
        fetchCart();  
      }

      return
    } catch (error) {
      flash(error.message)
    }
  };

  const checkoutCart = async () => {
    try {
      const data = await api.post(`/checkout`);

      if (!data.ok) {
        if (data.status === 409) {
          flash(data.body)
        } else {
          flash(
            data.body.message || 'Error inesperado al conectarse con el server'
          )
        }
      } else {
        flash('Pedido creado correctamente', 'success')    
        fetchCart();
      }
    } catch (error) {
      flash(error.message)
    }
  }

  useEffect(() => {
    if (cart.products) {
      setProducts(cart.products);
    }
  }, [cart]);

  const handleShowAttributes = (productId) => {
    setActiveModal(productId);
  };

  const handleCloseModal = () => {
    setActiveModal(null);
  };

  return (
    <Card className="shadow-sm p-3 mb-4 rounded">
      <Card.Header
        className="d-flex justify-content-between align-items-center border-3 rounded-top"
        style={{ backgroundColor: "transparent" }}
      >
        <h2 className="fw-bold text-primary display-6">
          Tienes estos productos:
        </h2>
      </Card.Header>

      <Card.Body>
        <Container className="p-3">
          {products ? (
            products.map((item) => {
              return (
                <>
                <div
                  key={item.product.id}
                  className="d-flex justify-content-between align-items-center mb-3 p-3 border rounded bg-light"
                >
                  <span className="text-dark fw-bold">
                    <div className="fw-bold text-secondary lead">
                      {item.product.productName}
                      <Button variant="link" size="sm" onClick={() => handleShowAttributes(item.product.id)}>
                      Ver atributos
                    </Button>
                    </div>


                    <div>
                       Cantidad: {item.amount}
                    </div>

                  </span>
                  <div>
                    <Button className="me-2" variant="secondary" onClick={() => increaseProduct(item.product.id)}>
                      <i className="bi bi-plus-lg"></i>
                    </Button>
                    <Button className="me-2" variant="secondary" onClick={() => deleteProduct(item.product.id)}>
                      <i className="bi bi-dash-lg"></i>
                    </Button>
                  </div>
                </div>

                <AttributeModal show={activeModal === item.product.id} onHide={handleCloseModal} attributes={item.product.attributes} />

              </>
              );
            })

          ) : (
            <></>
          )}
          { products && products.length !== 0 &&
            <Button variant="primary" onClick={() => checkoutCart()}>
              Confirmar Pedido
              </Button>          
          }
        </Container>
      </Card.Body>
    </Card>
  );
}
