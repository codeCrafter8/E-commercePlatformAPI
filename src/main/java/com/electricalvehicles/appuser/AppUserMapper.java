package com.electricalvehicles.appuser;

import com.electricalvehicles.appuser.request.CreateAppUserRequest;

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
                createRequest.email(),
                createRequest.password(),
                AppUserRole.USER
        );
    }
}
