package com.numjew.service_backend.brand.repository;

import com.numjew.service_backend.brand.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
