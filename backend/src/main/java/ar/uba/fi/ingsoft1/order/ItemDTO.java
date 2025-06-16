package ar.uba.fi.ingsoft1.order;

import ar.uba.fi.ingsoft1.product.Product;
import ar.uba.fi.ingsoft1.product.ProductDTO;


public record ItemDTO (
    ProductDTO product,
    Integer amount
) {
    public ItemDTO(Item item){
        this(new ProductDTO(item.product()), item.amount());
    }

    public ItemDTO(Product product, int amount){
        this(new Item(product, amount));
    }
}
