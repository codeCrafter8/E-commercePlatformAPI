package com.ecommerce.dto;

public record VendorDto(
    Long id,
    String email,
    String username,
    String phoneNumber,
    String companyCountry,
    String taxNumber
) {
}
