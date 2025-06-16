package orderTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import ar.uba.fi.ingsoft1.attribute.Attribute;
import ar.uba.fi.ingsoft1.cart.Cart;
import ar.uba.fi.ingsoft1.cart.CartRepository;
import ar.uba.fi.ingsoft1.dynamicRules.RuleEngine;
import ar.uba.fi.ingsoft1.dynamicRules.RuleLoaderService;
import ar.uba.fi.ingsoft1.exception.CartEmptyException;
import ar.uba.fi.ingsoft1.exception.EntityNotFoundException;
import ar.uba.fi.ingsoft1.exception.RuleViolationException;
import ar.uba.fi.ingsoft1.order.Order;
import ar.uba.fi.ingsoft1.order.OrderDTO;
import ar.uba.fi.ingsoft1.order.OrderRepository;
import ar.uba.fi.ingsoft1.order.OrderService;
import ar.uba.fi.ingsoft1.order.OrderState;
import ar.uba.fi.ingsoft1.product.Product;
import ar.uba.fi.ingsoft1.product.ProductRepository;
import ar.uba.fi.ingsoft1.user.User;
import ar.uba.fi.ingsoft1.user.UserRepository;
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private RuleLoaderService rulesLoaderService;

    @Mock
    private RuleEngine ruleEngine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_successful() {
        // Mock data
        Long userId = 1L;
        User user = new User();

        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("peso", "7"));

        Product product1 = new Product(1L, "Product 1","Product 1", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        user.setCart(cart);

        Order savedOrder = new Order(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(rulesLoaderService.getRuleEngine()).thenReturn(ruleEngine);
        when(ruleEngine.validateOrder(cart)).thenReturn(Collections.emptyList());
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Test
        OrderDTO result = orderService.createOrder(userId);

        // Assertions
        assertNotNull(result);
        verify(cartRepository).save(cart);
        verify(productRepository).saveAll(any());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testCancelOrder_success() {
        // Mock data
        Long userId = 1L;
        User user = new User();
        
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("peso", "7"));
        
        Product product1 = new Product("1L", "Product 1", 5, attributes, null);
        
        
        Order savedOrder = new Order(user);
        savedOrder.setId(3L);
        savedOrder.addProduct(product1, 2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(rulesLoaderService.getRuleEngine()).thenReturn(ruleEngine);
        when(orderRepository.findById(3L)).thenReturn(Optional.of(savedOrder));
        
        // Test
        var result = orderService.cancelOrder(3L);
        
        // Assertions
        assertEquals(true, result);
        assertEquals(OrderState.CANCELED, savedOrder.getState());
        assertEquals(5+2, product1.getStock());
        verify(productRepository).saveAll(any());
        verify(orderRepository).save(any(Order.class));

    }
    
    @Test
    void testCancelOrder_failureDueToState() {
        // Mock data
        Long userId = 1L;
        User user = new User();
        
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("peso", "7"));
        
        Product product1 = new Product(1L, "Product 1", "Product 1", 3, attributes, null);
        
        
        Order savedOrder = new Order(user);
        savedOrder.setId(3L);
        savedOrder.addProduct(product1, 2);
        // failure trigger
        // savedOrder.setOrderDate(LocalDateTime.of(2024, 11, 20, 11, 30));
        savedOrder.setState(OrderState.PROCESS);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(rulesLoaderService.getRuleEngine()).thenReturn(ruleEngine);
        when(orderRepository.findById(3L)).thenReturn(Optional.of(savedOrder));
        
        // Test
        var result = orderService.cancelOrder(3L);
        
        // Assertions
        assertEquals(false, result);
        assertEquals(OrderState.PROCESS, savedOrder.getState());
        assertEquals(3, product1.getStock());
        // verify(productRepository).saveAll(any());
        // verify(orderRepository).save(any(Order.class));
    }
    
    @Test
    void testCancelOrder_failureDueToTime() {
        // Mock data
        Long userId = 1L;
        User user = new User();
        
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("peso", "7"));
        
        Product product1 = new Product(1L, "Product 1", "Product 1", 3, attributes, null);
        
        
        Order savedOrder = new Order(user);
        savedOrder.setId(3L);
        savedOrder.addProduct(product1, 2);
        // failure trigger
          // cualquier fecha a mas de 1 dia de hoy
        savedOrder.setOrderDate(LocalDateTime.of(2024, 11, 20, 11, 30));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(rulesLoaderService.getRuleEngine()).thenReturn(ruleEngine);
        when(orderRepository.findById(3L)).thenReturn(Optional.of(savedOrder));
        
        // Test
        var result = orderService.cancelOrder(3L);
        
        // Assertions
        assertEquals(false, result);
        assertEquals(OrderState.CONFIRMED, savedOrder.getState());
        assertEquals(3, product1.getStock());
        // verify(productRepository).saveAll(any());
        // verify(orderRepository).save(any(Order.class));
    }

    @Test
    void testCreateOrder_userNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            orderService.createOrder(userId);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testCreateOrder_emptyCart() {
        Long userId = 1L;
        User user = new User();
        Cart cart = new Cart(); // Empty cart
        user.setCart(cart);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        CartEmptyException exception = assertThrows(CartEmptyException.class, () -> {
            orderService.createOrder(userId);
        });

        assertEquals("Cannot create order from empty cart", exception.getMessage());
    }

    @Test
    void testCreateOrder_ruleViolation() {
        Long userId = 1L;
        User user = new User();
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new Attribute("peso", "7"));

        Product product1 = new Product(1L, "Product 1", "Product 1", 1, attributes, null);

        Cart cart = new Cart();
        cart.addProduct(product1);
        user.setCart(cart);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(rulesLoaderService.getRuleEngine()).thenReturn(ruleEngine);
        when(ruleEngine.validateOrder(cart)).thenReturn(List.of("Rule 1 violated", "Rule 2 violated"));

        RuleViolationException exception = assertThrows(RuleViolationException.class, () -> {
            orderService.createOrder(userId);
        });

        assertEquals("Rule 1 violated; Rule 2 violated", exception.getMessage());
    }
}
