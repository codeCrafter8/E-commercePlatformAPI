package com.ecommerce.payload.request;

public record UpdateVendorRequest(
        String companyCountry,
        String taxNumber,
        String email,
        String phoneNumber
) {
}
