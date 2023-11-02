package com.pricecomparison.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Category {
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "category_sequence"
    )
    private Long id;
    //TODO: wszedzie te messages?
    @NotBlank
    @Size(
            min = 3,
            message = "Title must contains at least 3 characters."
    )
    private String title;
    private String imageURL;
    @ManyToOne
    @JoinColumn(
            name = "parent_id"
    )
    private Category parent;
    public Category(
            Long id,
            String title,
            String imageURL,
            Category parent
    ) {
        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.parent = parent;
    }
    public Category(
            String title,
            String imageURL,
            Category parent
    ) {
        this.title = title;
        this.imageURL = imageURL;
        this.parent = parent;
    }
}
