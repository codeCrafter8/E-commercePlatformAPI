package com.pricecomparison.payload.request.update;

public record UpdateAppUserRequest(
    String firstName,
    String lastName,
    String email
) {
}
