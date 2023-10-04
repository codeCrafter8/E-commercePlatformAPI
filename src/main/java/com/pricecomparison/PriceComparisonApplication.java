package com.pricecomparison;

import com.pricecomparison.service.OfferService;
import com.pricecomparison.service.WriterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PriceComparisonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceComparisonApplication.class, args);
		WriterService writerService = new WriterService();
		writerService.write();
	}
}
