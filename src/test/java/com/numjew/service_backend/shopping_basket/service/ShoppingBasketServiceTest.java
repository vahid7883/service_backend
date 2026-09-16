package com.numjew.service_backend.shopping_basket.service;

import com.numjew.service_backend.product.exception.ProductVariantNotFoundException;
import com.numjew.service_backend.product.repository.ProductVariantRepository;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketItemDto;
import com.numjew.service_backend.shopping_basket.exception.ShoppingBasketItemNotFoundException;
import com.numjew.service_backend.shopping_basket.exception.ShoppingBasketNotFoundException;
import com.numjew.service_backend.shopping_basket.mapper.ShoppingBasketItemMapper;
import com.numjew.service_backend.shopping_basket.mapper.ShoppingBasketMapper;
import com.numjew.service_backend.shopping_basket.repository.ShoppingBasketRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.numjew.service_backend.product.domain.ProductVariant;
import com.numjew.service_backend.shopping_basket.domain.ShoppingBasket;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketDto;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingBasketServiceTest {

    @Mock
    private ShoppingBasketRepository shoppingBasketRepository;

    @Mock
    private ShoppingBasketMapper shoppingBasketMapper;

    @Mock
    private ShoppingBasketItemMapper shoppingBasketItemMapper;

    @Mock
    private ProductVariantRepository productVariantRepository;

    @InjectMocks
    private ShoppingBasketService shoppingBasketService;

    @Test
    void shouldAddItemToBasket() {
        // Arrange
        var basketId = UUID.randomUUID();
        var userId = 1L;
        var variantId = 10L;

        var basket = ShoppingBasket.builder()
                .id(basketId)
                .userId(userId)
                .build();

        var variant = ProductVariant.builder()
                .id(variantId)
                .build();

        var expectedDto = new ShoppingBasketDto();

        when(shoppingBasketRepository.findByIdAndUserId(basketId, userId))
                .thenReturn(Optional.of(basket));

        when(productVariantRepository.findById(variantId))
                .thenReturn(Optional.of(variant));

        when(shoppingBasketMapper.toDto(basket))
                .thenReturn(expectedDto);

        // Act
        var result = shoppingBasketService.addToBasket(
                basketId,
                variantId,
                userId
        );

        // Assert
        assertSame(expectedDto, result);

        assertEquals(1, basket.getItems().size());
        assertEquals(1, basket.getItem(variantId).getQuantity());

        verify(shoppingBasketRepository).save(basket);
        verify(shoppingBasketMapper).toDto(basket);
    }

    @Test
    void shouldCreateBasket() {
        // Arrange
        var userId = 1L;
        var expectedDto = new ShoppingBasketDto();

        when(shoppingBasketMapper.toDto(any(ShoppingBasket.class)))
                .thenReturn(expectedDto);

        // Act
        var result = shoppingBasketService.createBasket(userId);

        // Assert
        assertSame(expectedDto, result);

        var captor = ArgumentCaptor.forClass(ShoppingBasket.class);

        verify(shoppingBasketRepository).save(captor.capture());

        var savedBasket = captor.getValue();

        assertEquals(userId, savedBasket.getUserId());

        verify(shoppingBasketMapper).toDto(savedBasket);
    }

    @Test
    void shouldGetBasket() {
        // Arrange
        var basketId = UUID.randomUUID();
        var userId = 1L;

        var basket = ShoppingBasket.builder()
                .id(basketId)
                .userId(userId)
                .build();

        var expectedDto = new ShoppingBasketDto();

        when(shoppingBasketRepository.findWithItemsByIdAndUserId(basketId, userId))
                .thenReturn(Optional.of(basket));

        when(shoppingBasketMapper.toDto(basket))
                .thenReturn(expectedDto);

        // Act
        var result = shoppingBasketService.getBasket(basketId, userId);

        // Assert
        assertSame(expectedDto, result);

        verify(shoppingBasketRepository)
                .findWithItemsByIdAndUserId(basketId, userId);

        verify(shoppingBasketMapper)
                .toDto(basket);
    }

    @Test
    void shouldUpdateItem(){
        var basketId = UUID.randomUUID();
        var userId = 1L;
        var variantId = 10L;

        var basket = ShoppingBasket.builder()
                .id(basketId)
                .userId(userId)
                .build();

        basket.addItem(variantId);

        var expectedDto = new ShoppingBasketItemDto();

        when(shoppingBasketRepository.findByIdAndUserId(basketId,userId)).thenReturn(Optional.of(basket));
        when(shoppingBasketItemMapper.toDto(basket.getItem(variantId)))
                .thenReturn(expectedDto);

        var result = shoppingBasketService.updateItem(basketId,variantId,2,userId);

        assertSame(expectedDto, result);

        assertEquals(
                2,
                basket.getItem(variantId).getQuantity()
        );

        verify(shoppingBasketRepository).save(basket);

        verify(shoppingBasketItemMapper)
                .toDto(basket.getItem(variantId));

    }

    @Test
    void shouldRemoveItem() {
        // Arrange
        var basketId = UUID.randomUUID();
        var userId = 1L;
        var variantId = 10L;

        var basket = ShoppingBasket.builder()
                .id(basketId)
                .userId(userId)
                .build();

        basket.addItem(variantId);

        when(shoppingBasketRepository.findWithItemsByIdAndUserId(
                basketId,
                userId
        )).thenReturn(Optional.of(basket));

        // Act
        shoppingBasketService.removeItem(
                basketId,
                variantId,
                userId
        );

        // Assert
        assertNull(basket.getItem(variantId));

        assertTrue(basket.isEmpty());

        verify(shoppingBasketRepository).save(basket);
    }

    @Test
    void shouldClearBasket(){
        var basketId = UUID.randomUUID();
        var userId = 1L;
        var variantId = 10L;

        var basket = ShoppingBasket.builder()
                .id(basketId)
                .userId(userId)
                .build();

        basket.addItem(variantId);
        when(shoppingBasketRepository.findWithItemsByIdAndUserId(
                basketId,
                userId
        )).thenReturn(Optional.of(basket));
        shoppingBasketService.clearBasket(basketId,userId);
        assertNull(basket.getItem(variantId));
        assertTrue(basket.isEmpty());
        verify(shoppingBasketRepository)
                .findWithItemsByIdAndUserId(basketId, userId);
        verify(shoppingBasketRepository).save(basket);

    }


    @Test
    void shouldThrowExceptionWhenAddingToNonexistentBasket() {
        // Arrange
        var productVariantId = 10L;
        var userId = 1L;
        var basketId = UUID.randomUUID();

        when(shoppingBasketRepository.findByIdAndUserId(basketId, userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ShoppingBasketNotFoundException.class,
                () -> shoppingBasketService.addToBasket(
                        basketId,
                        productVariantId,
                        userId
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenAddingNonexistentVariant() {
        // Arrange
        var variantId = 10L;
        var userId = 1L;
        var basketId = UUID.randomUUID();

        var basket = ShoppingBasket.builder()
                .id(basketId)
                .userId(userId)
                .build();

        when(shoppingBasketRepository.findByIdAndUserId(basketId, userId))
                .thenReturn(Optional.of(basket));

        when(productVariantRepository.findById(variantId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ProductVariantNotFoundException.class,
                () -> shoppingBasketService.addToBasket(
                        basketId,
                        variantId,
                        userId
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenBasketNotFound(){
        var productVariantId = 10L;
        var userId = 1L;
        var basketId = UUID.randomUUID();

        when(shoppingBasketRepository.findByIdAndUserId(basketId, userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ShoppingBasketNotFoundException.class,
                () -> shoppingBasketService.addToBasket(
                        basketId,
                        productVariantId,
                        userId
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenGettingNonexistentBasket() {
        // Arrange
        var basketId = UUID.randomUUID();
        var userId = 1L;

        when(shoppingBasketRepository.findWithItemsByIdAndUserId(
                basketId,
                userId
        )).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ShoppingBasketNotFoundException.class,
                () -> shoppingBasketService.getBasket(
                        basketId,
                        userId
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingItemBasketNotFound() {
        var basketId = UUID.randomUUID();
        var variantId = 10L;
        var userId = 1L;

        when(shoppingBasketRepository.findByIdAndUserId(basketId, userId))
                .thenReturn(Optional.empty());

        assertThrows(
                ShoppingBasketNotFoundException.class,
                () -> shoppingBasketService.updateItem(
                        basketId,
                        variantId,
                        2,
                        userId
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentItem() {
        // Arrange
        var basketId = UUID.randomUUID();
        var variantId = 10L;
        var userId = 1L;

        var basket = ShoppingBasket.builder()
                .id(basketId)
                .userId(userId)
                .build();

        when(shoppingBasketRepository.findByIdAndUserId(basketId, userId))
                .thenReturn(Optional.of(basket));

        // Act & Assert
        assertThrows(
                ShoppingBasketItemNotFoundException.class,
                () -> shoppingBasketService.updateItem(
                        basketId,
                        variantId,
                        2,
                        userId
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenRemovingItemBasketNotFound() {
        // Arrange
        var basketId = UUID.randomUUID();
        var variantId = 10L;
        var userId = 1L;

        when(shoppingBasketRepository.findWithItemsByIdAndUserId(
                basketId,
                userId
        )).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ShoppingBasketNotFoundException.class,
                () -> shoppingBasketService.removeItem(
                        basketId,
                        variantId,
                        userId
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenRemovingNonexistentItem() {
        // Arrange
        var basketId = UUID.randomUUID();
        var variantId = 10L;
        var userId = 1L;

        var basket = ShoppingBasket.builder()
                .id(basketId)
                .userId(userId)
                .build();

        when(shoppingBasketRepository.findWithItemsByIdAndUserId(
                basketId,
                userId
        )).thenReturn(Optional.of(basket));

        // Act & Assert
        assertThrows(
                ShoppingBasketItemNotFoundException.class,
                () -> shoppingBasketService.removeItem(
                        basketId,
                        variantId,
                        userId
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenClearingNonexistentBasket() {
        // Arrange
        var userId = 1L;
        var basketId = UUID.randomUUID();

        when(shoppingBasketRepository.findWithItemsByIdAndUserId(
                basketId,
                userId
        )).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                ShoppingBasketNotFoundException.class,
                () -> shoppingBasketService.clearBasket(
                        basketId,
                        userId
                )
        );
    }

}