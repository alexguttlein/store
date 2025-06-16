package ar.uba.fi.ingsoft1.order;

// import ar.uba.fi.ingsoft1.order.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import ar.uba.fi.ingsoft1.dynamicRules.RulesLoaderRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ar.uba.fi.ingsoft1.cart.Cart;
import ar.uba.fi.ingsoft1.cart.CartRepository;
import ar.uba.fi.ingsoft1.dynamicRules.RuleLoaderService;
import ar.uba.fi.ingsoft1.exception.CartEmptyException;
import ar.uba.fi.ingsoft1.exception.EntityNotFoundException;
import ar.uba.fi.ingsoft1.exception.InvalidCartContentsException;
import ar.uba.fi.ingsoft1.exception.RuleViolationException;
import ar.uba.fi.ingsoft1.product.Product;
import ar.uba.fi.ingsoft1.product.ProductRepository;
import ar.uba.fi.ingsoft1.user.User;
import ar.uba.fi.ingsoft1.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private RuleLoaderService rulesLoaderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    
    private int max_order_cancel_time_hours = 24; 

    @Transactional
    public OrderDTO createOrder(Long userId){

        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Cart cart = user.getCart();
        if (cart.isEmpty()) {
            throw new CartEmptyException("Cannot create order from empty cart");
        }

        Order order = new Order(user); // default state: PROCESS

        var engine = rulesLoaderService.getRuleEngine();
        logger.debug("engine rule count: " + engine.getRules().size());
        System.out.println("engine rule count: " + engine.getRules().size());
        
        List<String> ruleErrors = engine.validateOrder(cart);
        logger.debug("rule evaluation result: " + ruleErrors);
        System.out.println("rule evaluation result: " + ruleErrors);
        if (!ruleErrors.isEmpty()) {
            String combinedErrors = String.join("; ", ruleErrors);
            // rule violation reason should be added to it
            throw new RuleViolationException(combinedErrors);
        }


        try {
            for (var item : cart) {
            
                item.product().removeStock(item.amount());
                order.addProduct(item);
                //  TODO: decrease stock for products by amount (if rules are valid)
            }
        } 
        catch (IllegalArgumentException e) {
            throw new InvalidCartContentsException("amounts in cart are negative");
        }
        
        productRepository.saveAll(order
                                  .getItems()
                                  .stream()
                                  .map(Item::product)
                                  .toList());
                                  
        
                                  
        cart.clearCart();
        cartRepository.save(cart);
        // orderRepository.save(order);
        return new OrderDTO(orderRepository.save(order));
    }

    public List<OrderDTO> getAllOrdersForUser(Long userId) {
        return orderRepository.findAllByOrderOwnerId(userId).stream()
        .map(OrderDTO::new)
        .toList();
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
        .map(OrderDTO::new)
        .toList();
    }

    public Page<OrderDTO> getOrdersForUser(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return orderRepository.findAllByOrderOwnerId(userId, pageable)
        .map(OrderDTO::new);
    }

    public Page<OrderDTO> getOrders(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return orderRepository.findAll(pageable)
        .map(OrderDTO::new);
    }
    
    public Page<OrderDTO> getConfirmedOrders(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return orderRepository.findAllByState(OrderState.CONFIRMED, pageable)
        .map(OrderDTO::new);
    }
    
    public Page<OrderDTO> getProcessedOrders(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        
        return orderRepository.findAllByState(OrderState.PROCESS, pageable)
        .map(OrderDTO::new);
    }
    
    public Page<OrderDTO> getSentOrders(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        
        return orderRepository.findAllByState(OrderState.SENT, pageable)
        .map(OrderDTO::new);
    }
    
    public Page<OrderDTO> getCanceledOrders(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        
        return orderRepository.findAllByState(OrderState.CANCELED, pageable)
        .map(OrderDTO::new);
    }


    public Page<OrderDTO> getAllConfirmedOrdersForUser(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findAllByOrderOwnerIdAndState(userId, OrderState.CONFIRMED, pageable)
        .map(OrderDTO::new);
    }

    public Page<OrderDTO> getAllProcessedOrdersForUser(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findAllByOrderOwnerIdAndState(userId, OrderState.PROCESS, pageable)
        .map(OrderDTO::new);
    }
    
    public Page<OrderDTO> getAllSentOrdersForUser(Long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return orderRepository.findAllByOrderOwnerIdAndState(userId, OrderState.SENT, pageable)
        .map(OrderDTO::new);
    }
    // solo para admins
    public OrderDTO getSingleOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("no such order exists"));
        return new OrderDTO(order); 
    }

    private void returnStock(List<Item> items) {
        var list = new LinkedList<Product>();
        for (var item : items) {
            var product = item.product();
            var amount = item.amount();
            product.addStock(amount);
            list.add(product);
        }
        productRepository.saveAll(list);
    }

    private boolean orderCancelable(Order order) {
        if (order.getState() != OrderState.CONFIRMED){
            return false;
        }
        var orderTime = order.getOrderDate();
        Duration timespan = Duration.between(orderTime, LocalDateTime.now());
        return timespan.toHours() <= this.max_order_cancel_time_hours;
    }

    
    public boolean canCancelOrder(Long orderId){
        var order = orderRepository
        .findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return orderCancelable(order);
    }
    
    private boolean updateOrderState(Order order, 
                                    OrderState currentState, 
                                    OrderState nextState){
        if (order.getState() != currentState){
            return false;
        }

        order.setState(nextState);
        orderRepository.save(order);
        return true;
    }

    // TODO: usar un tipo mas expresivo para indicar exito u error?
    @Transactional
    public boolean markOrderAsSent(Long orderId){
        var order = orderRepository
                    .findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return this.updateOrderState(order, OrderState.PROCESS, OrderState.SENT);
    }

    @Transactional
    public boolean markOrderAsProcessed(Long orderId){
        var order = orderRepository
        .findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return this.updateOrderState(order, OrderState.CONFIRMED, OrderState.PROCESS);
    }

    @Transactional
    public boolean cancelOrder(Long orderId) {
        var order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        boolean cancelable = orderCancelable(order);
        if (!cancelable) {return false;}
        order.setState(OrderState.CANCELED);
        this.returnStock(order.getItems());
        orderRepository.save(order);
        return true;
    }

}
