package com.ecommerce.payload.request.update;

public record UpdateProductRequest(
        String title,
        Float price,
        String description,
        Long categoryId,
        String state
) {
}
