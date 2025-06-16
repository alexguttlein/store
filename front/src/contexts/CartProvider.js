import React, { createContext, useContext, useState } from "react";
import { useApi } from "./ApiProvider";

const CartContext = createContext();

export default function CartProvider({ children }) {
  const [cart, setCart] = useState();
  const [itemsQty, setItemsQty] = useState(null);
  const {api} = useApi();

  const fetchCart = async () => {
    setCart();
    setItemsQty();

    try {
      const response = await api.get(`/cart`);
      if (!response.ok || !response.body) {
        setCart(null);
        setItemsQty(null);

        console.error("Error al recuperar carrito del server");
        return  
      }

      setCart(response.body);
      setItemsQty(response.body.length);
    } catch (error) {
        setCart(null);
        setItemsQty(null);
        
        console.error("Error al recuperar carrito del server");
        return  
    }

  };

  return (
    <CartContext.Provider value={{ cart, fetchCart, itemsQty, setItemsQty }}>
      {children}
    </CartContext.Provider>
  );
}

export const useCart = () => useContext(CartContext);
