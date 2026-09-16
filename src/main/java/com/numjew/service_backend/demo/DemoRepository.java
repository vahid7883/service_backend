package com.numjew.service_backend.demo;

import com.numjew.service_backend.demo.DemoDto;
import com.numjew.service_backend.demo.Demo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DemoRepository extends JpaRepository<Demo, Long> {
    Optional<DemoDto> findOneById(Long id);
    List<Demo> findByUserId(Long userId);
}
