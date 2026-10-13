package com.numjew.service_backend.product.exception;

public class ProductVariantNotAvailableException extends RuntimeException{
    public ProductVariantNotAvailableException() {
        super("ProductVariant Not Available or it has been discontinued! ");
    }
}
