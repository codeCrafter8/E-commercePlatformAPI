package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Company {
    @SequenceGenerator(
            name = "company_sequence",
            sequenceName = "company_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "company_sequence"
    )
    private Long id;
    @Column(
            nullable = false
    )
    String companyCountry;
    @Column(
            nullable = false
    )
    String taxNumber;

    public Company(String companyCountry, String taxNumber) {
        this.companyCountry = companyCountry;
        this.taxNumber = taxNumber;
    }
}
