package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long id;
    @NotBlank
    private String token;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "app_user_id",
            nullable = false
    )
    private AppUser appUser;
    @NotNull
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    @NotNull
    private LocalDateTime expiresAt;
    public ConfirmationToken(
            String token,
            AppUser appUser,
            LocalDateTime createdAt,
            LocalDateTime expiresAt
    ) {
        this.token = token;
        this.appUser = appUser;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}
