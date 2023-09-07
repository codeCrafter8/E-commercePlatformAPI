package com.ecommerce.payload.request.create;

public record CreateProductRequest(
        Long appUserId,
        String title,
        Float price,
        String description,
        Long categoryId,
        String state
) {
}
