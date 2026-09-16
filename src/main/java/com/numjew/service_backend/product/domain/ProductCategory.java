package com.numjew.service_backend.product.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "product_categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "category_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "product_id", nullable = false)
    private Long productId;


    @Column(name = "category_id", nullable = false)
    private Long categoryId;

}