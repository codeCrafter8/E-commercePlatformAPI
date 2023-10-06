package com.pricecomparison.repository;

import com.pricecomparison.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findAllByProductId(Long id);
    Optional<Offer> findBySourceAndSourceOfferId(String source, String sourceOfferId);
    List<Offer> findBySourceAndSourceOfferIdNotIn(String source, List<String> sourceOfferIds);
}
