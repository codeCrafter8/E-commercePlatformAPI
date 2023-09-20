package com.ecommerce.repository;

import com.ecommerce.model.Offer;
import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findAllByProduct(Product product);
    Optional<Offer> findBySourceAndSourceOfferId(String source, String sourceOfferId);
}
