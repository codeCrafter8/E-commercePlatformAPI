package com.pricecomparison.repository;

import com.pricecomparison.enumeration.AppUserRole;
import com.pricecomparison.model.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository underTest;

    @Test
    void itShouldCheckIfUserDoesNotExistByEmail() {
        //when
        boolean exists = underTest.existsByEmail("john@gmail.com");

        //then
        assertThat(exists).isFalse();
    }

    @Test
    void itShouldCheckIfUserExistsByEmail() {
        //given
        AppUser appUser = new AppUser("John", "Doe", "john123", "john@gmail.com", "password", AppUserRole.USER);
        underTest.save(appUser);

        //when
        boolean exists = underTest.existsByEmail("john@gmail.com");

        //then
        assertThat(exists).isTrue();
    }
}