package com.pricecomparison.payload.request.update;

public record UpdateProductRequest(
        String title,
        Long categoryId,
        String EAN
) {}
