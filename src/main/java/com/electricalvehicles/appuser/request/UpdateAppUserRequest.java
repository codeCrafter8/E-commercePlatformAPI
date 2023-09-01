package com.electricalvehicles.appuser.request;

public record UpdateAppUserRequest(
    String firstName,
    String lastName,
    String email
) {
}
