package com.pricecomparison.repository;

import com.pricecomparison.model.Category;
import com.pricecomparison.model.PriceEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceEntryRepository extends JpaRepository<PriceEntry, Long> {
    List<PriceEntry> findAllByProductId(Long id);
    Optional<PriceEntry> findByDate(LocalDate date);
}
