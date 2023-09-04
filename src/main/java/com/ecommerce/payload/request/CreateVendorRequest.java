package com.ecommerce.payload.request;

public record CreateVendorRequest(
        String companyCountry,
        String taxNumber,
        String email,
        String username,
        String password,
        String phoneNumber
) {
}
