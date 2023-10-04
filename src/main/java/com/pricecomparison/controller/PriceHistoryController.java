package com.pricecomparison.controller;

import com.pricecomparison.dto.PriceHistoryDto;
import com.pricecomparison.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/price-history")
public class PriceHistoryController {
    private final PriceHistoryService priceHistoryService;
    @GetMapping
    public ResponseEntity<List<PriceHistoryDto>> getPriceHistoryList() {
        List<PriceHistoryDto> priceHistoryList = priceHistoryService.getPriceHistoryList();
        return new ResponseEntity<>(priceHistoryList, HttpStatus.OK);
    }
}
