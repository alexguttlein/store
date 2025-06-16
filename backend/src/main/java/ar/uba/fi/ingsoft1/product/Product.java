package ar.uba.fi.ingsoft1.product;

import java.util.Set;

import ar.uba.fi.ingsoft1.alternative.Alternative;
import ar.uba.fi.ingsoft1.attribute.Attribute;
import ar.uba.fi.ingsoft1.exception.NotEnoughStockException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    private String productName;
    private String photo;
    private Integer stock;


    @ManyToMany
    @JoinTable(
            name = "product_attribute",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private Set<Attribute> attributes = null;

    @ManyToOne
    @JoinColumn(name = "alternative_id")
    private Alternative alternative;

    public Product(String productName, String photo, Integer stock, Set<Attribute> attributes, Alternative alternative) {
        this.productName = productName;
        this.photo = photo;
        this.stock = stock;
        this.attributes = attributes;
        this.alternative = alternative;
    }
    
    public void addStock(int amount) throws IllegalArgumentException {
        if (amount < 0) {
            throw new IllegalArgumentException("can only add positive amounts to stock");
        }

        this.stock += amount;
    }

    public void removeStock(int amount) throws IllegalArgumentException, NotEnoughStockException {
        if (amount < 0) {
            throw new IllegalArgumentException("amount of stock to remove must be a positive number");
        }
        if (amount > this.stock) {
            throw new NotEnoughStockException("not enough product in stock to remove this amount",
                                             this.productName, this.stock);
        }

        this.stock -= amount;
    }
}
