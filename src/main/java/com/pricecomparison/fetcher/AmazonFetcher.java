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
        //TODO: jak nie po ean to po nazwie?
        String url = "https://www.amazon.pl/s/ref=sr_st_featured-rank?keywords=" + EAN;

        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:49.0) Gecko/20100101 Firefox/49.0").ignoreHttpErrors(true).followRedirects(true).timeout(100000).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String source = "Amazon";
        List<String> sourceOfferIds = new ArrayList<>();

        Elements elements = document.select("span.a-price-whole");
        if(!elements.isEmpty()) {
            //TODO: only first?
            Element priceWholeElement = elements.first();
            Element priceFractionElement = document.select("span.a-price-fraction").first();
            String priceText = priceWholeElement.text().substring(0, priceWholeElement.text().length() - 1) +
                    "." + priceFractionElement.text();
            Float price = Float.valueOf(priceText);

            String sourceOfferId = document.getElementsByClass("sg-col-4-of-24 sg-col-4-of-12 s-result-item s-asin sg-col-4-of-16 sg-col s-widget-spacing-small sg-col-4-of-20").attr("data-asin");
            sourceOfferIds.add(sourceOfferId);

            offerService.createOrUpdateOffer(source, sourceOfferId, price, EAN);
        }

        offerService.deleteAbsentOffers(source, sourceOfferIds);
    }
}
