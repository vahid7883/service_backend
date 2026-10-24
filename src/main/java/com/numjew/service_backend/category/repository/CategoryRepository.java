
package com.numjew.service_backend.category.repository;

import com.numjew.service_backend.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}