package com.ecommerce.fetcher;

import com.ecommerce.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AmazonFetcher {
    private final OfferService offerService;
    public void fetch(String EAN) {
        //TODO: jak nie po ean to po nazwie?
        //todo: co jak produkt w amazonie nie istnieje
        String url = "https://www.amazon.pl/s/ref=sr_st_featured-rank?keywords=" + EAN;
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:49.0) Gecko/20100101 Firefox/49.0").ignoreHttpErrors(true).followRedirects(true).timeout(100000).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements elements = document.select("span.a-price-whole");
        if(!elements.isEmpty()) {
            Element priceWholeElement = elements.first();
            Element priceFractionElement = document.select("span.a-price-fraction").first();
            String priceText = priceWholeElement.text().substring(0, priceWholeElement.text().length() - 1) +
                    "." + priceFractionElement.text();
            Float price = Float.valueOf(priceText);

            offerService.createOffer("Amazon", price, EAN);
        }
    }
}
