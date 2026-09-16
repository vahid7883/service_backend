package com.numjew.service_backend.shopping_basket.mapper;

import com.numjew.service_backend.shopping_basket.domain.ShoppingBasketItem;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketItemDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShoppingBasketItemMapper {

    ShoppingBasketItemDto toDto(ShoppingBasketItem shoppingBasketItem);

    ShoppingBasketItem toEntity(ShoppingBasketItemDto shoppingBasketItemDto);
}
