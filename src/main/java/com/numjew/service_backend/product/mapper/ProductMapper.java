package com.numjew.service_backend.product.mapper;

import com.numjew.service_backend.product.domain.Product;
import com.numjew.service_backend.product.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", source = "id")
    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);

}
