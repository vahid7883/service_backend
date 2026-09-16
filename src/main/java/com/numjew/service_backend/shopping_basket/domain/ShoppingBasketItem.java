package com.numjew.service_backend.shopping_basket.domain;

import com.numjew.service_backend.shopping_basket.exception.InvalidQuantityException;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "shopping_basket_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_basket_variant",
                        columnNames = {"basket_id", "product_variant_id"}
                )
        }
)
public class ShoppingBasketItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id", nullable = false)
    private ShoppingBasket shoppingBasket;


    @Column(name = "product_variant_id", nullable = false)
    private Long productVariantId;


    @Column(nullable = false)
    private Integer quantity;

    public static ShoppingBasketItem create(
            Long productVariantId,
            ShoppingBasket shoppingBasket
    ) {
        return ShoppingBasketItem.builder()
                .productVariantId(productVariantId)
                .quantity(1)
                .shoppingBasket(shoppingBasket)
                .build();
    }

    public void detachFromBasket() {
        this.shoppingBasket = null;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void updateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException();
        }

        this.quantity = quantity;
    }

    public void decreaseQuantity() {
        if (quantity <= 1) {
            throw new InvalidQuantityException();
        }

        quantity--;
    }

}
