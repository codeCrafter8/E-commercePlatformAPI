package com.ecommerce.model;

import com.ecommerce.repository.VendorRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Vendor implements UserDetails {
    @SequenceGenerator(
            name = "vendor_sequence",
            sequenceName = "vendor_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "vendor_sequence"
    )
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = ALL)
    @JoinColumn(name = "company_id")
    private Company company;
    @Column(
            unique = true,
            nullable = false
    )
    private String email;
    @Column(
            unique = true,
            nullable = false
    )
    private String username;
    @Column(
            nullable = false
    )
    private String password;
    @Column(
            nullable = false
    )
    private String phoneNumber;
    private boolean locked;
    private boolean enabled;

    public Vendor(
            Company company,
            String email,
            String username,
            String password,
            String phoneNumber
    ){
        this.company = company;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_VENDOR"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
