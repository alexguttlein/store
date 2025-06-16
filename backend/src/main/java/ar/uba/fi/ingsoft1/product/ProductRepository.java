package ar.uba.fi.ingsoft1.product;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByAlternativeId(Long alternativeId);
    List<Product> findByStockGreaterThan(Integer stock);
}
