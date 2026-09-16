package com.numjew.service_backend.inventory.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "stock_reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockReservation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "variant_id", nullable = false)
    private Long variantId;


    @Column(nullable = false)
    private Integer quantity;


    @Column(name = "reservation_reference", nullable = false)
    private String reservationReference;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReservationStatus status = ReservationStatus.RESERVED;


    @Column(nullable = false)
    private LocalDateTime expiresAt;


    @CreationTimestamp
    private LocalDateTime createdAt;
}