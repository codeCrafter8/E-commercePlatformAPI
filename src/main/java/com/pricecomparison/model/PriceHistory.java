package com.pricecomparison.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class PriceHistory {
    @SequenceGenerator(
            name = "price_history_sequence",
            sequenceName = "price_history_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "price_history_generator"
    )
    private Long id;
    private LocalDate date;
    private Float price;
    @ManyToOne(
            optional = false
    )
    @JoinColumn(name = "product_id")
    private Product product;
    public PriceHistory(
            LocalDate date,
            Float price,
            Product product
    ) {
        this.date = date;
        this.price = price;
        this.product = product;
    }
}
