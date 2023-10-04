package com.pricecomparison.service;

import com.pricecomparison.dto.OfferDto;
import com.pricecomparison.dto.PriceHistoryDto;
import com.pricecomparison.mapper.PriceHistoryMapper;
import com.pricecomparison.model.PriceHistory;
import com.pricecomparison.model.Product;
import com.pricecomparison.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PriceHistoryService {
    private final PriceHistoryRepository priceHistoryRepository;
    private final ProductService productService;
    private final OfferService offerService;
    private final PriceHistoryMapper priceHistoryMapper;

    public void createPriceHistory(LocalDate date) {
        List<Product> products = productService.getAllProductsEntities();

        for(Product product : products) {
            List<OfferDto> offers = offerService.getAllOffersByProductId(product.getId());

            Optional<OfferDto> offerWithMinPrice = offers.stream().findFirst();

            Float minPrice = offerWithMinPrice.isPresent() ? offerWithMinPrice.get().price() : 0;

            //TODO: how to show on chart that there are no offers

            PriceHistory priceHistory = new PriceHistory(
                    date,
                    minPrice,
                    product
            );

            priceHistoryRepository.save(priceHistory);
        }
    }

    public List<PriceHistoryDto> getPriceHistoryList() {
        List<PriceHistory> priceHistoryList = priceHistoryRepository.findAll();
        return priceHistoryList.stream().map(priceHistoryMapper::map)
                .collect(Collectors.toList());
    }
}
