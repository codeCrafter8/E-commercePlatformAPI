package com.pricecomparison.controller;

import com.pricecomparison.payload.request.create.CreateAppUserRequest;
import com.pricecomparison.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody CreateAppUserRequest request) {
        String token = registrationService.register(request);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        registrationService.confirm(token);
        return new ResponseEntity<>("Confirmed.", HttpStatus.OK);
    }
}
