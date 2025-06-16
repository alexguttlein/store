import { useParams } from 'react-router-dom';
import Body from '../components/common/Body';
import Product from '../components/products/Product';

import {Button, Spinner, Container, Alert} from "react-bootstrap";
import {Link} from "react-router-dom";

import { useEffect, useState } from 'react';
import { useFlash } from '../contexts/FlashProvider';
import { useProduct } from '../contexts/ProductProvider'
import { useUser } from '../contexts/UserProvider'
import { useApi } from '../contexts/ApiProvider'


/*  Componente ProductPage:
    Devuelve el JSX de la vista detallada del producto. 
    Define la funcion que sera llamada al enviar el form de agregar producto al carrito luego de ser validado
    Obtiene el producto productId del servidor
*/


export default function ProductPage() {
    const { productId } = useParams();
    const {product, fetchProduct} = useProduct()
    const {isAdmin} = useUser()

    const flash = useFlash()
    const {api} = useApi()

    const [isLoading, setIsLoading] = useState(false)
    const [errorMessage, setErrorMessage] = useState('')

    useEffect(() => {
        fetchProduct(productId)
    }, [productId, fetchProduct]);
        
    if (product === undefined) return <Spinner animation="border" />;
    if (product === null || product.stock === 0) return <p>No se pudo obtener el producto</p>;

    const handleAddProductToCart = async formData => {
      setIsLoading(true)
      setErrorMessage('')

      try {
        const data = await api.post('/cart_products/' + formData.id)
  
        if (!data.ok) {
          setErrorMessage(
            data.body.message
              ? data.body.message
              : 'Error inesperado al conectarse con el server'
          )
        } else {
          flash('Producto agregado exitosamente.', 'success')    
        }
  
        return
      } catch (error) {
        setErrorMessage(error.message)
      } finally {
        setIsLoading(false)
      }
    }
    
    const handleCreateVariant = async formData => {
      setIsLoading(true)
      setErrorMessage('')

      flash(JSON.stringify(formData), 'success', 5)

      try {
        const data = await api.post('/products', {productName: product.name,
          photo: "x", 
          alternativeId: product.alternativeID, 
          stock: 1,
          attributeIds: formData["ids"]}) 
  
        if (!data.ok) {
          setErrorMessage(
            data.body.message
              ? data.body.message
              : 'Error inesperado al conectarse con el server'
          )
        } else {
          flash('Stock editado exitosamente.', 'success')    
        }
  
        return
      } catch (error) {
        setErrorMessage(error.message)
      } finally {
        setIsLoading(false)
        fetchProduct(productId)
      }
    }
    
    const handleEditStock = async formData => {
        setIsLoading(true)
        setErrorMessage('')

        try {
          const data = await api.patch('/products/' + formData.id, {stock: formData.newStock}) 
    
          if (!data.ok) {
            setErrorMessage(
              data.body.message
                ? data.body.message
                : 'Error inesperado al conectarse con el server'
            )
          } else {
            flash('Stock editado exitosamente.', 'success')    
          }
    
          return
        } catch (error) {
          setErrorMessage(error.message)
        } finally {
          setIsLoading(false)
          fetchProduct(productId)
        }
    }
    
    return (
        <Body>
            <Container className="my-5">
                <Product as="article" isAdmin={isAdmin()} product={product} 
                onAddProductToCartSubmit={handleAddProductToCart} 
                onCreateVariantSubmit={handleCreateVariant}
                onEditStockSubmit={handleEditStock}
                />
                <Button variant="link" as={Link} to="/products" className="text-decoration-none" size="lg">
                    Volver a lista de productos
                </Button>
                {errorMessage && <Alert variant='danger'>{errorMessage}</Alert>}
                {isLoading && <Spinner animation='border' />}

            </Container>
        </Body>
    );
}