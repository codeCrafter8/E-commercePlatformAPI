package com.pricecomparison.repository;

import com.pricecomparison.model.PriceEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceEntryRepository extends JpaRepository<PriceEntry, Long> {
}
