package com.numjew.service_backend.shopping_basket.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

    @Data
    public class UpdateBasketItemRequest {
        @NotNull(message = "Quantity must be provided.")
        @Positive(message = "Quantity must be greater than zero.")
        @Max(value = 100, message = "Quantity must be less than or equal to 100.")
        private Integer quantity;
    }

