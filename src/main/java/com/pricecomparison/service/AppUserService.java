package com.pricecomparison.service;

import com.pricecomparison.dto.AppUserDto;
import com.pricecomparison.mapper.AppUserMapper;
import com.pricecomparison.model.AppUser;
import com.pricecomparison.model.ConfirmationToken;
import com.pricecomparison.payload.request.create.CreateAppUserRequest;
import com.pricecomparison.payload.request.update.UpdateAppUserRequest;
import com.pricecomparison.exception.DuplicateResourceException;
import com.pricecomparison.exception.ResourceNotFoundException;
import com.pricecomparison.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AppUserService implements UserDetailsService {
    private final String USER_NOT_FOUND_MSG = "User with username [%s] not found.";
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));

        //TODO: Czy to na pewno ok?
        if(!appUser.isEnabled()) {
            throw new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }

        return new User(appUser.getUsername(), appUser.getPassword(), appUser.getAuthorities());
    }

    public List<AppUserDto> getAllUsers() {
        List<AppUser> users = appUserRepository.findAll();
        return users.stream().map(AppUserMapper::map)
                .collect(Collectors.toList());
    }

    public AppUserDto getUserById(final Long userId){
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id [%s] not found".formatted(userId))
                );
        return AppUserMapper.map(user);
    }

    public Long createUser(final CreateAppUserRequest createRequest) {
        final boolean userExists = appUserRepository.existsByEmail(createRequest.email());
        if(userExists){
            throw new DuplicateResourceException("Email already taken");
        }

        //TODO: add username check

        String password = passwordEncoder.encode(createRequest.password());

        AppUser user = AppUserMapper.map(createRequest, password);
        user.setEnabled(true);

        user = appUserRepository.save(user);

        return user.getId();
    }

    public void updateUser(final Long userId, final UpdateAppUserRequest updateRequest) {
        // TODO: for JPA use .getReferenceById(customerId) as it does does not bring object into memory and instead a reference
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id [%s] not found".formatted(userId))
                );
        //TODO: wonder if password can be changed
        user.setFirstName(updateRequest.firstName());
        user.setLastName(updateRequest.lastName());

        if (updateRequest.email() != null && !updateRequest.email().equals(user.getEmail())) {
            if (appUserRepository.existsByEmail(updateRequest.email())) {
                throw new DuplicateResourceException(
                        "Email already taken."
                );
            }
            user.setEmail(updateRequest.email());
        }

        appUserRepository.save(user);
    }

    public void deleteUser(final Long userId) {
        appUserRepository.deleteById(userId);
    }
    public String signUpUser(final CreateAppUserRequest request) {
        Long appUserId = createUser(request);

        AppUser appUser = appUserRepository.findById(appUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id [%s] not found".formatted(appUserId))
                );

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                appUser,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10)
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }
}
