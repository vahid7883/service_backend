package com.numjew.service_backend.product.controller;

import com.numjew.service_backend.product.dto.CreateProductRequest;
import com.numjew.service_backend.product.dto.ProductDto;
import com.numjew.service_backend.product.dto.UpdateProductRequest;
import com.numjew.service_backend.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody CreateProductRequest request, UriComponentsBuilder uriBuilder) {

        ProductDto product = productService.createProduct(request);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(
            @PathVariable Long id) {

        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {

        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ProductDto>> getProductsByBrandId(
            @PathVariable Long brandId) {

        return ResponseEntity.ok(
                productService.getAllProductsByBrandId(brandId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {

        return ResponseEntity.ok(
                productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}

