package com.pricecomparison.mapper;

import com.pricecomparison.dto.OfferDto;
import com.pricecomparison.model.Offer;
import org.springframework.stereotype.Service;

@Service
public class OfferMapper {

    public OfferDto map(final Offer offer) {
        return new OfferDto(
                offer.getId(),
                offer.getSource(),
                offer.getPrice()
        );
    }
}
