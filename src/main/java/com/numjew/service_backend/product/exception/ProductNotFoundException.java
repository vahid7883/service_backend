package com.numjew.service_backend.product.exception;


public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("product not found");
    }
}
