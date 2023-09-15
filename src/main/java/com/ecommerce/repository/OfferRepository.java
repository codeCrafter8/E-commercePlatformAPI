package com.ecommerce.repository;

import com.ecommerce.model.Offer;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findAllByProduct(Product product);
}
