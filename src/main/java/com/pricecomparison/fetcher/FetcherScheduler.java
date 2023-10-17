package com.pricecomparison.fetcher;

import com.pricecomparison.service.PriceEntryService;
import com.pricecomparison.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FetcherScheduler {
    private final FetcherManager fetcherManager;
    private final PriceEntryService priceEntryService;
    private final PriceHistoryService priceHistoryService;

    @Scheduled(fixedRate = 10000)
    //@Scheduled(cron = "0 0 * * * *")
    public void fetchOffers() {
        fetcherManager.fetchAll();
    }

    //@Scheduled(fixedRate = 10500)
    @Scheduled(cron = "0 42 20 * * *")
    public void createPriceEntries() {
        priceEntryService.createPriceEntries();
    }
}
