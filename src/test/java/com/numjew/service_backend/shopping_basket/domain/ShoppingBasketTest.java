package com.numjew.service_backend.shopping_basket.domain;

import com.numjew.service_backend.shopping_basket.exception.InvalidQuantityException;
import com.numjew.service_backend.shopping_basket.exception.ShoppingBasketItemNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingBasketTest {

    @Test
    void shouldAddItemToEmptyBasket() {

        // Arrange
        var basket = ShoppingBasket.builder()
                .userId(1L)
                .build();

        // Act
        basket.addItem(10L);

        // Assert
        assertEquals(1, basket.getItems().size());
        assertEquals(10L, basket.getItem(10L).getProductVariantId());
        assertEquals(1, basket.getItem(10L).getQuantity());
    }

    @Test
    void shouldIncreaseQuantityWhenAddingSameItem() {

        // Arrange
        var basket = ShoppingBasket.builder()
                .userId(1L)
                .build();

        // Act
        basket.addItem(10L);
        basket.addItem(10L);

        // Assert
        assertEquals(1, basket.getItems().size());
        assertEquals(2, basket.getItem(10L).getQuantity());
    }
    @Test
    void shouldRemoveItemFromBasket() {

        // Arrange
        var basket = ShoppingBasket.builder()
                .userId(1L)
                .build();

        basket.addItem(10L);

        // Act
        basket.removeItem(10L);

        // Assert
        assertTrue(basket.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonexistentItem() {

        // Arrange
        var basket = ShoppingBasket.builder()
                .userId(1L)
                .build();

        // Act & Assert
        assertThrows(
                ShoppingBasketItemNotFoundException.class,
                () -> basket.removeItem(999L)
        );
    }

    @Test
    void shouldClearAllItemsFromBasket() {

        // Arrange
        var basket = ShoppingBasket.builder()
                .userId(1L)
                .build();

        basket.addItem(10L);
        basket.addItem(20L);
        basket.addItem(30L);

        // Act
        basket.clear();

        // Assert
        assertTrue(basket.isEmpty());
        assertEquals(0, basket.getItems().size());
    }

    @Test
    void shouldUpdateItemQuantity() {

        // Arrange
        var basket = ShoppingBasket.builder()
                .userId(1L)
                .build();

        var item = basket.addItem(10L);

        // Act
        item.updateQuantity(5);

        // Assert
        assertEquals(5, item.getQuantity());
    }

    @Test
    void shouldRejectZeroQuantity() {

        // Arrange
        var basket = ShoppingBasket.builder()
                .userId(1L)
                .build();

        var item = basket.addItem(10L);

        // Act & Assert
        assertThrows(
                InvalidQuantityException.class,
                () -> item.updateQuantity(0)
        );
    }

    @Test
    void shouldRejectNegativeQuantity() {

        // Arrange
        var basket = ShoppingBasket.builder()
                .userId(1L)
                .build();

        var item = basket.addItem(10L);

        // Act & Assert
        assertThrows(
                InvalidQuantityException.class,
                () -> item.updateQuantity(-1)
        );
    }

    @Test
    void shouldRejectNullQuantity() {

        // Arrange
        var basket = ShoppingBasket.builder()
                .userId(1L)
                .build();

        var item = basket.addItem(10L);

        // Act & Assert
        assertThrows(
                InvalidQuantityException.class,
                () -> item.updateQuantity(null)
        );
    }

}