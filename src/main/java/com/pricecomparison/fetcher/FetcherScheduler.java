package com.pricecomparison.fetcher;

import com.pricecomparison.service.PriceEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class FetcherScheduler {
    private final FetcherManager fetcherManager;
    private final PriceEntryService priceEntryService;

    @Scheduled(fixedRate = 10000)
    //@Scheduled(cron = "0 0 * * * *")
    public void fetchOffers() {
        fetcherManager.fetchAll();
    }

    @Scheduled(fixedRate = 10500)
    //@Scheduled(cron = "0 1 0 * * *")
    public void createPriceHistory() {
        priceEntryService.createPriceHistory(LocalDate.now());
    }
}
