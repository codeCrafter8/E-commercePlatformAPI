package com.pricecomparison.service;

import com.pricecomparison.payload.request.AuthenticationRequest;
import com.pricecomparison.payload.response.AuthenticationResponse;
import com.pricecomparison.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        //Todo: czy to zwracac?
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtUtil.issueToken(userDetails);

        return new AuthenticationResponse(token);
    }
}
