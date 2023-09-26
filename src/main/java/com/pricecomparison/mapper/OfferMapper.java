package com.pricecomparison.mapper;

import com.pricecomparison.dto.OfferDto;
import com.pricecomparison.model.Offer;

public class OfferMapper {
    public static OfferDto map(final Offer offer) {
        return new OfferDto(
                offer.getId(),
                offer.getSource(),
                offer.getPrice()
        );
    }
}
