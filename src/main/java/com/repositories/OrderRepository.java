package com.repositories;

import com.models.Order;
import com.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByClientId(long clientId);
    Order findOrderById(Long orderId);

    List<Order> findOrderByClientIdAndStatus(long clientId, String status);
    List<Order> findOrderByDriverIdAndStatus(long driverId, String status);
    @Query("SELECT MAX(id) FROM Order")
    Long getMaxId();
}

