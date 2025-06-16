package attributeTest;

import ar.uba.fi.ingsoft1.attribute.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AttributeServiceTest {
    @Mock
    private AttributeRepository attributeRepository;

    @InjectMocks
    private AttributeService attributeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAttribute() {
        AttributeDTO attributeDTO = new AttributeDTO(null, "Color", "Blue");
        Attribute attribute = new Attribute(1L, "Color", "Blue");

        when(attributeRepository.save(any(Attribute.class))).thenReturn(attribute);

        AttributeDTO createdAttribute = attributeService.createAttribute(attributeDTO);

        assertNotNull(createdAttribute);
        assertEquals(1L, createdAttribute.id());
        assertEquals("Color", createdAttribute.attributeName());
        assertEquals("Blue", createdAttribute.value());

        verify(attributeRepository, times(1)).save(any(Attribute.class));
    }

    @Test
    public void testGetAttributes() {
        List<Attribute> attributes = List.of(
                new Attribute(1L, "Color", "Red"),
                new Attribute(2L, "Size", "Medium")
        );

        when(attributeRepository.findAll()).thenReturn(attributes);

        List<AttributeDTO> result = attributeService.getAttributes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Color", result.get(0).attributeName());
        assertEquals("Red", result.get(0).value());
        assertEquals("Size", result.get(1).attributeName());
        assertEquals("Medium", result.get(1).value());

        verify(attributeRepository, times(1)).findAll();
    }

}
