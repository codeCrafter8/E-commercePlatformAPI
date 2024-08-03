package com.pricecomparison.service;

import com.pricecomparison.dto.PriceEntryDto;
import com.pricecomparison.mapper.PriceEntryMapper;
import com.pricecomparison.model.PriceEntry;
import com.pricecomparison.repository.PriceEntryRepository;
import com.pricecomparison.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PriceEntryService {

    private final PriceEntryRepository priceEntryRepository;
    private final PriceEntryMapper priceEntryMapper;
    private final ProductRepository productRepository;

    public void createPriceEntries() {
        PriceEntry priceEntry1 = new PriceEntry(
                LocalDate.of(2023, 10, 4),
                15.99F,
                productRepository.findById(1L).get(),
                true
        );

        PriceEntry priceEntry2 = new PriceEntry(
                LocalDate.of(2023, 10, 2),
                20.99F,
                productRepository.findById(1L).get(),
                false
        );

        PriceEntry priceEntry3 = new PriceEntry(
                LocalDate.of(2023, 10, 3),
                16.99F,
                productRepository.findById(1L).get(),
                true
        );

        PriceEntry priceEntry4 = new PriceEntry(
                LocalDate.of(2023, 10, 1),
                15.99F,
                productRepository.findById(1L).get(),
                true
        );

        PriceEntry priceEntry5 = new PriceEntry(
                LocalDate.of(2023, 9, 30),
                15.99F,
                productRepository.findById(1L).get(),
                true
        );

        PriceEntry priceEntry6 = new PriceEntry(
                LocalDate.of(2023, 9, 28),
                15.99F,
                productRepository.findById(1L).get(),
                true
        );

        PriceEntry priceEntry7 = new PriceEntry(
                LocalDate.of(2023, 9, 29),
                17.99F,
                productRepository.findById(1L).get(),
                true
        );

        PriceEntry priceEntry8 = new PriceEntry(
                LocalDate.of(2023, 9, 27),
                22.99F,
                productRepository.findById(1L).get(),
                true
        );

        PriceEntry priceEntry9 = new PriceEntry(
                LocalDate.of(2023, 9, 26),
                17.99F,
                productRepository.findById(1L).get(),
                true
        );

        PriceEntry priceEntry10 = new PriceEntry(
                LocalDate.of(2023, 9, 25),
                17.99F,
                productRepository.findById(1L).get(),
                true
        );

        priceEntryRepository.save(priceEntry1);
        priceEntryRepository.save(priceEntry2);
        priceEntryRepository.save(priceEntry3);
        priceEntryRepository.save(priceEntry4);
        priceEntryRepository.save(priceEntry5);
        priceEntryRepository.save(priceEntry6);
        priceEntryRepository.save(priceEntry7);
        priceEntryRepository.save(priceEntry8);
        priceEntryRepository.save(priceEntry9);
        priceEntryRepository.save(priceEntry10);
    }

    public List<PriceEntryDto> getAllPriceEntries() {
        List<PriceEntry> priceEntries = priceEntryRepository.findAll();
        return priceEntries.stream().map(priceEntryMapper::map)
                .collect(Collectors.toList());
    }

    public List<PriceEntryDto> getAllPriceEntriesByProductId(Long productId) {
        List<PriceEntry> priceEntries = priceEntryRepository.findAllByProductId(productId);
        return priceEntries.stream().map(priceEntryMapper::map)
                .collect(Collectors.toList());
    }
}
