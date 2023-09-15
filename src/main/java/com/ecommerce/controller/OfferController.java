package com.ecommerce.controller;

import com.ecommerce.dto.OfferDto;
import com.ecommerce.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/products")
public class OfferController {
    private final OfferService offerService;
    @GetMapping("{productId}/offers")
    public ResponseEntity<List<OfferDto>> getAllOffers(@PathVariable Long productId) {
        List<OfferDto> offers = offerService.getAllOffersByProductId(productId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }
}
