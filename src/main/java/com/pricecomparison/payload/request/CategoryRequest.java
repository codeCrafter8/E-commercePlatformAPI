package com.pricecomparison.payload.request;

public record CategoryRequest(
        String title,
        Long parentId
) {
}
