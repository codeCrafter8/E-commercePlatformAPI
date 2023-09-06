package com.ecommerce.mapper;

import com.ecommerce.dto.AppUserDto;
import com.ecommerce.enumeration.AppUserRole;
import com.ecommerce.model.AppUser;
import com.ecommerce.payload.request.create.CreateAppUserRequest;

public final class AppUserMapper {
    public static AppUserDto map(final AppUser appUser) {
        return new AppUserDto(
                appUser.getId(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getEmail()
        );
    }

    public static AppUser map(final CreateAppUserRequest createRequest){
        return new AppUser(
                createRequest.firstName(),
                createRequest.lastName(),
                createRequest.username(),
                createRequest.email(),
                createRequest.password(),
                AppUserRole.USER
        );
    }
}
