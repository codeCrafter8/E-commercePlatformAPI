package com.pricecomparison.mapper;

import com.pricecomparison.dto.OfferDto;
import com.pricecomparison.dto.PriceHistoryDto;
import com.pricecomparison.model.Offer;
import com.pricecomparison.model.PriceHistory;
import org.springframework.stereotype.Service;

@Service
public class PriceHistoryMapper {
    public PriceHistoryDto map(final PriceHistory priceHistory) {
        return new PriceHistoryDto(
                priceHistory.getId(),
                priceHistory.getDate(),
                priceHistory.getPrice(),
                priceHistory.getProduct().getId()
        );
    }
}
