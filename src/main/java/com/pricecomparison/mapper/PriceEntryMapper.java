package com.pricecomparison.mapper;

import com.pricecomparison.dto.PriceEntryDto;
import com.pricecomparison.model.PriceEntry;
import org.springframework.stereotype.Service;

@Service
public class PriceEntryMapper {
    public PriceEntryDto map(final PriceEntry priceEntry) {
        return new PriceEntryDto(
                priceEntry.getId(),
                priceEntry.getDate(),
                priceEntry.getPrice(),
                priceEntry.getProduct().getId(),
                priceEntry.isPresent()
        );
    }
}
