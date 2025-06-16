/*package ar.uba.fi.ingsoft1.cart;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// @SpringBootTest
// @RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
public class CartCreationTest {

    @Autowired
    private CartService cartService;

    @MockBean
    private CartRepository cartRepository;

    @Test
    public void testCreateCart() {
        Cart cart = new Cart();
        cart.setId(1L);

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // Asumiendo que tienes un método de servicio para crear un carrito
        CartDTO createdCart = new CartDTO(1L, List.of());

        assertNotNull(createdCart);
        assertEquals(1L, createdCart.id());
        verify(cartRepository).save(any(Cart.class));
    }

}
*/