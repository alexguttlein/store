package alternativeTest;

import ar.uba.fi.ingsoft1.alternative.*;
import ar.uba.fi.ingsoft1.product.ProductDTO;
import ar.uba.fi.ingsoft1.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import static org.mockito.Mockito.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.*;

class AlternativeControllerTest {

    @Mock
    private AlternativeService alternativeService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private AlternativeController alternativeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAlternative() {

        AlternativeDTO alternativeDTO = new AlternativeDTO(1L, "Test Alternative","Test Alternative");
        when(alternativeService.createAlternative(any(AlternativeDTO.class))).thenReturn(alternativeDTO);

        ResponseEntity<Object> response = alternativeController.createAlternative(alternativeDTO);

        assertEquals(200, response.getStatusCodeValue()); // Verifica que el código de estado sea 200 OK
        assertEquals(alternativeDTO, response.getBody()); // Verifica que el cuerpo de la respuesta sea el DTO creado
        verify(alternativeService, times(1)).createAlternative(any(AlternativeDTO.class)); // Verifica que se llame al servicio
    }

    @Test
    void testGetAlternatives() {

        AlternativeDTO alternativeDTO = new AlternativeDTO(1L,"Test Alternative", "Test Alternative");
        List<AlternativeDTO> alternatives = List.of(alternativeDTO);
        Pageable pageable = PageRequest.of(0, 10);
        when(alternativeService.getAlternatives(0, 10)).thenReturn(new PageImpl<>(alternatives, pageable, alternatives.size()));

        ResponseEntity<Page<AlternativeDTO>> response = alternativeController.getAlternatives(0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("Test Alternative", response.getBody().getContent().get(0).alternativeName());
    }

    @Test
    void testGetAlternativeById() {
        Long id = 1L;
        AlternativeDTO alternativeDTO = new AlternativeDTO(id, "Test Alternative","Test Alternative");
        when(alternativeService.getAlternativeById(id)).thenReturn(alternativeDTO);

        ResponseEntity<AlternativeDTO> response = alternativeController.getAlternativeById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(alternativeDTO, response.getBody());
    }

    @Test
    void testDeleteAlternative() {

        Long id = 1L;
        doNothing().when(alternativeService).deleteAlternative(id);

        ResponseEntity<Void> response = alternativeController.deleteAlternative(id);

        assertEquals(204, response.getStatusCodeValue()); // 204 no content
        verify(alternativeService, times(1)).deleteAlternative(id); // Verifica que se haya llamado a delete
    }

    @Test
    void testGetProductsByAlternativeId() {

        Long id = 1L;
        ProductDTO productDTO = new ProductDTO(1L, "Product 1", "Test Alternative",1,null,null);
        when(productService.getProductsByAlternativeId(id)).thenReturn(List.of(productDTO));

        ResponseEntity<List<ProductDTO>> response = alternativeController.getProductsByAlternativeId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Product 1", response.getBody().get(0).productName());
    }
}