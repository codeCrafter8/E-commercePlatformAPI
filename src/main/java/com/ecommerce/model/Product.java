package com.ecommerce.model;

import com.ecommerce.enumeration.ProductState;
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
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = false,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;
    //TODO: test this
    @NotBlank
    @Size(min = 3, message = "Title must contains at least 3 characters.")
    private String title;
    @NotNull
    private Float price;
    @NotBlank
    @Size(min = 5, message = "Title must contains at least 5 characters.")
    private String description;
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = false,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Enumerated(EnumType.STRING)
    private ProductState state;
    //TODO: shipment size?

    public Product(
            AppUser appUser,
            String title,
            float price,
            String description,
            Category category,
            ProductState state
    ){
        this.appUser = appUser;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.state = state;
    }
}
