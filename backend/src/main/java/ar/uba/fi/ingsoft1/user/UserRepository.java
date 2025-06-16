
package ar.uba.fi.ingsoft1.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    // Stringly-typed generated query, see
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    User findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT u.cart.id FROM User u WHERE u.id = :userId") 
    Long findCartIdByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(u.role) FROM User u WHERE u.role = 'admin'") 
    Long countAdmins();

}
