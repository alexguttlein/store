package ar.uba.fi.ingsoft1.cart;

import ar.uba.fi.ingsoft1.authorization.AuthorizationContext;
import ar.uba.fi.ingsoft1.order.Order;
import ar.uba.fi.ingsoft1.order.OrderDTO;
import ar.uba.fi.ingsoft1.order.OrderService;
import ar.uba.fi.ingsoft1.exception.*;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private OrderService orderService;

    @Mock
    private Claims claims;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        AuthorizationContext.setClaims(claims);
    }

    @Test
    void testGetCart() {
        Long userId = 1L;
        Cart cart = new Cart();
        CartDTO cartDTO = new CartDTO(cart);
        when(claims.get("userId", Long.class)).thenReturn(userId);
        when(cartService.getCart(userId)).thenReturn(cartDTO);

        ResponseEntity<CartDTO> response = cartController.getCart();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartDTO, response.getBody());
    }

    @Test
    void testAddProduct() {
        Long userId = 1L;
        Long productId = 1L;
        when(claims.get("userId", Long.class)).thenReturn(userId);

        ResponseEntity<Void> response = cartController.addProduct(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).addProduct(userId, productId);
    }

    @Test
    void testRemoveProduct() {
        Long userId = 1L;
        Long productId = 1L;
        when(claims.get("userId", Long.class)).thenReturn(userId);

        ResponseEntity<Void> response = cartController.removeProduct(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(cartService, times(1)).removeProduct(userId, productId);
    }



    @Test
    void testCheckoutEntityNotFoundException() {
        Long userId = 1L;
        when(claims.get("userId", Long.class)).thenReturn(userId);
        when(orderService.createOrder(userId)).thenThrow(new EntityNotFoundException("Entity not found"));

        ResponseEntity<?> response = cartController.checkout();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody());
    }

    @Test
    void testCheckoutCartEmptyException() {
        Long userId = 1L;
        when(claims.get("userId", Long.class)).thenReturn(userId);
        when(orderService.createOrder(userId)).thenThrow(new CartEmptyException());

        ResponseEntity<?> response = cartController.checkout();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Your cart is empty", response.getBody());
    }

    @Test
    void testCheckoutNotEnoughStockException() {
        Long userId = 1L;
        when(claims.get("userId", Long.class)).thenReturn(userId);
        when(orderService.createOrder(userId)).thenThrow(new NotEnoughStockException("Product"));

        ResponseEntity<?> response = cartController.checkout();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Not enough stock to buy Product", response.getBody());
    }

    @Test
    void testCheckoutInvalidCartContentsException() {
        Long userId = 1L;
        when(claims.get("userId", Long.class)).thenReturn(userId);
        when(orderService.createOrder(userId)).thenThrow(new InvalidCartContentsException());

        ResponseEntity<?> response = cartController.checkout();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Please check your cart again.", response.getBody());
    }

    @Test
    void testCheckoutRuleViolationException() {
        Long userId = 1L;
        when(claims.get("userId", Long.class)).thenReturn(userId);
        when(orderService.createOrder(userId)).thenThrow(new RuleViolationException("Rule violated"));

        ResponseEntity<?> response = cartController.checkout();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Can't create an order for your cart for the following reason: Rule violated", response.getBody());
    }
}