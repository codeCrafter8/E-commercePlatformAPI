package com.ecommerce.dto;

import com.ecommerce.enumeration.ProductState;

public record ProductDto(
        Long id,
        Long appUserId,
        String title,
        String description,
        Long categoryId,
        ProductState state,
        Float price
) {
}
