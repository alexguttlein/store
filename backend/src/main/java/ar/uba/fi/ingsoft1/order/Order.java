package ar.uba.fi.ingsoft1.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.ingsoft1.product.Product;
import ar.uba.fi.ingsoft1.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// import ar.uba.fi.ingsoft1.order.OrderState;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User orderOwner;

    
    
    @ElementCollection
    private List<Item> items;
    // private HashMap<Product, Long> items;


    @Enumerated(EnumType.STRING)
    @Column(name="order_state")
    private OrderState state;

    @Column(nullable = false) 
    private LocalDateTime orderDate;

    public Order(User owner){
        this.orderOwner = owner;
        this.items = new ArrayList<>();
        this.state = OrderState.CONFIRMED;
        this.orderDate = LocalDateTime.now();
    }

    public Order(User owner, List<Item> products){
        this.orderOwner = owner;
        this.items = products;
        this.state = OrderState.CONFIRMED;
        this.orderDate = LocalDateTime.now();
    }

    public void addProduct(Product product, int quantity){
        // TODO: check que no este guardado ya
        Item newItem = new Item(product, quantity);
        this.items.add(newItem);    
    }

    public void addProduct(Item item) {
        this.items.add(item);
    }

}
