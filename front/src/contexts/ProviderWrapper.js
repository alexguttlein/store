import FlashProvider from "./FlashProvider";
import ApiProvider from "./ApiProvider";
import UserProvider from "./UserProvider";
import CartProvider from "./CartProvider";

/**
 * Combina providers en uno solo
 * Agregar mas providers de toda la app aca!!
 */
function ProviderWrapper({ children }) {
  return (
    <FlashProvider>
      <ApiProvider>
        <CartProvider>
          <UserProvider>{children}</UserProvider>
        </CartProvider>
      </ApiProvider>
    </FlashProvider>
  );
}

export default ProviderWrapper;