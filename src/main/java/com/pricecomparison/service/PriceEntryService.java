package com.pricecomparison.service;

import com.pricecomparison.dto.OfferDto;
import com.pricecomparison.dto.PriceEntryDto;
import com.pricecomparison.mapper.PriceEntryMapper;
import com.pricecomparison.model.PriceEntry;
import com.pricecomparison.model.Product;
import com.pricecomparison.repository.PriceEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PriceEntryService {
    private final PriceEntryRepository priceEntryRepository;
    private final ProductService productService;
    private final OfferService offerService;
    private final PriceEntryMapper priceEntryMapper;

    public void createPriceHistory(LocalDate date) {
        List<Product> products = productService.getAllProductsEntities();

        for(Product product : products) {
            List<OfferDto> offers = offerService.getAllOffersByProductId(product.getId());

            Optional<OfferDto> offerWithMinPrice = offers.stream().findFirst();

            Float minPrice = offerWithMinPrice.isPresent() ? offerWithMinPrice.get().price() : 0;

            //TODO: how to show on chart that there are no offers

            PriceEntry priceEntry = new PriceEntry(
                    date,
                    minPrice,
                    product
            );

            priceEntryRepository.save(priceEntry);
        }
    }

    public List<PriceEntryDto> getAllPriceEntries() {
        List<PriceEntry> priceEntries = priceEntryRepository.findAll();
        return priceEntries.stream().map(priceEntryMapper::map)
                .collect(Collectors.toList());
    }
}
