package com.pricecomparison.fetcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class AllegroFetcher {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(AllegroFetcher.class);
    private final static String FAILED_TO_SEND_REQUEST_MSG = "Failed to send HTTP request";
    @Value("${allegroToken}")
    private String token;

    public void fetch() {
        String url = "https://api.allegro.pl.allegrosandbox.pl/offers/listing?phrase=Fotel";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/vnd.allegro.public.v1+json")
                .header("Access", "application/vnd.allegro.public.v1+json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            LOGGER.error(FAILED_TO_SEND_REQUEST_MSG, e);
            throw new IllegalStateException(FAILED_TO_SEND_REQUEST_MSG);
        }
    }
}
