package ar.uba.fi.ingsoft1.alternative;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlternativeRepository extends JpaRepository<Alternative, Long> {
    List<Alternative> findByAlternativeName(String alternativeName);
}

