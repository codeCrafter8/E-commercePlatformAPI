package com.ecommerce.service;

import com.ecommerce.payload.request.AuthenticationRequest;
import com.ecommerce.payload.response.AuthenticationResponse;
import com.ecommerce.security.jwt.JwtUtil;
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
    private final AppUserService appUserService;
    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        //Todo: czy to zwracac?
        /*AppUser principal = (AppUser) authentication.getPrincipal();
        AppUserDto appUserDto = AppUserMapper.map(principal);*/

        UserDetails userDetails = appUserService.loadUserByUsername(request.username());

        String token = jwtUtil.issueToken(userDetails);

        return new AuthenticationResponse(token);
    }
}
