package com.numjew.service_backend.shopping_basket.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ShoppingBasketDto {

    private UUID id;

    private List<ShoppingBasketItemDto> items = new ArrayList<>();
}