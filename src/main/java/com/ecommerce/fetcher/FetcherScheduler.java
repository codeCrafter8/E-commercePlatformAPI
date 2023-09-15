package com.ecommerce.fetcher;

import com.ecommerce.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FetcherScheduler {
    private final FetcherManager fetcherManager;

    @Scheduled(fixedRate = 10000)
    public void startGettingOffers() {
        fetcherManager.fetchAll();
    }
}
