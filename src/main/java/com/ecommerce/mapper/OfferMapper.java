package com.ecommerce.mapper;

import com.ecommerce.dto.OfferDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Offer;
import com.ecommerce.model.Product;

public class OfferMapper {
    public static OfferDto map(final Offer offer) {
        return new OfferDto(
                offer.getId(),
                offer.getShopName(),
                offer.getPrice()
        );
    }
}
