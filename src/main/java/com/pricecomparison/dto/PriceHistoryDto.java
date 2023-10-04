package com.pricecomparison.dto;

import java.time.LocalDate;

public record PriceHistoryDto(
        Long id,
        LocalDate date,
        Float price,
        Long productId
) {
}
