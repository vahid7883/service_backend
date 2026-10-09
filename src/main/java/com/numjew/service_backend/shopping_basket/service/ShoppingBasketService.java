package com.numjew.service_backend.shopping_basket.service;

import com.numjew.service_backend.product.domain.ProductVariantStatus;
import com.numjew.service_backend.product.exception.ProductVariantNotAvailableException;
import com.numjew.service_backend.shopping_basket.mapper.ShoppingBasketItemMapper;
import com.numjew.service_backend.shopping_basket.mapper.ShoppingBasketMapper;
import com.numjew.service_backend.product.exception.ProductVariantNotFoundException;
import com.numjew.service_backend.product.repository.ProductVariantRepository;
import com.numjew.service_backend.shopping_basket.exception.ShoppingBasketItemNotFoundException;
import com.numjew.service_backend.shopping_basket.exception.ShoppingBasketNotFoundException;
import com.numjew.service_backend.shopping_basket.domain.ShoppingBasket;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketDto;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketItemDto;
import com.numjew.service_backend.shopping_basket.repository.ShoppingBasketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class ShoppingBasketService {

    private final ShoppingBasketRepository shoppingBasketRepository;
    private final ShoppingBasketMapper shoppingBasketMapper;
    private final ShoppingBasketItemMapper shoppingBasketItemMapper;
    private final ProductVariantRepository productVariantRepository;

    public ShoppingBasketDto createBasket(Long userId) {
        var basket = ShoppingBasket.builder()
                .userId(userId)
                .build();
        shoppingBasketRepository.save(basket);
        return shoppingBasketMapper.toDto(basket);
    }


    public ShoppingBasketDto addToBasket(UUID shoppingBasketId, Long productVariantId, Long userId) {
        var basket = shoppingBasketRepository
                .findByIdAndUserId(shoppingBasketId, userId)
                .orElseThrow(ShoppingBasketNotFoundException::new);

        var variant = productVariantRepository
                .findById(productVariantId)
                .orElseThrow(ProductVariantNotFoundException::new);
        if (variant.getStatus() != ProductVariantStatus.ACTIVE) {
            throw new ProductVariantNotAvailableException();
        }

        basket.addItem(variant.getId());
        shoppingBasketRepository.save(basket);
        return shoppingBasketMapper.toDto(basket);
    }

    public ShoppingBasketDto getBasket(UUID shoppingBasketId, Long userId) {
        var basket = shoppingBasketRepository
                .findWithItemsByIdAndUserId(shoppingBasketId, userId)
                .orElseThrow(ShoppingBasketNotFoundException::new);

        return shoppingBasketMapper.toDto(basket);
    }

    public ShoppingBasketItemDto updateItem(UUID shoppingBasketId, Long productVariantId, Integer quantity, Long userId) {
        var basket = shoppingBasketRepository
                .findByIdAndUserId(shoppingBasketId, userId)
                .orElseThrow(ShoppingBasketNotFoundException::new);
        var basketItems = basket.getItem(productVariantId);
        if (basketItems == null) {
            throw new ShoppingBasketItemNotFoundException();
        }

        basketItems.updateQuantity(quantity);
        shoppingBasketRepository.save(basket);

        return shoppingBasketItemMapper.toDto(basketItems);
    }


    public void removeItem(UUID shoppingBasketId, Long productVariantId, Long userId) {
        var basket = shoppingBasketRepository
                .findWithItemsByIdAndUserId(shoppingBasketId, userId)
                .orElseThrow(ShoppingBasketNotFoundException::new);
        basket.removeItem(productVariantId);
        shoppingBasketRepository.save(basket);

    }

    public void clearBasket(UUID shoppingBasketId,Long userId) {
        var basket = shoppingBasketRepository
                .findWithItemsByIdAndUserId(shoppingBasketId, userId)
                .orElseThrow(ShoppingBasketNotFoundException::new);
        basket.clear();
        shoppingBasketRepository.save(basket);
    }
}





