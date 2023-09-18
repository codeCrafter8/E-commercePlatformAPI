package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Entity
public class Offer {
    @SequenceGenerator(
            name = "offer_sequence",
            sequenceName = "offer_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "offer_sequence"
    )
    private Long id;
    private String shopName;
    private Float price;
    //TODO: Many to one optional = false?
    @ManyToOne(
            optional = false,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    public Offer(
            String shopName,
            Float price,
            Product product
    ) {
        this.shopName = shopName;
        this.price = price;
        this.product = product;
    }

    @Override
    public String toString() {
        return this.id + " " + this.shopName + " " + this.price;
    }
}
