package ar.uba.fi.ingsoft1.cart;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import ar.uba.fi.ingsoft1.order.Item;
import ar.uba.fi.ingsoft1.product.Product;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "cart")
public class Cart implements Iterable<Item> {
    @Id
    @GeneratedValue
    private Long id;

    // @ManyToMany
    // @JoinTable(
    //     name = "cart_products",
    //     joinColumns = @JoinColumn(name = "cart_id"),
    //     inverseJoinColumns = @JoinColumn(name = "product_id")
    // )
    // private List<Product> products;

    @ElementCollection 
    @CollectionTable(name = "cart_products", 
                     joinColumns = @JoinColumn(name = "cart_id")) 
    @MapKeyJoinColumn(name = "product_id")
    private Map<Product, Integer> products;
    
    // @OneToOne(mappedBy = "cart")
    // private User user;


    public Cart() {
        // this.products = new ArrayList<>();
        this.products = new HashMap<>();
    }

    // public Cart(List<Product> products){
    //     this.products = products;
    // }

    public boolean isEmpty(){
        return this.products.isEmpty();
    }

    public void clearCart(){
        this.products.clear();
    }

    private void setProductAmount(Product product, int amount) {
        this.products.merge(product, amount, Integer::sum);
        this.products.computeIfPresent(product, 
            (key, quantity) -> {
                if (quantity > key.getStock()) {
                    return key.getStock();
                } else {
                    return quantity; 
                }
            });
    }

    public void addProduct(Product product) {
        this.setProductAmount(product, 1);
    }

    public void addProductInBulk(Product product, int amount) throws IllegalArgumentException {
        if (amount < 1) {
            throw new IllegalArgumentException("amount must be positive");
        }
        this.setProductAmount(product, amount);
    }

    public void removeProduct(Product product) {
        this.products.computeIfPresent(product, 
            (key, quantity) -> {
                if (quantity > 1) {
                    return quantity - 1;
                } else {
                    return null; // remueve el producto
                }
            });
    }

    @Override
    public Iterator<Item> iterator() {

        class ProductIterator implements Iterator<Item> {
            private final Iterator<Map.Entry<Product, Integer>> iterator;

            public ProductIterator(Iterator<Map.Entry<Product, Integer>> items){
                this.iterator = items;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Item next(){
                if (!hasNext()){
                    throw new NoSuchElementException();
                }
                Map.Entry<Product, Integer> entry = iterator.next(); 
                return new Item(entry.getKey(), entry.getValue());
            }
        }

        return new ProductIterator(this.products.entrySet().iterator());
    }

    // public List<Product> getProductList() {
    //     List<Product> productList = new ArrayList<>();

    //     for (Map.Entry<Product, Integer> entry : products.entrySet()) {
    //         Product product = entry.getKey();
    //         Integer quantity = entry.getValue();

    //         for (int i = 0; i < quantity; i++) {
    //             productList.add(product);
    //         }
    //     }
    //     return productList;
    // }

    // public void setProducts(List<Product> productList) {
    //     // Limpiamos el mapa actual
    //     this.products.clear();

    //     // Recorremos la lista de productos y actualizamos el mapa
    //     for (Product product : productList) {
    //         this.products.merge(product, 1, Integer::sum);
    //     }
    // }
}