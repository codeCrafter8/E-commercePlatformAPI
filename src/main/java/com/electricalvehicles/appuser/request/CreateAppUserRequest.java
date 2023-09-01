package com.electricalvehicles.appuser.request;

public record CreateAppUserRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
