package com.pricecomparison.service;

import com.pricecomparison.dto.OfferDto;
import com.pricecomparison.exception.ResourceNotFoundException;
import com.pricecomparison.mapper.OfferMapper;
import com.pricecomparison.model.Offer;
import com.pricecomparison.model.Product;
import com.pricecomparison.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final ProductService productService;
    private final OfferMapper offerMapper;
    public List<OfferDto> getAllOffersByProductId(Long productId) {
        List<Offer> offers = offerRepository.findAllByProductId(productId);

        /*List<OfferDto> offers2 = offers.stream()
                .sorted(Comparator.comparingDouble(Offer::getPrice))
                .map(offerMapper::map)
                .collect(Collectors.toList());

        Optional<OfferDto> offer = offers2.stream().findFirst();

        System.out.println(offer.get().price());

        return offers2;*/

        return offers.stream()
                .sorted(Comparator.comparingDouble(Offer::getPrice))
                .map(offerMapper::map)
                .collect(Collectors.toList());
    }

    public void createOrUpdateOffer(String source, String sourceOfferId, Float price, String EAN) {
        Optional<Offer> existingOffer = offerRepository.findBySourceAndSourceOfferId(source, sourceOfferId);
        Offer offer;

        if(existingOffer.isEmpty()) {
            Product product = productService.getProductByEAN(EAN);

            offer = new Offer(
                    source,
                    sourceOfferId,
                    price,
                    product
            );
        }
        else {
            offer = existingOffer.get();
            if(!offer.getPrice().equals(price)) {
                offer.setPrice(price);
            }
        }

        offerRepository.save(offer);
    }

    /*public Float getMinOfferPrice() {
        Offer offerWithMinPrice = offerRepository.findTopByOrderByPriceAsc()
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Offer not found")
                );

        getAllOffersByProductId(1L).stream().findFirst();
        return offerWithMinPrice.getPrice();
    }*/
}
