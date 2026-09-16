package com.numjew.service_backend.shopping_basket.repository;

import com.numjew.service_backend.shopping_basket.domain.ShoppingBasket;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketDto;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketItemDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingBasketRepository extends JpaRepository<ShoppingBasket, UUID> {

    Optional<ShoppingBasket> findByIdAndUserId(
            UUID basketId,
            Long userId
    );

    @EntityGraph(attributePaths = "items")
    Optional<ShoppingBasket> findWithItemsByIdAndUserId(
            UUID basketId,
            Long userId
    );
}
