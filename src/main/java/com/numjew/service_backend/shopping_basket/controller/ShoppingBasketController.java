package com.numjew.service_backend.shopping_basket.controller;

import com.numjew.service_backend.auth.AuthService;
import com.numjew.service_backend.product.exception.ProductNotFoundException;
import com.numjew.service_backend.product.exception.ProductVariantNotFoundException;
import com.numjew.service_backend.shopping_basket.domain.AddBasketItemRequest;
import com.numjew.service_backend.shopping_basket.domain.UpdateBasketItemRequest;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketDto;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketItemDto;
import com.numjew.service_backend.shopping_basket.exception.ShoppingBasketNotFoundException;
import com.numjew.service_backend.shopping_basket.service.ShoppingBasketService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/shopping-baskets")
@AllArgsConstructor
public class ShoppingBasketController {
    private final ShoppingBasketService shoppingBasketService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<ShoppingBasketDto> createBasket(UriComponentsBuilder uriBuilder) {
        var user = authService.getCurrentUser();
        var basketDto = shoppingBasketService.createBasket(user.getId());
        var uri = uriBuilder.path("/shopping-baskets/{id}").buildAndExpand(basketDto.getId()).toUri();
        return ResponseEntity.created(uri).body(basketDto);

    }


    @PostMapping("/{shoppingBasketId}/items")
    public ResponseEntity<ShoppingBasketDto> addToBasket(
            @PathVariable UUID shoppingBasketId,
            @Valid @RequestBody AddBasketItemRequest request
    ) {
        var user = authService.getCurrentUser();

        var basketDto = shoppingBasketService.addToBasket(
                shoppingBasketId,
                request.getProductVariantId(),
                user.getId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(basketDto);
    }

    @GetMapping("/{basketId}")
    public ShoppingBasketDto getCart(@PathVariable UUID basketId) {
        var user = authService.getCurrentUser();

        return shoppingBasketService.getBasket(basketId, user.getId());
    }

    @PutMapping("/{basketId}/items/{productVariantId}")
    public ShoppingBasketItemDto updateItem(
            @PathVariable("basketId") UUID basketId,
            @PathVariable("productVariantId") Long productVariantId,
            @Valid @RequestBody UpdateBasketItemRequest request
    ) {
        var user = authService.getCurrentUser();

        return shoppingBasketService.updateItem(basketId, productVariantId, request.getQuantity(), user.getId());
    }

    @DeleteMapping("/{basketId}/items/{productVariantId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable("basketId") UUID basketId,
            @PathVariable("productVariantId") Long productVariantId
    ) {
        var user = authService.getCurrentUser();

        shoppingBasketService.removeItem(basketId, productVariantId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{basketId}/items")
    public ResponseEntity<Void> clearBasket(@PathVariable UUID basketId) {
        var user = authService.getCurrentUser();

        shoppingBasketService.clearBasket(basketId, user.getId());

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ShoppingBasketNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBasketNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Basket not found."));
    }

    @ExceptionHandler(ProductVariantNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found."));
    }



}


