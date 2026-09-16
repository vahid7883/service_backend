package com.numjew.service_backend.shopping_basket.mapper;

import com.numjew.service_backend.shopping_basket.domain.ShoppingBasket;
import com.numjew.service_backend.shopping_basket.dto.ShoppingBasketDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShoppingBasketMapper {
    @Mapping(target = "id", source = "id")
    ShoppingBasketDto toDto(ShoppingBasket shoppingBasket);

    ShoppingBasket toEntity(ShoppingBasketDto shoppingBasketDto);

}
