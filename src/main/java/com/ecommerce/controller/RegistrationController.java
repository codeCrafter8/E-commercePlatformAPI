package com.ecommerce.controller;

import com.ecommerce.payload.request.create.CreateAppUserRequest;
import com.ecommerce.service.RegistrationService;
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

    //TODO: why i dont see the error page only string!?
    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        registrationService.confirm(token);
        return new ResponseEntity<>("Confirmed.", HttpStatus.OK);
    }
}
