import Body from '../components/common/Body';

import {Button, Container} from "react-bootstrap";
import {Link} from "react-router-dom";

import { useFlash } from '../contexts/FlashProvider';
import { useApi } from '../contexts/ApiProvider'
import { useNavigate } from 'react-router-dom'

import NewProductForm from '../components/products/NewProductForm'

export default function NewProductPage() {
    const { api } = useApi()
    
    const flash = useFlash()
    const navigate = useNavigate();

    const handleAddProduct = async formData => {

        try {
            const data = await api.post('/variants', {productName: formData.productName, productPhoto: formData.productPhoto,
              variants: formData.variants}) 

            if (!data.ok) {
                flash(
                data.body.message
                  ? data.body.message
                  : 'Error inesperado al conectarse con el server'
              )
            } else {
                flash("Producto creado exitosamente")
                navigate("/products")
            }
      
          } catch (error) {
            flash(error.message)
          }
    }
    
    return (
        <Body>
            <Container className="my-5">
                <h2 className='fw-bold display-6 mb-3'>Agregar Producto</h2>

                <NewProductForm onSubmit={handleAddProduct}></NewProductForm>
                <Button variant="link" as={Link} to="/products" className="text-decoration-none" size="lg">
                    Volver a lista de productos
                </Button>
            </Container>
        </Body>
    );
}