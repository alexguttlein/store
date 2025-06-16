package ar.uba.fi.ingsoft1.attribute;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AttributeService {
    @Autowired
    private AttributeRepository attributeRepository;

    public AttributeDTO createAttribute(AttributeDTO data){
        Attribute attribute = data.asAttribute();
        return new AttributeDTO(attributeRepository.save(attribute));
    }

    public AttributeDTO createAttributeFromNameAndValue(String attributeName, String attributeValue){
        Attribute newAttribute = new Attribute();
        newAttribute.setAttributeName(attributeName);
        newAttribute.setValue(attributeValue);
        return new AttributeDTO(attributeRepository.save(newAttribute));
    }

    // Método para obtener todas las Attributes
    public List<AttributeDTO> getAttributes() {
        return attributeRepository.findAll().stream()
                .map(AttributeDTO::new)
                .toList();
    }

    // Método para obtener una Attribute por ID
    public AttributeDTO getAttributeById(Long id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attribute not found"));
        return new AttributeDTO(attribute);
    }

    //eliminar atributo por id
    public void deleteAttribute(Long id) {
        if (!attributeRepository.existsById(id)) {
            throw new RuntimeException("Attribute not found with id: " + id);
        }
        attributeRepository.deleteById(id);
    }

    public List<AttributeDTO> findAttributesByName(String attributeName) {
        List<Attribute> attributes = attributeRepository.findByAttributeName(attributeName);
        List<AttributeDTO> attributeDTOs = new ArrayList<>();

        for (Attribute attribute : attributes) {
            attributeDTOs.add(new AttributeDTO(attribute));
        }

        return attributeDTOs;
    }


    public List<Long> findIdsByAttributeNameAndAttributeValue(String attributeName, String attributeValue) {
        List<Attribute> attributes = attributeRepository.findByAttributeNameAndValue(attributeName, attributeValue);
        List<Long> attributeIds = new ArrayList<>();

        for (Attribute attribute : attributes) {
            attributeIds.add(attribute.getId());
        }

        return attributeIds;
    }
}
