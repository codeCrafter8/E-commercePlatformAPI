package com.pricecomparison.service;

import com.pricecomparison.dto.AppUserDto;
import com.pricecomparison.enumeration.AppUserRole;
import com.pricecomparison.exception.DuplicateResourceException;
import com.pricecomparison.exception.ResourceNotFoundException;
import com.pricecomparison.mapper.AppUserMapper;
import com.pricecomparison.model.AppUser;
import com.pricecomparison.payload.request.create.CreateAppUserRequest;
import com.pricecomparison.payload.request.update.UpdateAppUserRequest;
import com.pricecomparison.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    private final AppUserMapper appUserMapper = new AppUserMapper();
    private AppUserService underTest;
    private AppUser appUser;
    private CreateAppUserRequest createRequest;
    private Long id;

    @BeforeEach
    void setUp() {
        underTest = new AppUserService(appUserRepository, passwordEncoder, confirmationTokenService, appUserMapper);
        id = 1L;
        appUser = new AppUser(id, "John", "Doe", "john123", "john@gmail.com", "password", AppUserRole.USER);
        createRequest = new CreateAppUserRequest("John", "Doe", "john123", "john@gmail.com", "password");
    }

    @Test
    void canGetAllUsers() {
        //when
        underTest.getAllUsers();
        //then
        verify(appUserRepository).findAll();
    }

    @Test
    void canGetUserById() {
        //given
        given(appUserRepository.findById(id)).willReturn(Optional.of(appUser));
        AppUserDto expected = appUserMapper.map(appUser);

        //when
        AppUserDto actual = underTest.getUserById(id);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willThrowWhenGetUserByIdReturnsEmptyOptional() {
        //given
        given(appUserRepository.findById(id)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getUserById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User with id [%s] not found".formatted(id));
    }

    @Test
    void canCreateUser() {
        //given
        String encodedPassword = "$s175$5";
        given(passwordEncoder.encode(createRequest.password())).willReturn(encodedPassword);
        given(appUserRepository.save(any(AppUser.class))).willReturn(appUser);

        //when
        Long returnedId = underTest.createUser(createRequest);

        //then
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getId()).isNull();
        assertThat(capturedAppUser.getFirstName()).isEqualTo(createRequest.firstName());
        assertThat(capturedAppUser.getLastName()).isEqualTo(createRequest.lastName());
        assertThat(capturedAppUser.getUsername()).isEqualTo(createRequest.username());
        assertThat(capturedAppUser.getEmail()).isEqualTo(createRequest.email());
        assertThat(capturedAppUser.getPassword()).isEqualTo(encodedPassword);

        assertThat(returnedId).isEqualTo(appUser.getId());
    }

    @Test
    void willThrowWhenTryingToCreateEmailAlreadyTaken() {
        //given
        given(appUserRepository.existsByEmail(anyString()))
                .willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.createUser(createRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        verify(passwordEncoder, never()).encode(anyString());
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void canUpdateUser() {
        //given
        UpdateAppUserRequest updateRequest = new UpdateAppUserRequest("Jonathan", "Doerr", "john2@gmail.com");
        given(appUserRepository.findById(id)).willReturn(Optional.of(appUser));

        //when
        underTest.updateUser(id, updateRequest);

        //then
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getFirstName()).isEqualTo(updateRequest.firstName());
        assertThat(capturedAppUser.getLastName()).isEqualTo(updateRequest.lastName());
        assertThat(capturedAppUser.getEmail()).isEqualTo(updateRequest.email());
    }

    @Test
    void willThrowWhenTryingToUpdateUserNotFound() {
        //when
        //then
        assertThatThrownBy(() -> underTest.updateUser(id, any()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User with id [%s] not found".formatted(id));

        verify(appUserRepository, never()).save(any());
    }

    @Test
    void willThrowWhenTryingToUpdateEmailAlreadyTaken() {
        //given
        UpdateAppUserRequest updateRequest = new UpdateAppUserRequest("John", "Doe", "john2@gmail.com");
        given(appUserRepository.findById(id)).willReturn(Optional.of(appUser));
        given(appUserRepository.existsByEmail("john2@gmail.com")).willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.updateUser(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken.");

        verify(appUserRepository, never()).save(any());
    }

    @Test
    void canDeleteUser() {
        //when
        underTest.deleteUser(id);

        //then
        verify(appUserRepository).deleteById(id);
    }

    @Test
    void canSignUpUser() {
        //given
        String encodedPassword = "$s175$5";
        given(passwordEncoder.encode(createRequest.password())).willReturn(encodedPassword);

        //when
        String token = underTest.signUpUser(createRequest);

        //then
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(appUserArgumentCaptor.capture());
        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertThat(capturedAppUser.getId()).isNull();
        assertThat(capturedAppUser.getFirstName()).isEqualTo(createRequest.firstName());
        assertThat(capturedAppUser.getLastName()).isEqualTo(createRequest.lastName());
        assertThat(capturedAppUser.getUsername()).isEqualTo(createRequest.username());
        assertThat(capturedAppUser.getEmail()).isEqualTo(createRequest.email());
        assertThat(capturedAppUser.getPassword()).isEqualTo(encodedPassword);

        assertThat(token).isNotNull();
    }

    @Test
    void willThrowWhenTryingToSignUpEmailAlreadyTaken() {
        //given
        given(appUserRepository.existsByEmail(anyString()))
                .willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> underTest.signUpUser(createRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        verify(passwordEncoder, never()).encode(anyString());
        verify(appUserRepository, never()).save(any());
        verify(confirmationTokenService, never()).saveConfirmationToken(any());
    }
}