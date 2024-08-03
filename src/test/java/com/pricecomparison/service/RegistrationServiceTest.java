package com.pricecomparison.service;

import com.pricecomparison.enumeration.AppUserRole;
import com.pricecomparison.model.AppUser;
import com.pricecomparison.model.ConfirmationToken;
import com.pricecomparison.payload.request.create.CreateAppUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private AppUserService appUserService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private RegistrationService underTest;
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
    void canRegister() {
        //given
        CreateAppUserRequest createRequest = new CreateAppUserRequest("John", "Doe", "john123", "john@gmail.com", "password");

        String token = "z3jsS6Jd754zQsxYXpOVKOliy0mDd1tUK7lFFXebI7i5MmgmWbkZ79xctb5z8COV";
        given(appUserService.signUpUser(createRequest)).willReturn(token);

        //when
        String actualToken = underTest.register(createRequest);

        //then
        assertEquals(token, actualToken);
        verify(emailService, times(1)).send(anyString(), anyString(), anyString());
    }

    @Test
    void canConfirm() {
        //given
        given(confirmationTokenService.getConfirmationTokenByToken(token)).willReturn(confirmationToken);

        //when
        underTest.confirm(token);

        //then
        assertNotNull(confirmationToken.getConfirmedAt());
        assertTrue(confirmationToken.getAppUser().isEnabled());
    }

    @Test
    void willThrowWhenTryingToConfirmEmailIsAlreadyConfirmed() {
        //given
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        given(confirmationTokenService.getConfirmationTokenByToken(token)).willReturn(confirmationToken);

        //when
        //then
        assertThatThrownBy(() -> underTest.confirm(token))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Email is already confirmed.");
    }

    @Test
    void willThrowWhenTryingToConfirmEmailIsAlreadyExpired() {
        //given
        confirmationToken.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        given(confirmationTokenService.getConfirmationTokenByToken(token)).willReturn(confirmationToken);

        //when
        //then
        assertThatThrownBy(() -> underTest.confirm(token))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Token is already expired.");
    }
}