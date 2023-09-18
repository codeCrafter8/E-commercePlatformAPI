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
public class MoreleFetcher {
    private final OfferService offerService;
    public void fetch(String EAN) {
        //TODO: jak nie po ean to po nazwie?
        //todo: co jak produkt w amazonie nie istnieje
        String url = "https://www.morele.net/wyszukiwarka/?q=" + EAN;
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements elements = document.select("div.price-new");
        if(!elements.isEmpty()) {
            String priceText = elements.first().text();
            priceText = priceText.substring(0, priceText.length() - 3);
            priceText = priceText.replace(",", ".");

            Float price = Float.valueOf(priceText);

            offerService.createOffer("Morele", price, EAN);
        }
    }
}
