package com.pricecomparison.dto;

public record CategoryDto(
        Long id,
        String title,
        Long parentId
) {
}
