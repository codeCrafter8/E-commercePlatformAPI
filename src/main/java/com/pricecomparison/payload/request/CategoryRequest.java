package com.pricecomparison.payload.request;

public record CategoryRequest(
        String title,
        String imageUrl,
        Long parentId
) {
}
