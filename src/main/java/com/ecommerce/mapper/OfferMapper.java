package com.ecommerce.mapper;

import com.ecommerce.dto.OfferDto;
import com.ecommerce.model.Offer;

public class OfferMapper {
    public static OfferDto map(final Offer offer) {
        return new OfferDto(
                offer.getId(),
                offer.getSource(),
                offer.getPrice()
        );
    }
}
