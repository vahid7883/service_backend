package com.numjew.service_backend.common.exception;

import com.numjew.service_backend.product.exception.DuplicateProductSlugException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import com.numjew.service_backend.brand.exception.BrandNotFoundException;
import com.numjew.service_backend.product.exception.InvalidProductSlugException;
import com.numjew.service_backend.product.exception.ProductNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void shouldReturnConflictWhenProductSlugAlreadyExists() {

        // Arrange
        var exception =
                new DuplicateProductSlugException("wireless-headphones");

        var servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/api/products");

        var request = new ServletWebRequest(servletRequest);

        // Act
        var response = handler.handleDuplicateProductSlug(
                exception,
                request
        );

        // Assert
        assertEquals(409, response.getStatusCode().value());
        assertNotNull(response.getBody());

        var body = response.getBody();

        assertEquals(409, body.status());
        assertEquals("Conflict", body.error());
        assertEquals(
                "A product with slug 'wireless-headphones' already exists.",
                body.message()
        );
        assertEquals("/api/products", body.path());
        assertNotNull(body.timestamp());
    }


    @Test
    void shouldReturnNotFoundWhenBrandDoesNotExist() {

        // Arrange
        var exception = new BrandNotFoundException(9999L);
        var servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/api/products");

        var request = new ServletWebRequest(servletRequest);

        // Act
        var response = handler.handleBrandNotFound(exception, request);

        // Assert
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());

        var body = response.getBody();

        assertEquals(404, body.status());
        assertEquals("Not Found", body.error());
        assertEquals(exception.getMessage(), body.message());
        assertEquals("/api/products", body.path());
        assertNotNull(body.timestamp());
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() {

        // Arrange
        var exception = new ProductNotFoundException();

        var servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/api/products/999");

        var request = new ServletWebRequest(servletRequest);

        // Act
        var response = handler.handleProductNotFound(exception, request);

        // Assert
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());

        var body = response.getBody();

        assertEquals(404, body.status());
        assertEquals("Not Found", body.error());
        assertEquals(exception.getMessage(), body.message());
        assertEquals("/api/products/999", body.path());
        assertNotNull(body.timestamp());
    }

    @Test
    void shouldReturnBadRequestWhenProductSlugIsInvalid() {

        // Arrange
        var exception = new InvalidProductSlugException("!!!");

        var servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/api/products");

        var request = new ServletWebRequest(servletRequest);

        // Act
        var response = handler.handleInvalidProductSlug(exception, request);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());

        var body = response.getBody();

        assertEquals(400, body.status());
        assertEquals("Bad Request", body.error());
        assertEquals(exception.getMessage(), body.message());
        assertEquals("/api/products", body.path());
        assertNotNull(body.timestamp());
    }
}