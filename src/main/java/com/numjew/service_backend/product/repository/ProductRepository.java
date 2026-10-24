package com.numjew.service_backend.product.repository;

import com.numjew.service_backend.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findProductsByBrandId(Long brandId);


    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

}
