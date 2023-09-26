package com.pricecomparison.payload.request;

public record AuthenticationRequest(
        String username,
        String password
) {
}
