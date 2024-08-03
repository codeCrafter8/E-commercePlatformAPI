package com.pricecomparison.payload.request.create;

public record CreateAppUserRequest(
        String firstName,
        String lastName,
        String username,
        String email,
        String password
) {}
