package com.ecommerce.service;

import com.ecommerce.dto.OfferDto;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.OfferMapper;
import com.ecommerce.model.Category;
import com.ecommerce.model.Offer;
import com.ecommerce.model.Product;
import com.ecommerce.repository.OfferRepository;
import com.ecommerce.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OfferService {
    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    public List<OfferDto> getAllOffersByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId))
                );

        List<Offer> offers = offerRepository.findAllByProduct(product);
        return offers.stream().map(OfferMapper::map)
                .collect(Collectors.toList());
    }

    public void createOffer(String shopName, Float price, String EAN) {
        Product product = productRepository.findByEAN(EAN)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with EAN [%s] not found".formatted(EAN))
                );

        Offer offer = new Offer(
                shopName,
                price,
                product
        );

        offerRepository.save(offer);
    }
}
