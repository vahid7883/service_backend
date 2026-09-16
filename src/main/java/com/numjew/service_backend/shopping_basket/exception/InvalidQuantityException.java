package com.numjew.service_backend.shopping_basket.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException() {
        super("quantity cannot be negative number or zero");
    }
}
