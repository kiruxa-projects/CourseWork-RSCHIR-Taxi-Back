package com.repositories;

import com.models.Order;
import com.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByClientId(long clientId);

    List<Order> findOrderByClientIdAndStatus(long clientId, String status);
    List<Order> findOrderByDriverIdAndStatus(long driverId, String status);
}

