import Body from '../components/common/Body';
import {Container, Spinner} from 'react-bootstrap'
import { useEffect } from 'react';
import {useSearchParams} from 'react-router-dom'
import { useOrderList } from '../contexts/OrderListProvider'
import { useUser } from '../contexts/UserProvider'
import { useFlash } from '../contexts/FlashProvider'
import { useApi } from '../contexts/ApiProvider'


import PaginationWidget from "../components/common/PaginationWidget";
import AdminOrderList from '../components/order/AdminOrderList';
import UserOrderList from '../components/order/UserOrderList';


/*  Componente AdminOrderListPage:
    Devuelve el JSX de la lista de todos los pedidos. 
    Obtiene los pedidos y la info de paginacion del servidor
*/

export default function OrderListPage() {
    const {orderList, paginationState, fetchOrderListPage} = useOrderList()
    const {api} = useApi()
    const {isAdmin} = useUser()
    const [searchParams, setSearchParams] = useSearchParams();

    const flash = useFlash()


    useEffect(() => {
        const page = Number(searchParams.get("page"))
        const page_size = Number(searchParams.get("page_size"))
        fetchOrderListPage(page ? page : 0, page_size ? page_size : 9)
        
    }, [searchParams, fetchOrderListPage])

    const loadPageNumber = async (number) => {
        searchParams.set("page", number);
        setSearchParams(searchParams);
        const page_size = Number(searchParams.get("page_size"));
        fetchOrderListPage(number, page_size ? page_size : 9);

    };
  
    const handleCancelOrder = async (orderId) => {
        try{
            const response = await api.post(`/orders/${orderId}/cancel`);

            if (!response.ok) {
                flash(response.body.message || 'Error inesperado al conectarse con el server');
            } else {
                flash('Pedido cancelado exitosamente.', 'success')
                fetchOrderListPage(paginationState.page, paginationState.per_page)
            }
        } catch (error) {
            flash('Error inesperado ' + error.message, 'danger')
        }

    }

    const handleSendOrder = async (orderId) => {
        try{
            const response = await api.post(`/orders/${orderId}/dispatch`);
            if (!response.ok) {
                flash(response.body.message || 'Error inesperado al conectarse con el server');
            } else {
                flash('Pedido marcado como enviado exitosamente.', 'success')
                fetchOrderListPage(paginationState.page, paginationState.per_page)
            }
        } catch (error) {
            flash('Error inesperado ' + error.message, 'danger')
        }
    }

    const handleProcessOrder = async (orderId) => {
        try{
            const response = await api.post(`/orders/${orderId}/process`);
            if (!response.ok) {
                flash(response.body.message || 'Error inesperado al conectarse con el server');
            } else {
                flash('Pedido marcado como en proceso exitosamente.', 'success')
                fetchOrderListPage(paginationState.page, paginationState.per_page)
            }
        } catch (error) {
            flash('Error inesperado ' + error.message, 'danger')
        }
    }

  if (orderList === undefined) return <Spinner animation="border" />;
  if (orderList === null) return <p>No se pudieron obtener los pedidos</p>;
  if (orderList === null || orderList.length === 0){
    return (
        <Body>
            <Container>
                <p>No hay pedidos disponibles o hay pedidos invalidos en la pagina</p>

                {paginationState &&
                    <PaginationWidget pagination={paginationState} loadPageNumber={loadPageNumber}/>
                }
            </Container>
        </Body>
    )
  } 

  return (
    <Body>
      <Container className="d-flex flex-column p-3">
                {isAdmin() ?
                    <AdminOrderList orderList={orderList} sendOrder={handleSendOrder} processOrder={handleProcessOrder}/>
                    :
                    <UserOrderList orderList={orderList} cancelOrder={handleCancelOrder}/>
                }

                {paginationState &&
                    <PaginationWidget pagination={paginationState} loadPageNumber={loadPageNumber}/>
                }
            </Container>
        </Body>
    );
}
