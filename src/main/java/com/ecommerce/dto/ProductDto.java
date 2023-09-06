package com.ecommerce.dto;

public record ProductDto(
    Long id,
    String username,
    String title,
    String description,
    String category,
    String state,
    float price
) {
}
