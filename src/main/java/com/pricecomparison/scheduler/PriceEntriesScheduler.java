package com.pricecomparison.scheduler;

import com.pricecomparison.service.PriceEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceEntriesScheduler {

    private final PriceEntryService priceEntryService;

    @Scheduled(cron = "0 2 0 * * *")
    public void createPriceEntries() {
        priceEntryService.createPriceEntries();
    }
}
