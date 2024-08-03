package com.pricecomparison.service;

import com.pricecomparison.enumeration.AppUserRole;
import com.pricecomparison.exception.ResourceNotFoundException;
import com.pricecomparison.model.AppUser;
import com.pricecomparison.model.ConfirmationToken;
import com.pricecomparison.repository.ConfirmationTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceTest {

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;
    @InjectMocks
    private ConfirmationTokenService underTest;
    private ConfirmationToken confirmationToken;
    private String token;

    @BeforeEach
    void setUp() {
        AppUser appUser = new AppUser(1L, "John", "Doe", "john123", "john@gmail.com", "password", AppUserRole.USER);
        token = "z3jsS6Jd754zQsxYXpOVKOliy0mDd1tUK7lFFXebI7i5MmgmWbkZ79xctb5z8COV";
        confirmationToken = new ConfirmationToken(
                1L,
                token,
                appUser,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10)
        );
    }

    @Test
    void canSaveConfirmationToken() {
        //when
        underTest.saveConfirmationToken(confirmationToken);

        //then
        verify(confirmationTokenRepository).save(any(ConfirmationToken.class));
    }

    @Test
    void canGetConfirmationToken() {
        //given
        given(confirmationTokenRepository.findByToken(token)).willReturn(Optional.of(confirmationToken));

        //when
        ConfirmationToken actual = underTest.getConfirmationTokenByToken(token);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(confirmationToken);
    }

    @Test
    void willThrowWhenGetConfirmationTokenByTokenReturnsEmptyOptional() {
        //given
        given(confirmationTokenRepository.findByToken(token)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getConfirmationTokenByToken(token))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Confirmation token with token [%s] not found".formatted(token));
    }
}