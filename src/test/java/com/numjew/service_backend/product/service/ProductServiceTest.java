
package com.numjew.service_backend.product.service;

import com.numjew.service_backend.brand.exception.BrandNotFoundException;
import com.numjew.service_backend.brand.repository.BrandRepository;
import com.numjew.service_backend.product.domain.Product;
import com.numjew.service_backend.product.domain.ProductStatus;
import com.numjew.service_backend.product.dto.CreateProductRequest;
import com.numjew.service_backend.product.dto.ProductDto;
import com.numjew.service_backend.product.dto.UpdateProductRequest;
import com.numjew.service_backend.product.exception.DuplicateProductSlugException;
import com.numjew.service_backend.product.exception.InvalidProductSlugException;
import com.numjew.service_backend.product.exception.ProductNotFoundException;
import com.numjew.service_backend.product.mapper.ProductMapper;
import com.numjew.service_backend.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.numjew.service_backend.category.exception.CategoryNotFoundException;
import com.numjew.service_backend.category.repository.CategoryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductSlugGenerator slugGenerator;

    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProductWithDraftStatus() {

        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Wireless Headphones");
        request.setDescription("Bluetooth headphones");
        request.setBrandId(1L);

        ProductDto expectedDto = new ProductDto();
        expectedDto.setId(10L);
        expectedDto.setName("Wireless Headphones");
        expectedDto.setDescription("Bluetooth headphones");
        expectedDto.setBrandId(1L);
        expectedDto.setSlug("wireless-headphones");
        expectedDto.setStatus(ProductStatus.DRAFT);

        when(slugGenerator.generate("Wireless Headphones"))
                .thenReturn("wireless-headphones");

        when(brandRepository.existsById(1L))
                .thenReturn(true);

        when(productRepository.existsBySlug("wireless-headphones"))
                .thenReturn(false);

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    product.setId(10L);
                    return product;
                });

        when(productMapper.toDto(any(Product.class)))
                .thenReturn(expectedDto);

        // Act
        ProductDto result = productService.createProduct(request);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Wireless Headphones", result.getName());
        assertEquals("wireless-headphones", result.getSlug());
        assertEquals(ProductStatus.DRAFT, result.getStatus());

        ArgumentCaptor<Product> captor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(captor.capture());

        Product savedProduct = captor.getValue();

        assertEquals("Wireless Headphones", savedProduct.getName());
        assertEquals("Bluetooth headphones", savedProduct.getDescription());
        assertEquals(1L, savedProduct.getBrandId());
        assertEquals("wireless-headphones", savedProduct.getSlug());
        assertEquals(ProductStatus.DRAFT, savedProduct.getStatus());

        verify(productMapper).toDto(savedProduct);
    }


    @Test
    void shouldThrowExceptionWhenBrandDoesNotExist() {

        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Wireless Headphones");
        request.setBrandId(99L);

        when(slugGenerator.generate("Wireless Headphones"))
                .thenReturn("wireless-headphones");

        when(brandRepository.existsById(99L))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                BrandNotFoundException.class,
                () -> productService.createProduct(request)
        );

        verify(productRepository, never()).save(any(Product.class));
        verify(productRepository, never()).existsBySlug(anyString());
        verifyNoInteractions(productMapper);
    }


    @Test
    void shouldThrowExceptionWhenSlugAlreadyExists() {

        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Wireless Headphones");
        request.setBrandId(1L);

        when(slugGenerator.generate("Wireless Headphones"))
                .thenReturn("wireless-headphones");

        when(brandRepository.existsById(1L))
                .thenReturn(true);

        when(productRepository.existsBySlug("wireless-headphones"))
                .thenReturn(true);

        // Act & Assert
        assertThrows(
                DuplicateProductSlugException.class,
                () -> productService.createProduct(request)
        );

        verify(productRepository, never()).save(any(Product.class));
        verifyNoInteractions(productMapper);
    }


    @Test
    void shouldThrowExceptionWhenGeneratedSlugIsBlank() {

        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName("!!!");
        request.setBrandId(1L);

        when(slugGenerator.generate("!!!"))
                .thenReturn("");

        // Act & Assert
        assertThrows(
                InvalidProductSlugException.class,
                () -> productService.createProduct(request)
        );

        verifyNoInteractions(brandRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(productMapper);
    }


    @Test
    void shouldTrimProductNameBeforeGeneratingSlugAndSaving() {

        // Arrange
        CreateProductRequest request = new CreateProductRequest();
        request.setName("  Wireless Headphones  ");
        request.setDescription("Bluetooth headphones");
        request.setBrandId(1L);

        ProductDto expectedDto = new ProductDto();
        expectedDto.setName("Wireless Headphones");
        expectedDto.setSlug("wireless-headphones");
        expectedDto.setBrandId(1L);
        expectedDto.setStatus(ProductStatus.DRAFT);

        when(slugGenerator.generate("Wireless Headphones"))
                .thenReturn("wireless-headphones");

        when(brandRepository.existsById(1L))
                .thenReturn(true);

        when(productRepository.existsBySlug("wireless-headphones"))
                .thenReturn(false);

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(productMapper.toDto(any(Product.class)))
                .thenReturn(expectedDto);

        // Act
        ProductDto result = productService.createProduct(request);

        // Assert
        assertEquals("Wireless Headphones", result.getName());

        verify(slugGenerator).generate("Wireless Headphones");

        ArgumentCaptor<Product> captor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(captor.capture());

        assertEquals("Wireless Headphones", captor.getValue().getName());
        assertEquals(
                "wireless-headphones",
                captor.getValue().getSlug()
        );
    }


    @Test
    void shouldReturnProductDtoWhenProductExists() {

        // Arrange
        Product product = Product.builder()
                .id(10L)
                .name("Wireless Headphones")
                .description("Bluetooth headphones")
                .brandId(1L)
                .slug("wireless-headphones")
                .status(ProductStatus.DRAFT)
                .build();

        ProductDto expectedDto = new ProductDto();
        expectedDto.setId(10L);
        expectedDto.setName("Wireless Headphones");
        expectedDto.setDescription("Bluetooth headphones");
        expectedDto.setBrandId(1L);
        expectedDto.setSlug("wireless-headphones");
        expectedDto.setStatus(ProductStatus.DRAFT);

        when(productRepository.findById(10L))
                .thenReturn(java.util.Optional.of(product));

        when(productMapper.toDto(product))
                .thenReturn(expectedDto);

        // Act
        ProductDto result = productService.getProductById(10L);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Wireless Headphones", result.getName());
        assertEquals("wireless-headphones", result.getSlug());
        assertEquals(ProductStatus.DRAFT, result.getStatus());

        verify(productRepository).findById(10L);
        verify(productMapper).toDto(product);
    }


    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {

        // Arrange
        when(productRepository.findById(999L))
                .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(999L)
        );

        verify(productRepository).findById(999L);
        verifyNoInteractions(productMapper);
    }


    @Test
    void shouldReturnAllProducts() {

        // Arrange
        Product product1 = Product.builder()
                .id(1L)
                .name("Wireless Headphones")
                .brandId(1L)
                .slug("wireless-headphones")
                .status(ProductStatus.DRAFT)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Gaming Mouse")
                .brandId(2L)
                .slug("gaming-mouse")
                .status(ProductStatus.DRAFT)
                .build();

        ProductDto dto1 = new ProductDto();
        dto1.setId(1L);
        dto1.setName("Wireless Headphones");

        ProductDto dto2 = new ProductDto();
        dto2.setId(2L);
        dto2.setName("Gaming Mouse");

        when(productRepository.findAll())
                .thenReturn(List.of(product1, product2));

        when(productMapper.toDto(product1)).thenReturn(dto1);
        when(productMapper.toDto(product2)).thenReturn(dto2);

        // Act
        List<ProductDto> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Wireless Headphones", result.get(0).getName());
        assertEquals("Gaming Mouse", result.get(1).getName());

        verify(productRepository).findAll();
        verify(productMapper).toDto(product1);
        verify(productMapper).toDto(product2);
    }


    @Test
    void shouldReturnProductsForExistingBrand() {

        // Arrange
        Long brandId = 1L;

        Product product = Product.builder()
                .id(10L)
                .name("Wireless Headphones")
                .brandId(brandId)
                .slug("wireless-headphones")
                .status(ProductStatus.DRAFT)
                .build();

        ProductDto dto = new ProductDto();
        dto.setId(10L);
        dto.setName("Wireless Headphones");
        dto.setBrandId(brandId);

        when(brandRepository.existsById(brandId)).thenReturn(true);
        when(productRepository.findProductsByBrandId(brandId))
                .thenReturn(List.of(product));
        when(productMapper.toDto(product)).thenReturn(dto);

        // Act
        List<ProductDto> result =
                productService.getAllProductsByBrandId(brandId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(brandId, result.get(0).getBrandId());
        assertEquals("Wireless Headphones", result.get(0).getName());

        verify(brandRepository).existsById(brandId);
        verify(productRepository).findProductsByBrandId(brandId);
        verify(productMapper).toDto(product);
    }

    @Test
    void shouldThrowExceptionWhenBrandDoesNotExistForProductSearch() {

        // Arrange
        Long brandId = 99L;

        when(brandRepository.existsById(brandId)).thenReturn(false);

        // Act & Assert
        assertThrows(
                BrandNotFoundException.class,
                () -> productService.getAllProductsByBrandId(brandId)
        );

        verify(productRepository, never())
                .findProductsByBrandId(anyLong());
        verifyNoInteractions(productMapper);
    }

    @Test
    void shouldReturnEmptyListWhenBrandHasNoProducts() {

        Long brandId = 1L;

        when(brandRepository.existsById(brandId)).thenReturn(true);
        when(productRepository.findProductsByBrandId(brandId))
                .thenReturn(List.of());

        List<ProductDto> result =
                productService.getAllProductsByBrandId(brandId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verifyNoInteractions(productMapper);
    }


    @Test
    void shouldUpdateProductSuccessfully() {

        // Arrange
        Long productId = 10L;

        Product existingProduct = Product.builder()
                .id(productId)
                .name("Old Headphones")
                .description("Old description")
                .brandId(1L)
                .slug("old-headphones")
                .status(ProductStatus.DRAFT)
                .build();

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Wireless Headphones");
        request.setDescription("Updated description");
        request.setBrandId(2L);

        ProductDto expectedDto = new ProductDto();
        expectedDto.setId(productId);
        expectedDto.setName("Wireless Headphones");
        expectedDto.setDescription("Updated description");
        expectedDto.setBrandId(2L);
        expectedDto.setSlug("wireless-headphones");
        expectedDto.setStatus(ProductStatus.DRAFT);

        when(productRepository.findById(productId))
                .thenReturn(java.util.Optional.of(existingProduct));
        when(slugGenerator.generate("Wireless Headphones"))
                .thenReturn("wireless-headphones");
        when(brandRepository.existsById(2L))
                .thenReturn(true);
        when(productRepository.existsBySlugAndIdNot(
                "wireless-headphones", productId))
                .thenReturn(false);
        when(productRepository.save(existingProduct))
                .thenReturn(existingProduct);
        when(productMapper.toDto(existingProduct))
                .thenReturn(expectedDto);

        // Act
        ProductDto result =
                productService.updateProduct(productId, request);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Wireless Headphones", result.getName());
        assertEquals("Updated description", result.getDescription());
        assertEquals(2L, result.getBrandId());
        assertEquals("wireless-headphones", result.getSlug());
        assertEquals(ProductStatus.DRAFT, result.getStatus());

        assertEquals("Wireless Headphones", existingProduct.getName());
        assertEquals("Updated description", existingProduct.getDescription());
        assertEquals(2L, existingProduct.getBrandId());
        assertEquals("wireless-headphones", existingProduct.getSlug());
        assertEquals(ProductStatus.DRAFT, existingProduct.getStatus());

        verify(productRepository).save(existingProduct);
        verify(productMapper).toDto(existingProduct);
    }

    @Test
    void shouldThrowExceptionWhenProductToUpdateDoesNotExist() {

        Long productId = 999L;

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Wireless Headphones");
        request.setBrandId(1L);

        when(productRepository.findById(productId))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(productId, request)
        );

        verifyNoInteractions(brandRepository, productMapper, slugGenerator);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatedBrandDoesNotExist() {

        Long productId = 10L;

        Product product = Product.builder()
                .id(productId)
                .name("Old Headphones")
                .brandId(1L)
                .slug("old-headphones")
                .status(ProductStatus.DRAFT)
                .build();

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Wireless Headphones");
        request.setBrandId(99L);

        when(productRepository.findById(productId))
                .thenReturn(java.util.Optional.of(product));
        when(slugGenerator.generate("Wireless Headphones"))
                .thenReturn("wireless-headphones");
        when(brandRepository.existsById(99L))
                .thenReturn(false);

        assertThrows(
                BrandNotFoundException.class,
                () -> productService.updateProduct(productId, request)
        );

        verify(productRepository, never()).save(any(Product.class));
        verify(productRepository, never())
                .existsBySlugAndIdNot(anyString(), anyLong());
        verifyNoInteractions(productMapper);
    }

    @Test
    void shouldThrowExceptionWhenUpdatedSlugAlreadyExists() {

        Long productId = 10L;

        Product product = Product.builder()
                .id(productId)
                .name("Old Headphones")
                .brandId(1L)
                .slug("old-headphones")
                .status(ProductStatus.DRAFT)
                .build();

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Wireless Headphones");
        request.setBrandId(1L);

        when(productRepository.findById(productId))
                .thenReturn(java.util.Optional.of(product));
        when(slugGenerator.generate("Wireless Headphones"))
                .thenReturn("wireless-headphones");
        when(brandRepository.existsById(1L))
                .thenReturn(true);
        when(productRepository.existsBySlugAndIdNot(
                "wireless-headphones", productId))
                .thenReturn(true);

        assertThrows(
                DuplicateProductSlugException.class,
                () -> productService.updateProduct(productId, request)
        );

        verify(productRepository, never()).save(any(Product.class));
        verifyNoInteractions(productMapper);
    }

    @Test
    void shouldThrowExceptionWhenUpdatedSlugIsInvalid() {

        Long productId = 10L;

        Product product = Product.builder()
                .id(productId)
                .name("Old Headphones")
                .brandId(1L)
                .slug("old-headphones")
                .status(ProductStatus.DRAFT)
                .build();

        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("!!!");
        request.setBrandId(1L);

        when(productRepository.findById(productId))
                .thenReturn(java.util.Optional.of(product));
        when(slugGenerator.generate("!!!"))
                .thenReturn("");

        assertThrows(
                InvalidProductSlugException.class,
                () -> productService.updateProduct(productId, request)
        );

        verifyNoInteractions(brandRepository);
        verify(productRepository, never()).save(any(Product.class));
        verifyNoInteractions(productMapper);
    }

    @Test
    void shouldDeleteProductWhenItExists() {

        // Arrange
        Long productId = 10L;

        Product product = Product.builder()
                .id(productId)
                .name("Wireless Headphones")
                .brandId(1L)
                .slug("wireless-headphones")
                .status(ProductStatus.DRAFT)
                .build();

        when(productRepository.findById(productId))
                .thenReturn(java.util.Optional.of(product));

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(productRepository).findById(productId);
        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowExceptionWhenProductToDeleteDoesNotExist() {

        // Arrange
        Long productId = 999L;

        when(productRepository.findById(productId))
                .thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteProduct(productId)
        );

        verify(productRepository, never()).delete(any(Product.class));
    }


    @Test
    void shouldReturnProductsForExistingCategory() {

        // Arrange
        Long categoryId = 5L;

        Product product = Product.builder()
                .id(10L)
                .name("Wireless Headphones")
                .brandId(1L)
                .slug("wireless-headphones")
                .status(ProductStatus.DRAFT)
                .build();

        ProductDto dto = new ProductDto();
        dto.setId(10L);
        dto.setName("Wireless Headphones");
        dto.setBrandId(1L);

        when(categoryRepository.existsById(categoryId))
                .thenReturn(true);

        when(productRepository.findAllByCategoryId(categoryId))
                .thenReturn(List.of(product));

        when(productMapper.toDto(product))
                .thenReturn(dto);

        // Act
        List<ProductDto> result =
                productService.getAllProductsByCategoryId(categoryId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
        assertEquals("Wireless Headphones", result.get(0).getName());

        verify(categoryRepository).existsById(categoryId);
        verify(productRepository).findAllByCategoryId(categoryId);
        verify(productMapper).toDto(product);
    }

    @Test
    void shouldReturnEmptyListWhenCategoryHasNoProducts() {

        // Arrange
        Long categoryId = 5L;

        when(categoryRepository.existsById(categoryId))
                .thenReturn(true);

        when(productRepository.findAllByCategoryId(categoryId))
                .thenReturn(List.of());

        // Act
        List<ProductDto> result =
                productService.getAllProductsByCategoryId(categoryId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(categoryRepository).existsById(categoryId);
        verify(productRepository).findAllByCategoryId(categoryId);
        verifyNoInteractions(productMapper);
    }

    @Test
    void shouldThrowExceptionWhenCategoryDoesNotExistForProductSearch() {

        // Arrange
        Long categoryId = 99L;

        when(categoryRepository.existsById(categoryId))
                .thenReturn(false);

        // Act & Assert
        assertThrows(
                CategoryNotFoundException.class,
                () -> productService.getAllProductsByCategoryId(categoryId)
        );

        verify(categoryRepository).existsById(categoryId);
        verify(productRepository, never())
                .findAllByCategoryId(anyLong());
        verifyNoInteractions(productMapper);
    }

}