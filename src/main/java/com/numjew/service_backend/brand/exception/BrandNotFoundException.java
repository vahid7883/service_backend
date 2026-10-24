package com.numjew.service_backend.brand.exception;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(Long brandId) {
        super("Brand not found with id: " + brandId);
    }
}
