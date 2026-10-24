
package com.numjew.service_backend.product.service;

import java.text.Normalizer;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class ProductSlugGenerator {

    public String generate(String name) {
        String normalized = Normalizer.normalize(
                name.trim(),
                Normalizer.Form.NFD
        );

        return normalized
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}