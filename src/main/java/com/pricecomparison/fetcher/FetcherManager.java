package com.pricecomparison.fetcher;

import com.pricecomparison.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FetcherManager {

    private final AmazonFetcher amazonFetcher;
    private final ProductRepository productRepository;
    private final EbayFetcher ebayFetcher;
    private final MoreleFetcher moreleFetcher;

    public void fetchAll() {
        List<String> productEANs = productRepository.findAllEANs();

        for(String EAN : productEANs) {
            ebayFetcher.fetch(EAN);
            amazonFetcher.fetch(EAN);
            moreleFetcher.fetch(EAN);
        }
    }
}
