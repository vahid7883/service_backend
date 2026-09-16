package com.numjew.service_backend.order.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;


        @Column(name = "order_id", nullable = false)
        private Long orderId;


        @Column(name = "product_variant_id", nullable = false)
        private Long productVariantId;


        // Snapshot
        @NotBlank
        @Column(nullable = false)
        private String productName;


        @NotBlank
        @Column(nullable = false)
        private String sku;


        @Column(nullable = false, precision = 12, scale = 2)
        private BigDecimal unitPrice;


        @NotNull
        @Positive
        @Column(nullable = false)
        private Integer quantity;
}




