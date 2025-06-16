package ar.uba.fi.ingsoft1.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import ar.uba.fi.ingsoft1.authorization.AuthorizationContext;
import ar.uba.fi.ingsoft1.authorization.AuthorizationService;
import ar.uba.fi.ingsoft1.authorization.RequiresPrivilege;
import ar.uba.fi.ingsoft1.dynamicRules.RuleLoaderService;
import ar.uba.fi.ingsoft1.exception.*;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private RuleLoaderService ruleService;

    @Operation(summary = "Get orders by user ID (or all orders if admin)") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "List of orders") 
    })
    @GetMapping
    @RequiresPrivilege("user")
    public ResponseEntity<Page<OrderDTO>> getOrdersForUser(
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "9") int pageSize) {
            Claims claims = AuthorizationContext.getClaims();
            Long userId = claims.get("userId", Long.class);

            if (authorizationService.hasPrivilege(claims, "admin")){
                return ResponseEntity.ok(orderService.getOrders(pageNo, pageSize));
            }
            
            return ResponseEntity.ok(orderService.getOrdersForUser(userId, pageNo, pageSize));
        }

    @Operation(summary = "Get all confirmed orders by user ID (or all confirmed if admin)", 
    responses = { 
        @ApiResponse(responseCode = "200", description = "List of confirmed orders"), 
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403")
    })    
    @GetMapping("/confirmed")
    @RequiresPrivilege("user")
    public ResponseEntity<Page<OrderDTO>> getConfirmedOrdersForUser(
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "9") int pageSize) {
            Claims claims = AuthorizationContext.getClaims();
            Long userId = claims.get("userId", Long.class);

            if (authorizationService.hasPrivilege(claims, "admin")){
                return ResponseEntity.ok(orderService.getConfirmedOrders(pageNo, pageSize));
            }
            
            return ResponseEntity.ok(orderService.getAllConfirmedOrdersForUser(userId, pageNo, pageSize));
        }
    
    @Operation(summary = "Get processed orders by user ID (or all processed orders if admin)") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "List of processed orders"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403")
    })
    @GetMapping("/processed")
    @RequiresPrivilege("user")
    public ResponseEntity<Page<OrderDTO>> getProcessedOrdersForUser(
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "9") int pageSize) {
            Claims claims = AuthorizationContext.getClaims();
            Long userId = claims.get("userId", Long.class);

            if (authorizationService.hasPrivilege(claims, "admin")){
                return ResponseEntity.ok(orderService.getProcessedOrders(pageNo, pageSize));
            }
            
            return ResponseEntity.ok(orderService.getAllProcessedOrdersForUser(userId, pageNo, pageSize));
        }

    @Operation(summary = "Get sent orders by user ID (or all sent orders if admin)") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "List of sent orders"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403")
    })
    @GetMapping("/sent")
    @RequiresPrivilege("user")
    public ResponseEntity<Page<OrderDTO>> getSentOrdersForUser(
        @RequestParam(defaultValue = "0") int pageNo,
        @RequestParam(defaultValue = "9") int pageSize) {
            Claims claims = AuthorizationContext.getClaims();
            Long userId = claims.get("userId", Long.class);

            if (authorizationService.hasPrivilege(claims, "admin")){
                return ResponseEntity.ok(orderService.getSentOrders(pageNo, pageSize));
            }
            
            return ResponseEntity.ok(orderService.getAllSentOrdersForUser(userId, pageNo, pageSize));
        }

    @Operation(summary = "Get a single orders by order ID") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "order"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
        @ApiResponse(responseCode = "404", description = "the order does not exist")
    })
    @GetMapping("/{orderId}")
    @RequiresPrivilege("user")
    public ResponseEntity<OrderDTO> getOrder(
        @PathVariable Long orderId) {
        try {
            Claims claims = AuthorizationContext.getClaims();
            Long userId = claims.get("userId", Long.class);
            
            var order = orderService.getSingleOrder(orderId);
            
            if (!order.idOwner().equals(userId)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        
            return new ResponseEntity<>(order, HttpStatus.OK); 
        
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
    }
     
    @Operation(summary = "check if an order can be canceled, by order ID") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "true if order can be cancelled, false otherwise"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
        @ApiResponse(responseCode = "404", description = "order does not exist")
    })
    @GetMapping("/{orderId}/cancel")
    @RequiresPrivilege("user")
    public ResponseEntity<Boolean> canOrderBeCanceled(
                @PathVariable Long orderId) {
        try {
            Claims claims = AuthorizationContext.getClaims();
            Long userId = claims.get("userId", Long.class);
            var order = orderService.getSingleOrder(orderId);

            if (!order.idOwner().equals(userId)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            var res = orderService.canCancelOrder(orderId);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    } 

    @Operation(summary = "cancel an order by its order id") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "the order got cancelled"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
        @ApiResponse(responseCode = "404", description = "order does not exist"),
        @ApiResponse(responseCode = "409", description = "order no longer within cancel window")
    })
    @PostMapping("/{orderId}/cancel")
    @RequiresPrivilege("user")
    public ResponseEntity<?> cancelOrder(
                        @PathVariable Long orderId) {

        Claims claims = AuthorizationContext.getClaims();
        Long userId = claims.get("userId", Long.class);
        OrderDTO order;
        try {
            order = orderService.getSingleOrder(orderId);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        if (!order.idOwner().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        var result = orderService.cancelOrder(orderId);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Marks an order as \"SENT\"") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "order successfully updated"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
        @ApiResponse(responseCode = "404", description = "order does not exist"),
        @ApiResponse(responseCode = "409", description = "order does not qualify to be updated as \"SENT\"")
    })
    @PostMapping("/{orderId}/dispatch")
    @RequiresPrivilege("admin")
    public ResponseEntity<?> markOrderAsSent(
                            @PathVariable Long orderId){
        boolean res;
        try {
            res = orderService.markOrderAsSent(orderId);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        if (res) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

    }

    @Operation(summary = "Marks an order as \"PROCESS\"") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "order successfully updated"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
        @ApiResponse(responseCode = "404", description = "order does not exist"),
        @ApiResponse(responseCode = "409", description = "order does not qualify to be updated as \"PROCESS\"")
    })
    @PostMapping("/{orderId}/process")
    @RequiresPrivilege("admin")
    public ResponseEntity<?> markOrderAsProcessed(@PathVariable Long orderId){
        boolean res;
        try {
            res = orderService.markOrderAsProcessed(orderId);
        }
        catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        if (res) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "uploads a new rule to the rule engine executor") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "rule successfully added"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
        @ApiResponse(responseCode = "400", description = "rule in invalid format or malformed")
    })
    @PostMapping("/rules")
    @RequiresPrivilege("admin")
    public ResponseEntity<?> addNewRuleToEngine(@RequestBody String rulejson) {

        try {
            ruleService.loadRulesFromJSON(rulejson);
            
        } catch (JsonProcessingException | InvalidRuleException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }


}
