package ar.uba.fi.ingsoft1.product;

import java.util.List;
import static java.util.Objects.isNull;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.uba.fi.ingsoft1.alternative.Alternative;
import ar.uba.fi.ingsoft1.alternative.AlternativeRepository;
import ar.uba.fi.ingsoft1.attribute.Attribute;
import ar.uba.fi.ingsoft1.attribute.AttributeRepository;
import ar.uba.fi.ingsoft1.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private AlternativeRepository alternativeRepository;

    //crear nuevo producto
    public ProductDTO createProduct(ProductCreateDTO data) {
        Set<Attribute> attributes = null;
        if (data.attributeIds() != null && !data.attributeIds().isEmpty()) {
            attributes = data.attributeIds().stream()
                    .map(id -> attributeRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException("Attribute not found with id: " + id)))
                    .collect(Collectors.toSet());
        }

        Alternative alternative = null;
        if (data.alternativeId() != null) {
            alternative = alternativeRepository.findById(data.alternativeId())
                    .orElseThrow(() -> new EntityNotFoundException("Variant not found with id: " + data.alternativeId()));
        }

        Product product = new Product(data.productName(), data.photo(),data.stock(), attributes, alternative);
        return new ProductDTO(productRepository.save(product));
    }

    // Método para obtener todos los productos con paginacion
    public Page<ProductDTO> getProducts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return productRepository.findAll(pageable).map(ProductDTO::new);
    }

    //obtener todos los productos
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductDTO::new)
                .toList();
    }

    //obtener producto por id
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return new ProductDTO(product);
    }

    //obtener productos por id de la alternativa
    public List<ProductDTO> getProductsByAlternativeId(Long id) {
        return productRepository.findAllByAlternativeId(id).stream()
                .map(ProductDTO::new)
                .toList();
    }

    //eliminar producto por id
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    //modificar producto por id
    public ProductDTO updateProduct(Long id, ProductCreateDTO data) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        if (!isNull(data.productName())) product.setProductName(data.productName());
        if (!isNull(data.photo())) product.setPhoto(data.photo());
        if (!isNull(data.stock())) product.setStock(data.stock());

        // Actualizar attributees si se proporcionan IDs
        if (data.attributeIds() != null) {
            Set<Attribute> attributes = data.attributeIds().stream()
                    .map(attributeId -> attributeRepository.findById(attributeId)
                            .orElseThrow(() -> new EntityNotFoundException("Attribute not found with id: " + attributeId)))
                    .collect(Collectors.toSet());
            product.setAttributes(attributes);
        }

        // Actualizar la alternativa si se proporciona un ID
        if (data.alternativeId() != null) {
            Alternative alternative = alternativeRepository.findById(data.alternativeId())
                    .orElseThrow(() -> new EntityNotFoundException("Variant not found with id: " + data.alternativeId()));
            product.setAlternative(alternative);
        }

        return new ProductDTO(productRepository.save(product));
    }

    //recupera los productos cuyo stock sea mayor a cero
    public List<ProductDTO> getProductsInStock() {
        return productRepository.findByStockGreaterThan(0).stream()
                .map(ProductDTO::new)
                .toList();
    }
}
