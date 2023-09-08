package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(
        name = "\"order\""
)
public class Order {
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    private Long idd;
    @ManyToOne(
            fetch = FetchType.EAGER,
            optional = false,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
}
