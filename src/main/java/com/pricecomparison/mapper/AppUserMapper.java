package com.pricecomparison.mapper;

import com.pricecomparison.dto.AppUserDto;
import com.pricecomparison.enumeration.AppUserRole;
import com.pricecomparison.model.AppUser;
import com.pricecomparison.payload.request.create.CreateAppUserRequest;

public final class AppUserMapper {
    public static AppUserDto map(final AppUser appUser) {
        return new AppUserDto(
                appUser.getId(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getUsername(),
                appUser.getEmail()
        );
    }

    public static AppUser map(final CreateAppUserRequest createRequest, String password){
        return new AppUser(
                createRequest.firstName(),
                createRequest.lastName(),
                createRequest.username(),
                createRequest.email(),
                password,
                AppUserRole.USER
        );
    }
}
