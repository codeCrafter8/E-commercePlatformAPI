package com.ecommerce.payload.request.update;

import com.ecommerce.enumeration.ProductState;

public record UpdateProductRequest(
        String title,
        float price,
        String description,
        String category,
        String state
) {
}
