package com.numjew.service_backend.order.repository;

import com.numjew.service_backend.order.domain.Order;
import com.numjew.service_backend.shopping_basket.domain.ShoppingBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserId(Long userId);
}
