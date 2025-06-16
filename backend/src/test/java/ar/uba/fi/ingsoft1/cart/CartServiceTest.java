package ar.uba.fi.ingsoft1.cart;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ar.uba.fi.ingsoft1.exception.EntityNotFoundException;
import ar.uba.fi.ingsoft1.product.Product;
import ar.uba.fi.ingsoft1.product.ProductRepository;
import ar.uba.fi.ingsoft1.user.User;
import ar.uba.fi.ingsoft1.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Product mockProduct;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCart_userExists_shouldReturnCartDTO() {
        // Arrange
        Long userId = 1L;
        Cart cart = new Cart();
        User user = new User();
        user.setCart(cart);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        CartDTO result = cartService.getCart(userId);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getCart_userDoesNotExist_shouldThrowException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cartService.getCart(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void addProduct_validUserAndProduct_shouldAddProductToCart() {
        // Arrange
        Long userId = 1L;
        Long productId = 2L;
        Long cartId = 3L;

        Cart cart = new Cart();

        when(userRepository.findCartIdByUserId(userId)).thenReturn(cartId);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(mockProduct.getStock()).thenReturn(1);

        // Act
        cartService.addProduct(userId, productId);

        // Assert
        verify(cartRepository, times(1)).save(cart);
        assertTrue(cart.getProducts().containsKey(mockProduct));
    }


    @Test
    void removeProduct_validUserAndProduct_shouldRemoveProductFromCart() {
        // Arrange
        Long userId = 1L;
        Long productId = 2L;
        Long cartId = 3L;

        Cart cart = new Cart();
        cart.addProduct(mockProduct);

        when(userRepository.findCartIdByUserId(userId)).thenReturn(cartId);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(mockProduct.getStock()).thenReturn(2);

        // Act
        cartService.removeProduct(userId, productId);

        // Assert
        verify(cartRepository, times(1)).save(cart);
        assertFalse(cart.getProducts().containsKey(mockProduct));
    }

    @Test
    void removeProduct_invalidProduct_shouldThrowException() {
        // Arrange
        Long userId = 1L;
        Long productId = 2L;
        Long cartId = 3L;

        Cart cart = new Cart();

        when(userRepository.findCartIdByUserId(userId)).thenReturn(cartId);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cartService.removeProduct(userId, productId));
    }

    @Test
    void addProduct_invalidProduct_shouldThrowException() {
        // Arrange
        Long userId = 1L;
        Long productId = 2L;
        Long cartId = 3L;

        Cart cart = new Cart();

        when(userRepository.findCartIdByUserId(userId)).thenReturn(cartId);
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cartService.addProduct(userId, productId));
    }

    @Test
    void addProduct_invalidCartId_shouldThrowException() {
        // Arrange
        Long userId = 1L;
        Long productId = 2L;
        Long cartId = 3L;

        when(userRepository.findCartIdByUserId(userId)).thenReturn(cartId);
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cartService.addProduct(userId, productId));
    }
}

