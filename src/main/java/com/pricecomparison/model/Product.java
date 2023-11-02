package com.pricecomparison.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Product {
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;
    @NotBlank
    @Size(
            min = 3,
            message = "Title must contains at least 3 characters."
    )
    private String title;
    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "category_id",
            nullable = false
    )
    private Category category;
    @NotBlank(
            message = "ean"
    )
    @Column(
            unique = true
    )
    private String EAN;
    @ManyToMany(
            mappedBy = "favoriteProducts"
    )
    private Set<AppUser> favoredBy;
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    private Set<Offer> offers;
    public Product(
            Long id,
            String title,
            Category category,
            String EAN
    ) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.EAN = EAN;
    }
    public Product(
            String title,
            Category category,
            String EAN
    ) {
        this.title = title;
        this.category = category;
        this.EAN = EAN;
    }
}
