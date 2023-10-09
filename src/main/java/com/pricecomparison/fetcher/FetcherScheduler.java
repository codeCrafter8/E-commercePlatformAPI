package com.pricecomparison.fetcher;

import com.pricecomparison.service.PriceEntryService;
import com.pricecomparison.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
        //priceHistoryService.getPriceHistory(1L);
    }

    //@Scheduled(fixedRate = 10500)
    @Scheduled(cron = "0 7 10 * * *")
    public void createPriceEntries() {
        priceEntryService.createPriceEntries(LocalDate.now());
    }
}
