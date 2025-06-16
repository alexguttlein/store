package ar.uba.fi.ingsoft1.alternative;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.uba.fi.ingsoft1.product.ProductService;
import ar.uba.fi.ingsoft1.product.Product;
import ar.uba.fi.ingsoft1.product.ProductCreateDTO;


import ar.uba.fi.ingsoft1.attribute.AttributeService;
import ar.uba.fi.ingsoft1.attribute.Attribute;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


import java.util.Map;


@Service
@Transactional
@RequiredArgsConstructor
public class AlternativeService {
    @Autowired
    private AlternativeRepository alternativeRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private AttributeService attributeService;

    public AlternativeDTO createAlternative(AlternativeDTO data){
        Alternative alternative = data.asAlternative();
        return new AlternativeDTO(alternativeRepository.save(alternative));
    }

    public void createAlternativeWithVariants(String alternativeName,String alternativePhoto , List<Map<String, String>> variants) {
        Alternative alternative = new Alternative();
        alternative.setAlternativeName(alternativeName);
        alternative.setAlternativePhoto(alternativePhoto);
        alternative = alternativeRepository.save(alternative);
        Long alternativeId = alternative.getId();

        
        if (variants.isEmpty()) {
            Set<Long> attributes = new HashSet<>();
            productService.createProduct(new ProductCreateDTO(alternativeName, alternativePhoto, 1, attributes, alternativeId));
            return;
        }

        for (Map<String, String> variantMap : variants) {
            Set<Long> attributes = new HashSet<>();

            for (Map.Entry<String, String> entry : variantMap.entrySet()) {
                String attributeName = entry.getKey();  // "Color", "Tamaño"
                String attributeValue = entry.getValue();  // "Rojo", "Grande"
                
                // First, try to find the attribute by both name and value
                Long attributeId = attributeService.findIdsByAttributeNameAndAttributeValue(attributeName, attributeValue)
                        .stream()
                        .findFirst() // We expect a single result or none
                        .orElseGet(() -> {
                            // If no attribute is found, create a new one
                            return attributeService.createAttributeFromNameAndValue(attributeName, attributeValue).id();
                        });

                attributes.add(attributeId); // Add the attribute to the list
            }
        
            productService.createProduct(new ProductCreateDTO(alternativeName, alternativePhoto,1, attributes, alternativeId));
        }
    }

    // Método para obtener todas las Alternatives con paginacion
    public Page<AlternativeDTO> getAlternatives(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        return alternativeRepository.findAll(pageable).map(AlternativeDTO::new);
    }

    // Método para obtener todas las Alternatives
    public List<AlternativeDTO> getAllAlternatives() {

        return alternativeRepository.findAll().stream().map(AlternativeDTO::new).toList();
    }

    // Método para obtener una Alternative por ID
    public AlternativeDTO getAlternativeById(Long id) {
        Alternative alternative = alternativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alternative not found"));
        return new AlternativeDTO(alternative);
    }

    //eliminar alternativa por id
    public void deleteAlternative(Long id) {
        if (!alternativeRepository.existsById(id)) {
            throw new RuntimeException("Alternative not found with id: " + id);
        }
        alternativeRepository.deleteById(id);
    }

    public List<AlternativeDTO> findAlternativesByName(String alternativeName) {
        List<Alternative> alternatives = alternativeRepository.findByAlternativeName(alternativeName);
        List<AlternativeDTO> alternativeDTOs = new ArrayList<>();

        for (Alternative alternative : alternatives) {
            alternativeDTOs.add(new AlternativeDTO(alternative));
        }

        return alternativeDTOs;
    }


}

