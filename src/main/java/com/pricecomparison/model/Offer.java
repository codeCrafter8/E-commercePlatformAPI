package com.pricecomparison.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
    @NotBlank
    private String source;
    @NotBlank
    private String sourceOfferId;
    private Float price;
    //TODO: Many to one optional = false?
    @ManyToOne(
            optional = false,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "product_id",
            nullable = false
    )
    private Product product;
    public Offer(
            Long id,
            String source,
            String sourceOfferId,
            Float price,
            Product product
    ) {
        this.id = id;
        this.source = source;
        this.sourceOfferId = sourceOfferId;
        this.price = price;
        this.product = product;
    }
    public Offer(
            String source,
            String sourceOfferId,
            Float price,
            Product product
    ) {
        this.source = source;
        this.sourceOfferId = sourceOfferId;
        this.price = price;
        this.product = product;
    }
}
