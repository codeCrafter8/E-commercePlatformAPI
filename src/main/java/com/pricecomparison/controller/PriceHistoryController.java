package com.pricecomparison.controller;

import com.pricecomparison.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/price-history")
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    @GetMapping(
            value = "{productId}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public ResponseEntity<byte[]> getPriceHistory(@PathVariable Long productId) throws IOException {
        priceHistoryService.createPriceHistory(productId);

        var imgFile = new ClassPathResource("image/Price-Line-Chart.jpg");
        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

        return new ResponseEntity<>(bytes, HttpStatus.OK);
    }
}
