package alternativeTest;

import ar.uba.fi.ingsoft1.alternative.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class AlternativeServiceTest {
    @Mock
    private AlternativeRepository alternativeRepository;

    @InjectMocks
    private AlternativeService alternativeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAlternative_shouldSaveAndReturnAlternative() {
        AlternativeDTO dto = new AlternativeDTO(null, "Test Alternative", "Test Photo");
        Alternative alternative = new Alternative("Test Alternative", "Test Photo");

        when(alternativeRepository.save(any(Alternative.class))).thenReturn(alternative);

        AlternativeDTO savedDto = alternativeService.createAlternative(dto);

        assertNotNull(savedDto);
        assertEquals(dto.alternativeName(), savedDto.alternativeName());
        verify(alternativeRepository, times(1)).save(any(Alternative.class));
    }

    @Test
    void getAlternatives_shouldReturnAllAlternatives() {
        Alternative alternative1 = new Alternative(1L, "Alternative 1","Alternative 1");
        Alternative alternative2 = new Alternative(2L, "Alternative 1","Alternative 2");

        when(alternativeRepository.findAll()).thenReturn(List.of(alternative1, alternative2));

        List<AlternativeDTO> alternatives = alternativeService.getAllAlternatives();

        assertEquals(2, alternatives.size());
        verify(alternativeRepository, times(1)).findAll();
    }

    @Test
    void getAlternativeById_shouldReturnAlternative() {
        Long id = 1L;
        Alternative alternative = new Alternative(id, "Test Alternative","Test Alternative");

        when(alternativeRepository.findById(id)).thenReturn(Optional.of(alternative));

        AlternativeDTO dto = alternativeService.getAlternativeById(id);

        assertNotNull(dto);
        assertEquals("Test Alternative", dto.alternativeName());
        verify(alternativeRepository, times(1)).findById(id);
    }

    @Test
    void deleteAlternative_shouldDeleteAlternativeIfExists() {
        Long id = 1L;

        when(alternativeRepository.existsById(id)).thenReturn(true);

        alternativeService.deleteAlternative(id);

        verify(alternativeRepository, times(1)).deleteById(id);
    }

    @Test
    void findAlternativesByName_shouldReturnMatchingAlternatives() {
        String alternativeName = "Alternative Name";
        Alternative alternative1 = new Alternative(1L, alternativeName, "Test Alternative");
        Alternative alternative2 = new Alternative(2L, alternativeName, "Test Alternative");

        when(alternativeRepository.findByAlternativeName(alternativeName)).thenReturn(List.of(alternative1, alternative2));

        List<AlternativeDTO> alternatives = alternativeService.findAlternativesByName(alternativeName);

        assertEquals(2, alternatives.size());
        verify(alternativeRepository, times(1)).findByAlternativeName(alternativeName);
    }

}

