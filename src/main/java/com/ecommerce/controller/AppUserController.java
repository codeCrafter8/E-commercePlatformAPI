package com.ecommerce.controller;

import com.ecommerce.dto.AppUserDto;
import com.ecommerce.payload.request.CreateAppUserRequest;
import com.ecommerce.payload.request.CreateVendorRequest;
import com.ecommerce.payload.request.UpdateAppUserRequest;
import com.ecommerce.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<List<AppUserDto>> getAllUsers() {
        List<AppUserDto> users = appUserService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<AppUserDto> getUserById(@PathVariable("id") Long userId) {
        AppUserDto user = appUserService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody CreateAppUserRequest createRequest) {
        Long savedUserId = appUserService.createUser(createRequest);
        return new ResponseEntity<>(savedUserId, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") Long userId,
                                        @RequestBody UpdateAppUserRequest updateRequest) {
        appUserService.updateUser(userId, updateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        appUserService.deleteUser(userId);
        return new ResponseEntity<>("User successfully deleted.", HttpStatus.OK);
    }
}
