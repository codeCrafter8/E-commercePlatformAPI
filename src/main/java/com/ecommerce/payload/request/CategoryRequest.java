package com.ecommerce.payload.request;

public record CategoryRequest(
        String title,
        Long parentId
) {
}
