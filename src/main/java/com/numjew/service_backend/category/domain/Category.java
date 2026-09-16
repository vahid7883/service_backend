package com.numjew.service_backend.category.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;



@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;


        @NotBlank
        @Column(nullable = false)
        private String name;


        @NotBlank
        @Column(nullable = false, unique = true)
        private String slug;

        @Enumerated(EnumType.STRING)
        @Builder.Default
        private CategoryStatus status = CategoryStatus.ACTIVE;


        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "parent_id")
        private Category parent;

    }


