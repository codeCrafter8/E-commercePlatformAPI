package com.pricecomparison.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.pricecomparison.payload.request.create.CreateAppUserRequest;
import com.pricecomparison.payload.request.update.UpdateAppUserRequest;
import com.pricecomparison.repository.AppUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricecomparison.enumeration.AppUserRole;
import com.pricecomparison.model.AppUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("john")
public class AppUserControllerTest {
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
    void canGetAllUsers() throws Exception {
        //given
        List<AppUser> users = List.of(
                new AppUser(1L, "John", "Doe", "john123", "john@gmail.com", "password", AppUserRole.USER),
                new AppUser(2L, "Adam", "Smith", "adam123", "adam@gmail.com", "password", AppUserRole.USER)
        );
        appUserRepository.saveAll(users);

        //TODO: header authorization or with mock user

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/users"));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(users.size()));
    }

    @Test
    void canGetUserById() throws Exception {
        //given
        AppUser user = new AppUser("John", "Doe", "john123", "john@gmail.com", "password", AppUserRole.USER);
        appUserRepository.save(user);

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/users/{id}", user.getId()));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(user.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void canCreateUser() throws Exception {
        //given
        CreateAppUserRequest createRequest = new CreateAppUserRequest("John", "Doe", "john123", "john@gmail.com", "password");

        //when
        //then
        MvcResult result = mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        Long id = Long.valueOf(result.getResponse().getContentAsString());
        AppUser appUser = appUserRepository.findById(id).get();

        assertThat(appUser.getFirstName()).isEqualTo(createRequest.firstName());
        assertThat(appUser.getLastName()).isEqualTo(createRequest.lastName());
        assertThat(appUser.getUsername()).isEqualTo(createRequest.username());
        assertThat(appUser.getEmail()).isEqualTo(createRequest.email());
    }

    @Test
    void canUpdateUser() throws Exception {
        //given
        AppUser user = new AppUser("John", "Doe", "john123", "john@gmail.com", "password", AppUserRole.USER);
        appUserRepository.save(user);

        UpdateAppUserRequest updateRequest = new UpdateAppUserRequest("Jonathan", "Doer", "john2@gmail.com");

        //when
        ResultActions response = mockMvc.perform(put("/api/v1/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        //then
        response.andExpect(status().isOk())
                .andDo(print());

        AppUser updatedUser = appUserRepository.findById(user.getId()).get();

        assertThat(updatedUser.getFirstName()).isEqualTo(updateRequest.firstName());
        assertThat(updatedUser.getLastName()).isEqualTo(updateRequest.lastName());
        assertThat(updatedUser.getEmail()).isEqualTo(updateRequest.email());
    }

    @Test
    void canDeleteUser() throws Exception {
        //given
        AppUser user = new AppUser("John", "Doe", "john123", "john@gmail.com", "password", AppUserRole.USER);
        appUserRepository.save(user);

        //when
        ResultActions response = mockMvc.perform(delete("/api/v1/users/{id}", user.getId()));

        //then
        response.andExpect(status().isOk())
                .andExpect(content().string("User successfully deleted."))
                .andDo(print());

        boolean exists = appUserRepository.existsById(user.getId());
        assertThat(exists).isFalse();
    }
}