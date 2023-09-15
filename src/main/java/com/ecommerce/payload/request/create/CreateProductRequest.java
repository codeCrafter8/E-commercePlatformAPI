package com.ecommerce.payload.request.create;

public record CreateProductRequest(
        String title,
        Long categoryId,
        String EAN
) {
}
