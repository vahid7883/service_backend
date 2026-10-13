
package com.numjew.service_backend.common.exception;

import com.numjew.service_backend.brand.exception.BrandNotFoundException;
import com.numjew.service_backend.product.exception.DuplicateProductSlugException;
import com.numjew.service_backend.product.exception.InvalidProductSlugException;
import com.numjew.service_backend.product.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateProductSlugException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateProductSlug(
            DuplicateProductSlugException ex,
            WebRequest request) {

        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleBrandNotFound(
            BrandNotFoundException ex,
            WebRequest request) {

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleProductNotFound(
            ProductNotFoundException ex,
            WebRequest request) {

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidProductSlugException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidProductSlug(
            InvalidProductSlugException ex,
            WebRequest request) {

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            WebRequest request) {

        HttpServletRequest servletRequest =
                ((ServletWebRequest) request).getRequest();

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                servletRequest.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }
}