package ar.uba.fi.ingsoft1.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderOwnerId(Long userId);
    Page<Order> findAllByOrderOwnerId(Long userId, Pageable pageable);

    Page<Order> findAllByState(OrderState orderState, Pageable pageable);


    List<Order> findAllByOrderOwnerIdAndState(Long userId, OrderState orderState);
    Page<Order> findAllByOrderOwnerIdAndState(Long userId, OrderState orderState, Pageable pageable);
}
