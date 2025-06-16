package ar.uba.fi.ingsoft1.attribute;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    List<Attribute> findByAttributeName(String attributeName);
    List<Attribute> findByAttributeNameAndValue(String attributeName, String value);

}
