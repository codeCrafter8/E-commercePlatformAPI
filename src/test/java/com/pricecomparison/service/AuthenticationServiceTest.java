package com.pricecomparison.service;

import com.pricecomparison.payload.request.AuthenticationRequest;
import com.pricecomparison.payload.response.AuthenticationResponse;
import com.pricecomparison.security.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthenticationService underTest;

    @Test
    void canLogin() {
        //given
        AuthenticationRequest request = new AuthenticationRequest("john123", "password");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        given(authenticationManager.authenticate(any())).willReturn(authentication);

        given(authentication.getPrincipal()).willReturn(userDetails);

        String token = "z3jsS6Jd754zQsxYXpOVKOliy0mDd1tUK7lFFXebI7i5MmgmWbkZ79xctb5z8COV";
        given(jwtUtil.issueToken(userDetails)).willReturn(token);

        //when
        AuthenticationResponse response = underTest.login(request);

        //then
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        verify(jwtUtil).issueToken(userDetails);

        assertEquals(token, response.token());
    }
}