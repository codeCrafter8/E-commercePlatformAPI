package com.ecommerce.payload.request;

public record AuthenticationRequest(
        String username,
        String password
) {
}
