package com.numjew.service_backend.shopping_basket.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddBasketItemRequest {

    @NotNull
    private Long productVariantId;
}

