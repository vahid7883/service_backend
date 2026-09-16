package com.numjew.service_backend.product.exception;

public class ProductVariantNotFoundException extends RuntimeException{
    public ProductVariantNotFoundException(){
        super("productVariant not found");
    }
}
