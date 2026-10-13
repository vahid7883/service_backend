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
import com.numjew.service_backend.product.repository.ProductRepository;
import com.numjew.service_backend.product.mapper.ProductMapper;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final BrandRepository brandRepository;
    private final ProductSlugGenerator slugGenerator;


    @Transactional
    public ProductDto createProduct(CreateProductRequest request) {

        String name = request.getName().trim();
        String slug = slugGenerator.generate(name);

        if (slug.isBlank()) {
            throw new InvalidProductSlugException(request.getName());
        }

        if (!brandRepository.existsById(request.getBrandId())) {
            throw new BrandNotFoundException(request.getBrandId());
        }

        if (productRepository.existsBySlug(slug)) {
            throw new DuplicateProductSlugException(slug);
        }

        Product product = Product.builder()
                .name(name)
                .description(request.getDescription())
                .brandId(request.getBrandId())
                .slug(slug)
                .status(ProductStatus.DRAFT)
                .build();

        Product savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

//    public ProductDto createProduct(ProductDto productReq){
//        var product = Product.builder()
//                .name(productReq.getName())
//                .brandId(productReq.getBrandId())
//                .description(productReq.getDescription())
//                .slug(productReq.getSlug())
//                .status(productReq.getStatus())
//                .build();
//        var savedProduct = productRepository.save(product);
//        return productMapper.toDto(savedProduct);
//    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return productMapper.toDto(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAllProductsByBrandId(Long brandId) {
        if (!brandRepository.existsById(brandId)) {
            throw new BrandNotFoundException(brandId);
        }
        return  productRepository.findProductsByBrandId(brandId)
                .stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public ProductDto updateProduct(Long id, UpdateProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        String name = request.getName().trim();
        String slug = slugGenerator.generate(name);

        if (slug.isBlank()) {
            throw new InvalidProductSlugException(name);
        }

        if (!brandRepository.existsById(request.getBrandId())) {
            throw new BrandNotFoundException(request.getBrandId());
        }

        if (productRepository.existsBySlugAndIdNot(slug, id)) {
            throw new DuplicateProductSlugException(slug);
        }

        product.setName(name);
        product.setDescription(request.getDescription());
        product.setBrandId(request.getBrandId());
        product.setSlug(slug);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        productRepository.delete(product);
    }


}
