package com.pricecomparison.scheduler;

import com.pricecomparison.fetcher.FetcherManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FetchScheduler {

    private final FetcherManager fetcherManager;

    @Scheduled(fixedRate = 10000)
    public void fetchOffers() {
        fetcherManager.fetchAll();
    }

}
