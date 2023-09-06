package com.ecommerce.payload.request.create;

public record CreateProductRequest(
        String username,
        String title,
        float price,
        String description,
        String category,
        String state
) {
}
