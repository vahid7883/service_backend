package com.numjew.service_backend.order.domain;

public enum OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    OUT_FOR_DELIVERY,
    CANCELLED,
    DELIVERED
}