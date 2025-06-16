package productTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ar.uba.fi.ingsoft1.product.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    public void testCreateProduct() {
        ProductCreateDTO createDTO = new ProductCreateDTO("Test Product", null, null);
        Product savedProduct = new Product(1L, "Test Product", null, null);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDTO result = productService.createProduct(createDTO);

        assertNotNull(result);
        assertEquals("Test Product", result.productName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testGetProducts() {
        Product product1 = new Product(1L, "Product 1", null, null);
        Product product2 = new Product(2L, "Product 2", null, null);
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductDTO> products = productService.getProducts();

        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).productName());
        assertEquals("Product 2", products.get(1).productName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductById() {
        Product product = new Product(1L, "Product 1", null, null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<ProductDTO> result = Optional.ofNullable(productService.getProductById(1L));

        assertTrue(result.isPresent());
        assertEquals("Product 1", result.get().productName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateProduct() {
        ProductCreateDTO updateDTO = new ProductCreateDTO("Updated Product", null, null);
        Product existingProduct = new Product(1L, "Old Product", null,null);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        ProductDTO result = productService.updateProduct(1L, updateDTO);

        assertNotNull(result);
        assertEquals("Updated Product", result.productName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(existingProduct);
    }*/
}
