
package com.numjew.service_backend.product.exception;

public class DuplicateProductSlugException extends RuntimeException {

    public DuplicateProductSlugException(String slug) {
        super("A product with slug '" + slug + "' already exists.");
    }
}