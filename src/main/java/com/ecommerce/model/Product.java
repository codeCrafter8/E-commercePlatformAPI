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
    /*@ManyToOne(
            fetch = FetchType.EAGER,
            optional = false,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;*/
    @NotBlank
    @Size(min = 3, message = "Title must contains at least 3 characters.")
    private String title;
    /*@NotNull
    private Float price;*/
    /*@NotBlank
    @Size(min = 5, message = "Description must contains at least 5 characters.")
    private String description;*/
    @ManyToOne(
            optional = false,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @NotBlank(message = "ean")
    //TODO: unique
    private String EAN;
    /*@Enumerated(EnumType.STRING)
    private ProductState state;*/
    @ManyToMany(mappedBy = "favoriteProducts")
    private Set<AppUser> favoredBy;
    /*@OneToOne(mappedBy = "product")
    private Order order;*/
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private Set<Offer> offers;

    //TODO: shipment size?
    public Product(
            String title,
            Category category,
            String EAN
    ){
        this.title = title;
        this.category = category;
        this.EAN = EAN;
    }
}
