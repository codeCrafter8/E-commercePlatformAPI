package com.pricecomparison.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricecomparison.payload.request.AuthenticationRequest;
import com.pricecomparison.payload.request.create.CreateAppUserRequest;
import com.pricecomparison.repository.AppUserRepository;
import com.pricecomparison.security.jwt.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @AfterEach
    void tearDown() {
        appUserRepository.deleteAll();
    }
    @Test
    void canLogin() throws Exception {
        //given
        CreateAppUserRequest createRequest = new CreateAppUserRequest("John", "Doe", "john123", "john@gmail.com", "password");

        //register
        MvcResult result = mockMvc.perform(post("/api/v1/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn();
        String registrationToken = result.getResponse().getContentAsString();

        //confirm
        mockMvc.perform(get("/api/v1/registration/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("token", registrationToken));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("john123", "password");

        //when
        //then
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String jwtToken = loginResult.getResponse().getHeader("AUTHORIZATION");

        assertThat(jwtUtil.isTokenValid(jwtToken, authenticationRequest.username())).isTrue();
    }
}