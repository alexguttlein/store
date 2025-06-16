package ar.uba.fi.ingsoft1.product;

import ar.uba.fi.ingsoft1.alternative.AlternativeDTO;
import ar.uba.fi.ingsoft1.attribute.AttributeDTO;

import java.util.List;

public record ProductDTO(
        Long id,
        String productName,
        String photo,
        Integer stock,
        List<AttributeDTO> attributes,
        AlternativeDTO alternative
) {
    public ProductDTO(Product product) {
        this(
                product.getId(),
                product.getProductName(),
                product.getPhoto(),
                product.getStock(),

                product.getAttributes() == null ? List.of() : product.getAttributes().stream()
                        .map(AttributeDTO::new)
                        .toList(),

                product.getAlternative() != null ? new AlternativeDTO(product.getAlternative()) : null
        );
    }
}
