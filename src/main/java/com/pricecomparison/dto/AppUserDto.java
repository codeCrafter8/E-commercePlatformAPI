package com.pricecomparison.dto;

public record AppUserDto(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email
) {}
