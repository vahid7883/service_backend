package com.numjew.service_backend.payment.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_methods")
@Getter
@Setter
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType = PaymentType.DEBIT_CARD;

    @Column(nullable = false)
    private String token;

    @Column(length = 2000)
    private String metadata;

}
