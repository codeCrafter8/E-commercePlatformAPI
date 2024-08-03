package com.pricecomparison.dto;

import java.time.LocalDate;

public record PriceEntryDto(
        Long id,
        LocalDate date,
        Float price,
        Long productId,
        boolean present
) {}
