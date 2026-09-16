package com.numjew.service_backend.product.dto;

import com.numjew.service_backend.product.domain.ProductStatus;
import lombok.Data;

@Data
public class ProductDto {
    private Long id;

    private String name;

    private ProductStatus status = ProductStatus.DRAFT;

    private Long brandId;

    private String slug;
}
