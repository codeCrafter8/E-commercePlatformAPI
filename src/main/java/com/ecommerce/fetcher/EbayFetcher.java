package com.ecommerce.fetcher;

import com.ecommerce.service.OfferService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class EbayFetcher {
    private final OfferService offerService;
    @Value("${ebayAppId}")
    private String appId;
    public void fetch(String EAN) {
        String url = "https://svcs.ebay.com/services/search/FindingService/v1?" +
                "OPERATION-NAME=findItemsAdvanced" +
                "&SERVICE-VERSION=1.0.0" +
                "&SECURITY-APPNAME=" + appId +
                "&RESPONSE-DATA-FORMAT=JSON" +
                "&REST-PAYLOAD" +
                "&keywords=" + EAN;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-EBAY-SOA-GLOBAL-ID", "EBAY-PL")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        List<Float> prices = getPrice(response.body());
        for(Float price : prices) {
            System.out.println(price);
            offerService.createOffer("Ebay", price, EAN);
        }
    }

    private List<Float> getPrice(String responseBody) {
        List<Float> prices = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray findItemsAdvancedResponse = jsonObject.getJSONArray("findItemsAdvancedResponse");
            JSONArray searchResult = findItemsAdvancedResponse.getJSONObject(0).getJSONArray("searchResult");
            JSONArray items = searchResult.getJSONObject(0).getJSONArray("item");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONArray sellingStatus = item.getJSONArray("sellingStatus");
                JSONObject sellingStatusObj = sellingStatus.getJSONObject(0);
                JSONArray convertedCurrentPrice = sellingStatusObj.getJSONArray("convertedCurrentPrice");
                JSONObject convertedPriceObj = convertedCurrentPrice.getJSONObject(0);
                String convertedCurrentPriceValue = convertedPriceObj.getString("__value__");

                prices.add(Float.valueOf(convertedCurrentPriceValue));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prices;
    }
}
