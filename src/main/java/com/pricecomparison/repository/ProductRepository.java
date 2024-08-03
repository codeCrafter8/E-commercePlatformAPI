package com.pricecomparison.repository;

import com.pricecomparison.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByEAN(String EAN);
    @Query("SELECT p.EAN FROM Product p")
    List<String> findAllEANs();

}
