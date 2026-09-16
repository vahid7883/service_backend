package com.numjew.service_backend.product.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "product_id", nullable = false)
    private Long productId;


    @Column(nullable = false)
    private String imageUrl;


    private String altText;


    @Builder.Default
    private Boolean primaryImage = false;


    private Integer displayOrder;


    @CreationTimestamp
    private LocalDateTime createdAt;
}