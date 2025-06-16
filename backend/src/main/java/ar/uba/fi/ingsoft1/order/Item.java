package ar.uba.fi.ingsoft1.order;

import ar.uba.fi.ingsoft1.product.Product;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

@Embeddable
public record Item(
    @ManyToOne
    Product product,
    Integer amount
) {

}
