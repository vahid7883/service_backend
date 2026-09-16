package com.numjew.service_backend.shopping_basket.domain;

import com.numjew.service_backend.shopping_basket.exception.ShoppingBasketItemNotFoundException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Entity
@Builder
@Table(name = "shopping_baskets")
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingBasket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "shoppingBasket",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private Set<ShoppingBasketItem> items = new LinkedHashSet<>();


    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.forEach(ShoppingBasketItem::detachFromBasket);
        items.clear();
    }

    public ShoppingBasketItem addItem(Long productVariantId) {
        var item = getItem(productVariantId);

        if (item != null) {
            item.increaseQuantity();
        } else {
            item = ShoppingBasketItem.create(productVariantId, this);
            items.add(item);
        }

        return item;
    }

    public void removeItem(Long productVariantId) {
        var item = getItem(productVariantId);

        if (item == null) {
            throw new ShoppingBasketItemNotFoundException();
        }

        items.remove(item);
        item.detachFromBasket();
    }



    public ShoppingBasketItem getItem(Long productVariantId) {
        return items.stream()
                .filter(item -> item.getProductVariantId().equals(productVariantId))
                .findFirst()
                .orElse(null);

    }

}




