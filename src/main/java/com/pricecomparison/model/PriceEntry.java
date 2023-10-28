package com.pricecomparison.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class PriceEntry {
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
    @JoinColumn(
            name = "product_id"
    )
    private Product product;
    private boolean present;
    public PriceEntry(
            LocalDate date,
            Float price,
            Product product,
            boolean present
    ) {
        this.date = date;
        this.price = price;
        this.product = product;
        this.present = present;
    }
}
