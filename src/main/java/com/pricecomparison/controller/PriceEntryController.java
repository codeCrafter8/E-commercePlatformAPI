package com.pricecomparison.controller;

import com.pricecomparison.dto.PriceEntryDto;
import com.pricecomparison.service.PriceEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/price-entries")
public class PriceEntryController {

    private final PriceEntryService priceEntryService;

    @GetMapping
    public ResponseEntity<List<PriceEntryDto>> getAllPriceEntries() {
        List<PriceEntryDto> priceEntries = priceEntryService.getAllPriceEntries();
        return new ResponseEntity<>(priceEntries, HttpStatus.OK);
    }
}
