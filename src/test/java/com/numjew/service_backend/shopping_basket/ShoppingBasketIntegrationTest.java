package com.numjew.service_backend.shopping_basket;

import com.numjew.service_backend.product.domain.ProductVariant;
import com.numjew.service_backend.product.domain.ProductVariantStatus;
import com.numjew.service_backend.product.exception.ProductVariantNotAvailableException;
import com.numjew.service_backend.product.exception.ProductVariantNotFoundException;
import com.numjew.service_backend.product.repository.ProductVariantRepository;
import com.numjew.service_backend.shopping_basket.domain.ShoppingBasket;
import com.numjew.service_backend.shopping_basket.exception.InvalidQuantityException;
import com.numjew.service_backend.shopping_basket.exception.ShoppingBasketItemNotFoundException;
import com.numjew.service_backend.shopping_basket.exception.ShoppingBasketNotFoundException;
import com.numjew.service_backend.shopping_basket.repository.ShoppingBasketRepository;
import com.numjew.service_backend.shopping_basket.service.ShoppingBasketService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ShoppingBasketIntegrationTest {

    @Autowired
    private ShoppingBasketService shoppingBasketService;

    @Autowired
    private ShoppingBasketRepository shoppingBasketRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Test
    void shouldAddProductVariantToBasket() {

        // Arrange
        var userId = 1L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        var variant = productVariantRepository.save(
                ProductVariant.builder()
                        .sku("TEST-SKU-" + UUID.randomUUID())
                        .productId(1L)
                        .price(new BigDecimal("29.99"))
                        .status(ProductVariantStatus.ACTIVE)
                        .build()
        );

        // Act
        shoppingBasketService.addToBasket(
                basket.getId(),
                variant.getId(),
                userId
        );

        // Assert
        var savedBasket = shoppingBasketRepository
                .findWithItemsByIdAndUserId(basket.getId(), userId)
                .orElseThrow();

        assertThat(savedBasket.getItems()).hasSize(1);

        var item = savedBasket.getItem(variant.getId());

        assertThat(item).isNotNull();
        assertThat(item.getProductVariantId()).isEqualTo(variant.getId());
        assertThat(item.getQuantity()).isEqualTo(1);
    }

    @Test
    void shouldIncreaseQuantityWhenAddingSameProductVariant() {

        // Arrange
        var userId = 1L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        var variant = productVariantRepository.save(
                ProductVariant.builder()
                        .sku("TEST-SKU-" + UUID.randomUUID())
                        .productId(1L)
                        .price(new BigDecimal("29.99"))
                        .status(ProductVariantStatus.ACTIVE)
                        .build()
        );

        // Act
        shoppingBasketService.addToBasket(
                basket.getId(),
                variant.getId(),
                userId
        );

        shoppingBasketService.addToBasket(
                basket.getId(),
                variant.getId(),
                userId
        );

        // Assert
        var savedBasket = shoppingBasketRepository
                .findWithItemsByIdAndUserId(basket.getId(), userId)
                .orElseThrow();

        assertThat(savedBasket.getItems()).hasSize(1);

        var item = savedBasket.getItem(variant.getId());

        assertThat(item).isNotNull();
        assertThat(item.getProductVariantId()).isEqualTo(variant.getId());
        assertThat(item.getQuantity()).isEqualTo(2);
    }


    @Test
    void shouldCreateBasket() {

        // Arrange
        var userId = 1L;

        // Act
        var result = shoppingBasketService.createBasket(userId);

        // Assert
        assertThat(result).isNotNull();

        var savedBasket = shoppingBasketRepository
                .findById(result.getId())
                .orElseThrow();

        assertThat(savedBasket.getUserId()).isEqualTo(userId);
    }

    @Test
    void shouldGetBasket() {

        // Arrange
        var userId = 1L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        // Act
        var result = shoppingBasketService.getBasket(
                basket.getId(),
                userId
        );

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(basket.getId());
    }


    @Test
    void shouldUpdateBasketItem(){
        var userId = 1L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        var variant = productVariantRepository.save(
                ProductVariant.builder()
                        .sku("TEST-SKU-" + UUID.randomUUID())
                        .productId(1L)
                        .price(new BigDecimal("29.99"))
                        .status(ProductVariantStatus.ACTIVE)
                        .build()
        );

        // Act

        shoppingBasketService.addToBasket(basket.getId(),variant.getId(),userId);
        shoppingBasketService.updateItem(basket.getId(),variant.getId(),5,userId);


        var savedBasket = shoppingBasketRepository
                .findWithItemsByIdAndUserId(basket.getId(), userId)
                .orElseThrow();

        var item = savedBasket.getItem(variant.getId());

        assertThat(item).isNotNull();
        assertThat(item.getProductVariantId()).isEqualTo(variant.getId());
        assertThat(item.getQuantity()).isEqualTo(5);


    }

    @Test
    void shouldRemoveProductVariantFromBasket(){
        var userId = 1L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        var variant = productVariantRepository.save(
                ProductVariant.builder()
                        .sku("TEST-SKU-" + UUID.randomUUID())
                        .productId(1L)
                        .price(new BigDecimal("29.99"))
                        .status(ProductVariantStatus.ACTIVE)
                        .build()
        );

        // Act

        shoppingBasketService.addToBasket(basket.getId(),variant.getId(),userId);
        shoppingBasketService.removeItem(basket.getId(),variant.getId(),userId);

        //end
        var savedBasket = shoppingBasketRepository
                .findWithItemsByIdAndUserId(basket.getId(), userId)
                .orElseThrow();

        var item = savedBasket.getItem(variant.getId());

        assertThat(item).isNull();
        assertThat(savedBasket.getItems()).isEmpty();

    }

    @Test
    void shouldClearBasket(){
        var userId = 1L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        var variant = productVariantRepository.save(
                ProductVariant.builder()
                        .sku("TEST-SKU-" + UUID.randomUUID())
                        .productId(1L)
                        .price(new BigDecimal("29.99"))
                        .status(ProductVariantStatus.ACTIVE)
                        .build()
        );

        // Act

        shoppingBasketService.addToBasket(basket.getId(),variant.getId(),userId);
        shoppingBasketService.clearBasket(basket.getId(),userId);

        var savedBasket = shoppingBasketRepository
                .findWithItemsByIdAndUserId(basket.getId(), userId)
                .orElseThrow();

        assertThat(savedBasket.getItems()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenProductVariantDoesNotExist(){
        var userId = 1L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        var nonExistentVariantId = 999999L;

        // Act & Assert
        assertThatThrownBy(() ->
                shoppingBasketService.addToBasket(
                        basket.getId(),
                        nonExistentVariantId,
                        userId
                )
        ).isInstanceOf(ProductVariantNotFoundException.class);
    }

    @Test
    void shouldNotAccessBasketOwnedByAnotherUser(){
        var userId = 1L;
        var anotherUserId = 2L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        assertThatThrownBy(() ->
                shoppingBasketService.getBasket(
                        basket.getId(),
                        anotherUserId
                )
        ).isInstanceOf(ShoppingBasketNotFoundException.class);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentBasketItem() {

        // Arrange
        var userId = 1L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        var nonExistentProductVariantId = 999999L;

        // Act & Assert
        assertThatThrownBy(() ->
                shoppingBasketService.updateItem(
                        basket.getId(),
                        nonExistentProductVariantId,
                        5,
                        userId
                )
        ).isInstanceOf(ShoppingBasketItemNotFoundException.class);
    }


    @Test
    void shouldRejectZeroQuantity() {
        var userId = 1L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        var variant = productVariantRepository.save(
                ProductVariant.builder()
                        .sku("TEST-SKU-" + UUID.randomUUID())
                        .productId(1L)
                        .price(new BigDecimal("29.99"))
                        .status(ProductVariantStatus.ACTIVE)
                        .build()
        );

        shoppingBasketService.addToBasket(
                basket.getId(),
                variant.getId(),
                userId
        );

        assertThatThrownBy(() ->
                shoppingBasketService.updateItem(
                        basket.getId(),
                        variant.getId(),
                        0,
                        userId
                )
        ).isInstanceOf(InvalidQuantityException.class);
    }

    @Test
    void shouldRejectNegativeQuantity(){
        var userId = 1L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        var variant = productVariantRepository.save(
                ProductVariant.builder()
                        .sku("TEST-SKU-" + UUID.randomUUID())
                        .productId(1L)
                        .price(new BigDecimal("29.99"))
                        .status(ProductVariantStatus.ACTIVE)
                        .build()
        );

        shoppingBasketService.addToBasket(
                basket.getId(),
                variant.getId(),
                userId
        );

        assertThatThrownBy(() ->
                shoppingBasketService.updateItem(
                        basket.getId(),
                        variant.getId(),
                        -1,
                        userId
                )
        ).isInstanceOf(InvalidQuantityException.class);
    }

    @Test
    void shouldNotAddInactiveProductVariant(){
        var userId = 1L;

        var basket = shoppingBasketRepository.save(
                ShoppingBasket.builder()
                        .userId(userId)
                        .build()
        );

        var variant = productVariantRepository.save(
                ProductVariant.builder()
                        .sku("TEST-SKU-" + UUID.randomUUID())
                        .productId(1L)
                        .price(new BigDecimal("29.99"))
                        .status(ProductVariantStatus.DISCONTINUED)
                        .build()
        );



        assertThatThrownBy(() ->
            shoppingBasketService.addToBasket(
                    basket.getId(),
                    variant.getId(),
                    userId
            )
        ).isInstanceOf(ProductVariantNotAvailableException.class);
    }

    @Test
    void shouldThrowExceptionWhenBasketDoesNotExist() {
        var userId = 1L;
        var invalidBasketId = UUID.randomUUID();

        assertThatThrownBy(() ->
                shoppingBasketService.getBasket(
                        invalidBasketId,
                        userId
                )
        ).isInstanceOf(ShoppingBasketNotFoundException.class);
    }



}