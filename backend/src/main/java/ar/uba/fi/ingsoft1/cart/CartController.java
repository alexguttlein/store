package ar.uba.fi.ingsoft1.cart;

import ar.uba.fi.ingsoft1.user.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.uba.fi.ingsoft1.authorization.AuthorizationContext;
import ar.uba.fi.ingsoft1.authorization.RequiresPrivilege;
import ar.uba.fi.ingsoft1.exception.CartEmptyException;
import ar.uba.fi.ingsoft1.exception.EntityNotFoundException;
import ar.uba.fi.ingsoft1.exception.InvalidCartContentsException;
import ar.uba.fi.ingsoft1.exception.NotEnoughStockException;
import ar.uba.fi.ingsoft1.exception.RuleViolationException;
import ar.uba.fi.ingsoft1.order.OrderDTO;
import ar.uba.fi.ingsoft1.order.OrderService;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CartController {
    @Autowired
    private final CartService cartService;

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final EmailService emailService;

    @Operation(summary = "obtain the cart of the current logged in user") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "the user's cart"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
    })
    @GetMapping("/cart")
    @RequiresPrivilege("user")
    public ResponseEntity<CartDTO> getCart() {
         Claims claims = AuthorizationContext.getClaims();
         Long userId = claims.get("userId", Long.class);
         CartDTO cartDTO = cartService.getCart(userId); 
         return ResponseEntity.ok(cartDTO); 
    }

    @Operation(summary = "add a product to the logged in user") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "product added successfully"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
    })
    @PostMapping("/cart_products/{productId}") 
    @RequiresPrivilege("user")
    public ResponseEntity<Void> addProduct(@PathVariable Long productId) { 
        Claims claims = AuthorizationContext.getClaims();
        Long userId = claims.get("userId", Long.class);

        cartService.addProduct(userId, productId); 
        return ResponseEntity.ok().build(); 
    } 

    @Operation(summary = "remove a product from the logged in user's cart") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "product successfully removed from cart"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
    })
    @DeleteMapping("/cart_products/{productId}")
    @RequiresPrivilege("user")
    public ResponseEntity<Void> removeProduct(@PathVariable Long productId) {
        Claims claims = AuthorizationContext.getClaims(); 
        Long userId = claims.get("userId", Long.class);

        cartService.removeProduct(userId, productId); 
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "checkout the contents of the cart to create a new order") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "order successfully created for the cart's contents"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
    })
    @PostMapping("/checkout")
    @RequiresPrivilege("user")
    public ResponseEntity<?> checkout(){
        try {
            Claims claims = AuthorizationContext.getClaims();
            Long userId = claims.get("userId", Long.class);

            OrderDTO orderdto = orderService.createOrder(userId);

            
            emailService.sendCheckOutEmail("delivered@resend.dev", orderdto);

            return new ResponseEntity<>(orderdto, HttpStatus.OK);
        } 
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (CartEmptyException e) {
            return new ResponseEntity<>("Your cart is empty", HttpStatus.NOT_FOUND);
        }
        catch (NotEnoughStockException e) {
            return new ResponseEntity<>("Not enough stock to buy " + e.getProductName(), HttpStatus.CONFLICT);
        }
        catch (InvalidCartContentsException e){
            return new ResponseEntity<>("Please check your cart again.", HttpStatus.BAD_REQUEST);
        }
        catch (RuleViolationException e) {
            return new ResponseEntity<>(
                "Can't create an order for your cart for the following reason: " + e.getMessage(),
                 HttpStatus.CONFLICT);
        }
    }
}