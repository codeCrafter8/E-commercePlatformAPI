package com.ecommerce.payload.request.update;

public record UpdateProductRequest(
        String title,
        Long categoryId,
        String EAN
) {
}
