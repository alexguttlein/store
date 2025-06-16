import Body from '../components/common/Body';
import ProductsList from '../components/products/ProductsList';
import {Container, Spinner,Row, Col} from 'react-bootstrap'
import { useEffect } from 'react';
import {useSearchParams} from 'react-router-dom'
import { useProductList } from '../contexts/ProductListProvider'
import { useUser } from '../contexts/UserProvider'
import AddProductCard from '../components/products/AddProductCard';


import PaginationWidget from "../components/common/PaginationWidget";

/*  Componente ProductsListPage:
    Devuelve el JSX de la lista de productos. 
    Obtiene los productos y la info de paginacion del servidor
*/

export default function ProductsListPage() {
    const {productList, paginationState, fetchProductListPage} = useProductList()
    const {isAdmin} = useUser()
    const [searchParams, setSearchParams] = useSearchParams();

    useEffect(() => {
        const page = Number(searchParams.get("page")) || 0
        const page_size = Number(searchParams.get("page_size")) || 9
        fetchProductListPage(page, page_size)
    }, [searchParams, fetchProductListPage])

  const loadPageNumber = async (number) => {
    searchParams.set("page", number);
    setSearchParams(searchParams);
    const page_size = Number(searchParams.get("page_size"));

    fetchProductListPage(number, page_size ? page_size : 9);
  };

  if (productList === undefined) return <Spinner animation="border" />;

  return (
    <Body>
      <Container className="d-flex flex-column p-3">
              <span className='fw-bold'>Click en un producto para agregar al carrito o ver otras posibles variantes</span>

              <Container>
                  <Row className="g-4 my-3">
                      {isAdmin() && 
                          <Col as="article" xs={12} sm={6} md={6} lg={3} className="d-flex align-items-stretch">
                          <AddProductCard/>
                          </Col>
                      }
                  {productList === null || productList.length === 0 ?
                    <p>No hay productos disponibles.</p> 
                    :
                    <ProductsList isAdmin={isAdmin()} productsList={productList}/>
                   }
                  </Row>
              </Container>
                {paginationState &&
                    <PaginationWidget pagination={paginationState} loadPageNumber={loadPageNumber}/>
                }
            </Container>
        </Body>
    );
}
