package com.numjew.service_backend.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.numjew.service_backend.common.exception.GlobalExceptionHandler;
import com.numjew.service_backend.product.domain.ProductStatus;
import com.numjew.service_backend.product.dto.CreateProductRequest;
import com.numjew.service_backend.product.dto.ProductDto;
import com.numjew.service_backend.product.dto.UpdateProductRequest;
import com.numjew.service_backend.product.exception.ProductNotFoundException;
import com.numjew.service_backend.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateProductAndReturnCreatedResponse() throws Exception {

        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Wireless Headphones");
        request.setDescription("Bluetooth headphones");
        request.setBrandId(1L);

        ProductDto product = new ProductDto();
        product.setId(10L);
        product.setName("Wireless Headphones");
        product.setDescription("Bluetooth headphones");
        product.setStatus(ProductStatus.DRAFT);
        product.setBrandId(1L);
        product.setSlug("wireless-headphones");

        when(productService.createProduct(any(CreateProductRequest.class)))
                .thenReturn(product);

        // Act & Assert
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/products/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Wireless Headphones"))
                .andExpect(jsonPath("$.description").value("Bluetooth headphones"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.slug").value("wireless-headphones"));
    }

    @Test
    void shouldRejectProductCreationWhenNameIsBlank() throws Exception {

        CreateProductRequest request = new CreateProductRequest();
        request.setName("   ");
        request.setDescription("Bluetooth headphones");
        request.setBrandId(1L);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .createProduct(any(CreateProductRequest.class));
    }

    @Test
    void shouldReturnProductWhenProductExists() throws Exception {

        // Arrange
        ProductDto product = new ProductDto();
        product.setId(10L);
        product.setName("Wireless Headphones");
        product.setDescription("Bluetooth headphones");
        product.setStatus(ProductStatus.DRAFT);
        product.setBrandId(1L);
        product.setSlug("wireless-headphones");

        when(productService.getProductById(10L))
                .thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/products/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Wireless Headphones"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.slug").value("wireless-headphones"));

        verify(productService, times(1)).getProductById(10L);
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {

        when(productService.getProductById(999L))
                .thenThrow(new ProductNotFoundException());

        mockMvc.perform(get("/products/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(productService).getProductById(999L);
    }

    @Test
    void shouldReturnAllProducts() throws Exception {

        // Arrange
        ProductDto product1 = new ProductDto();
        product1.setId(1L);
        product1.setName("Wireless Headphones");
        product1.setStatus(ProductStatus.DRAFT);
        product1.setBrandId(1L);
        product1.setSlug("wireless-headphones");

        ProductDto product2 = new ProductDto();
        product2.setId(2L);
        product2.setName("Gaming Mouse");
        product2.setStatus(ProductStatus.DRAFT);
        product2.setBrandId(2L);
        product2.setSlug("gaming-mouse");

        when(productService.getAllProducts())
                .thenReturn(List.of(product1, product2));

        // Act & Assert
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Wireless Headphones"))
                .andExpect(jsonPath("$[1].name").value("Gaming Mouse"));

        verify(productService).getAllProducts();
    }

    @Test
    void shouldReturnProductsByBrandId() throws Exception {

        // Arrange
        Long brandId = 1L;

        ProductDto product = new ProductDto();
        product.setId(10L);
        product.setName("Wireless Headphones");
        product.setStatus(ProductStatus.DRAFT);
        product.setBrandId(brandId);
        product.setSlug("wireless-headphones");

        when(productService.getAllProductsByBrandId(brandId))
                .thenReturn(List.of(product));

        // Act & Assert
        mockMvc.perform(get("/products/brand/{brandId}", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].name").value("Wireless Headphones"))
                .andExpect(jsonPath("$[0].brandId").value(1));

        verify(productService).getAllProductsByBrandId(brandId);
    }

    @Test
    void shouldUpdateProductAndReturnUpdatedProduct() throws Exception {

        // Arrange
        Long productId = 10L;

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Headphones");
        request.setDescription("Updated Bluetooth headphones");
        request.setBrandId(2L);

        ProductDto updatedProduct = new ProductDto();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Headphones");
        updatedProduct.setDescription("Updated Bluetooth headphones");
        updatedProduct.setStatus(ProductStatus.DRAFT);
        updatedProduct.setBrandId(2L);
        updatedProduct.setSlug("updated-headphones");

        when(productService.updateProduct(
                org.mockito.ArgumentMatchers.eq(productId),
                any(UpdateProductRequest.class)))
                .thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .put("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Updated Headphones"))
                .andExpect(jsonPath("$.description").value("Updated Bluetooth headphones"))
                .andExpect(jsonPath("$.brandId").value(2))
                .andExpect(jsonPath("$.slug").value("updated-headphones"));

        verify(productService).updateProduct(
                org.mockito.ArgumentMatchers.eq(productId),
                any(UpdateProductRequest.class));
    }

    @Test
    void shouldDeleteProductAndReturnNoContent() throws Exception {

        // Arrange
        Long productId = 10L;

        // Act & Assert
        mockMvc.perform(delete("/products/{id}", productId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(productService).deleteProduct(productId);
    }

    @Test
    void shouldRejectProductUpdateWhenNameIsBlank() throws Exception {

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("   ");
        request.setDescription("Updated description");
        request.setBrandId(1L);

        mockMvc.perform(put("/products/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .updateProduct(eq(10L), any(UpdateProductRequest.class));
    }
}
