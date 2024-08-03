package com.pricecomparison.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricecomparison.model.AppUser;
import com.pricecomparison.payload.request.create.CreateAppUserRequest;
import com.pricecomparison.repository.AppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AppUserRepository appUserRepository;

    @AfterEach
    void tearDown() {
        appUserRepository.deleteAll();
    }

    @Test
    void canRegister() throws Exception {
        //given
        CreateAppUserRequest createRequest = new CreateAppUserRequest("John", "Doe", "john123", "john@gmail.com", "password");

        //when
        ResultActions response = mockMvc.perform(post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)));

        //then
        response.andExpect(status().isOk())
                .andDo(print());

        AppUser appUser = appUserRepository.findByUsername(createRequest.username()).get();
        assertThat(appUser.getFirstName()).isEqualTo(createRequest.firstName());
        assertThat(appUser.getLastName()).isEqualTo(createRequest.lastName());
        assertThat(appUser.getEmail()).isEqualTo(createRequest.email());
    }

    @Test
    void canConfirm() throws Exception {
        //given
        CreateAppUserRequest createRequest = new CreateAppUserRequest("John", "Doe", "john123", "john@gmail.com", "password");

        //register
        MvcResult result = mockMvc.perform(post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andReturn();
        String registrationToken = result.getResponse().getContentAsString();

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/registration/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .param("token", registrationToken));

        //then
        response.andExpect(status().isOk())
                .andExpect(content().string("Confirmed."))
                .andDo(print());
    }
}