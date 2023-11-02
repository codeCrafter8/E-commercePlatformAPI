package com.pricecomparison.fetcher;

import com.pricecomparison.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class MoreleFetcher {
    private final OfferService offerService;
    public void fetch(String EAN) {
        String url = "https://www.morele.net/wyszukiwarka/?q=" + EAN;

        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String source = "Morele";
        List<String> sourceOfferIds = new ArrayList<>();

        if(!isOfferDisabled(document)) {
            Elements elements = document.select("div.price-new");
            if (!elements.isEmpty()) {
                String priceText = Objects.requireNonNull(elements.first()).text();
                priceText = priceText.substring(0, priceText.length() - 3);
                priceText = priceText.replace(",", ".");

                Float price = Float.valueOf(priceText);

                String sourceOfferId = document.getElementsByClass("cat-product card").attr("data-product-id");
                sourceOfferIds.add(sourceOfferId);

                offerService.createOrUpdateOffer(source, sourceOfferId, price, EAN);
            }
        }

        offerService.deleteAbsentOffers(source, sourceOfferIds, EAN);
    }

    private boolean isOfferDisabled(Document document) {
        Elements elements = document.select("div.disabled-product-info");
        return !elements.isEmpty();
    }
}
