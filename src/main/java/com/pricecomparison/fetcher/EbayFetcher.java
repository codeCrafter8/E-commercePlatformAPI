package com.pricecomparison.fetcher;

import com.pricecomparison.service.OfferService;
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        List<String> sourceOfferIds = new ArrayList<>();

        if(response != null && getCount(response.body()) > 0) {
            JSONArray items = getItems(response.body());
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                Float price = getPrice(item);

                String sourceOfferId = getSourceOfferId(item);
                sourceOfferIds.add(sourceOfferId);

                offerService.createOrUpdateOffer("Ebay", sourceOfferId, price, EAN);
            }
        }

        offerService.deleteAbsentOffers("Ebay", sourceOfferIds);
    }

    private Float getPrice(JSONObject item) {
        JSONArray sellingStatus = item.getJSONArray("sellingStatus");
        JSONObject sellingStatusObj = sellingStatus.getJSONObject(0);
        JSONArray convertedCurrentPrice = sellingStatusObj.getJSONArray("convertedCurrentPrice");
        JSONObject convertedPriceObj = convertedCurrentPrice.getJSONObject(0);
        String convertedCurrentPriceValue = convertedPriceObj.getString("__value__");

        return Float.valueOf(convertedCurrentPriceValue);
    }

    private String getSourceOfferId(JSONObject item) {
        JSONArray itemId = item.getJSONArray("itemId");

        return itemId.get(0).toString();
    }

    private int getCount(String responseBody) {
        int count = 0;
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject findItemsAdvancedResponse = jsonObject.getJSONArray("findItemsAdvancedResponse").getJSONObject(0);
            JSONObject searchResult = findItemsAdvancedResponse.getJSONArray("searchResult").getJSONObject(0);
            count = Integer.parseInt(searchResult.getString("@count"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    private JSONArray getItems(String responseBody) {
        JSONArray items = null;
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray findItemsAdvancedResponse = jsonObject.getJSONArray("findItemsAdvancedResponse");
            JSONArray searchResult = findItemsAdvancedResponse.getJSONObject(0).getJSONArray("searchResult");
            items = searchResult.getJSONObject(0).getJSONArray("item");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
