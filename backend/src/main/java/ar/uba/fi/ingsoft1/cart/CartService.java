package ar.uba.fi.ingsoft1.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.uba.fi.ingsoft1.exception.EntityNotFoundException;
import ar.uba.fi.ingsoft1.product.Product;
import ar.uba.fi.ingsoft1.product.ProductRepository;
import ar.uba.fi.ingsoft1.user.UserRepository;
import lombok.RequiredArgsConstructor;



@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private final CartRepository cartRepository;

    @Autowired
    private final ProductRepository productRepository;
   
    @Autowired
    private final UserRepository userRepository;


    private Cart findCartByUserId(Long userId){
        var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("no such user exists"));
        var cart = user.getCart();
        return cart;
    }

    // Listar todos los productos en el carrito
    public CartDTO getCart(Long userId) {
        var cart = findCartByUserId(userId);
        return new CartDTO(cart);
    }

    // Agregar un producto al carrito
    public void addProduct(Long userId, Long productId) {
        var cartId = userRepository.findCartIdByUserId(userId);
        var cart = cartRepository
                     .findById(cartId)
                     .orElseThrow(() -> new EntityNotFoundException("Incorrect cart ID. got: " + cartId.toString()));
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        cart.addProduct(product);
        cartRepository.save(cart);
    }

    // Eliminar un producto del carrito
    public void removeProduct(Long userId, Long productId) {
        var cartId = userRepository.findCartIdByUserId(userId);
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        cart.removeProduct(product);
        cartRepository.save(cart);
    }

}

