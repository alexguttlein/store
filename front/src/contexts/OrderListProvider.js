import React, { createContext, useContext, useState } from 'react';
import { useApi } from './ApiProvider';
import { useCallback } from 'react';
const OrderContext = createContext();

function isCancelable(date, status) {
    const now = new Date(); // Current date and time
    const oneDayInMs = 24 * 60 * 60 * 1000; // Milliseconds in one day
    const givenDate = new Date(date); // Convert the input to a Date object
  
    return (now - givenDate < oneDayInMs) && status === "CONFIRMED"; // Check if the difference is less than one day
  }
  

export default function OrderListProvider({ children }) {
    const [orderList, setOrderList] = useState();
    const [paginationState, setPaginationState] = useState();
    const {api} = useApi();

    const isOrderDataValid = (order) => {
        return order &&
                order.id != null
    }

    const mapOrdersListData = (orders) => {
        const validOrders = orders.filter(o => isOrderDataValid(o))

        const invalidOrdersLength = orders.length - validOrders.length;
    
        if (invalidOrdersLength > 0) {
            console.warn(`Hay ${invalidOrdersLength} pedidos que no están siendo mostrados debido a información inválida.`);
        }

        const ordersData =  validOrders.map((order, index) => ({
            id: order.id,
            owner: order.idOwner || -1,
            items: order.items || [],
            status: order.state || "UNDEFINED",
            date: order.orderDate.slice(0, 10) || 'UNDEFINED',
            isCancelable: isCancelable(order.orderDate, order.state) || false
        }));
        
        return ordersData

    };

    const fetchOrderListPage = useCallback(async (page, per_page) => {
        setOrderList();
        setPaginationState();
        try {
            const response = await api.get(`/orders?pageNo=${page}&pageSize=${per_page}`);

            if (!response.ok || !response.body.content) {
                setOrderList(null);
                setPaginationState(null);
                console.error("Error al recuperar pedidos del server");
                return
            }
            setOrderList(mapOrdersListData(response.body.content));
            const { pageable, totalPages } = response.body;

            if (pageable.pageNumber != null && totalPages != null) {
                setPaginationState({
                    page: pageable.pageNumber,
                    page_count: totalPages - 1,
                    per_page:response.body.pageable.pageSize
                });
            } else {
                setPaginationState({
                    page: 0,
                    page_count: 0,
                    per_page: response.body.content.length, 
                });
                if (pageable !== "INSTANCE")
                    console.warn("Datos de paginación inválidos recibidos del servidor");
            }

        } catch (error) {
            setOrderList(null);
            setPaginationState(null);
            console.error("Error al recuperar productos del server:", error);
            return
        }
    }, [api]);

    return (
        <OrderContext.Provider value={{ orderList, paginationState, fetchOrderListPage }}>
            {children}
        </OrderContext.Provider>
    );
}

export const useOrderList = () => {
    return useContext(OrderContext);
};
