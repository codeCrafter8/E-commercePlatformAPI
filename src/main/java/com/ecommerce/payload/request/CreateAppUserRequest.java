package com.ecommerce.payload.request;

public record CreateAppUserRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
