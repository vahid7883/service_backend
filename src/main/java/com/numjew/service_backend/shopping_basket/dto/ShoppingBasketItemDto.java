package com.numjew.service_backend.shopping_basket.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ShoppingBasketItemDto {
        private Long productVariantId;
        private Long productId;
        private String sku;
        private BigDecimal unitPrice;
        private Integer quantity;
}
