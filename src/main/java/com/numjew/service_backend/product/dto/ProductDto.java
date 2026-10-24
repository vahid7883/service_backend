
package com.numjew.service_backend.product.dto;

import com.numjew.service_backend.product.domain.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private ProductStatus status;
    private Long brandId;
    private String slug;
}