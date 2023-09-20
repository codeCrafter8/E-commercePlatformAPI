package com.ecommerce.service;

import com.ecommerce.dto.OfferDto;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.OfferMapper;
import com.ecommerce.model.Offer;
import com.ecommerce.model.Product;
import com.ecommerce.repository.OfferRepository;
import com.ecommerce.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    public List<OfferDto> getAllOffersByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId))
                );

        List<Offer> offers = offerRepository.findAllByProduct(product);
        return offers.stream()
                .sorted(Comparator.comparingDouble(Offer::getPrice))
                .map(OfferMapper::map)
                .collect(Collectors.toList());
    }

    public void createOrUpdateOffer(String source, String sourceOfferId, Float price, String EAN) {
        Optional<Offer> existingOffer = offerRepository.findBySourceAndSourceOfferId(source, sourceOfferId);
        Offer offer;

        if(existingOffer.isEmpty()) {
            Product product = productRepository.findByEAN(EAN)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product with EAN [%s] not found".formatted(EAN))
                    );

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
}
