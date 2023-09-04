package com.ecommerce.payload.request;

public record UpdateAppUserRequest(
    String firstName,
    String lastName,
    String email
) {
}
