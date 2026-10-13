
package com.numjew.service_backend.product.exception;

public class InvalidProductSlugException extends RuntimeException {

    public InvalidProductSlugException(String productName) {
        super("Unable to generate a valid slug for product name: '"
                + productName + "'.");
    }
}