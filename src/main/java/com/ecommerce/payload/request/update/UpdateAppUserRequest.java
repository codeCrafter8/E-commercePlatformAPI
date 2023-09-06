package com.ecommerce.payload.request.update;

public record UpdateAppUserRequest(
    String firstName,
    String lastName,
    String email
) {
}
