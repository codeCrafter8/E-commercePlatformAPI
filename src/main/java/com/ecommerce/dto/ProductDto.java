package com.ecommerce.dto;

public record ProductDto(
        Long id,
        String title,
        Long categoryId,
        String EAN
) {
}
