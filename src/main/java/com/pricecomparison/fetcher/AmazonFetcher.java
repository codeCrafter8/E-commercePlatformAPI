package com.pricecomparison.fetcher;

import com.pricecomparison.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class AmazonFetcher {
    private final OfferService offerService;
    public void fetch(String EAN) {
        String url = "https://www.amazon.pl/s?k=" + EAN;

        Document document;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                    .ignoreHttpErrors(true)
                    .followRedirects(true)
                    .timeout(100000)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String source = "Amazon";
        List<String> sourceOfferIds = new ArrayList<>();

        Elements elements = document.select("span.a-price-whole");
        if(!elements.isEmpty()) {
            Element priceWholeElement = elements.first();
            Element priceFractionElement = document.select("span.a-price-fraction").first();

            String reducedPriceWholeElementText = priceWholeElement != null ? priceWholeElement.text().substring(0, priceWholeElement.text().length() - 1) : "";
            String priceFractionElementText = priceFractionElement != null ? priceFractionElement.text() : "";

            String priceText = reducedPriceWholeElementText + "." + priceFractionElementText;
            Float price = Float.valueOf(priceText);

            String sourceOfferId = document.getElementsByClass("sg-col-4-of-24 sg-col-4-of-12 s-result-item s-asin sg-col-4-of-16 sg-col s-widget-spacing-small sg-col-4-of-20").attr("data-asin");
            sourceOfferIds.add(sourceOfferId);

            offerService.createOrUpdateOffer(source, sourceOfferId, price, EAN);
        }

        offerService.deleteAbsentOffers(source, sourceOfferIds);
    }
}
