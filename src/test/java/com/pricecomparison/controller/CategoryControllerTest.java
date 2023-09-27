package com.pricecomparison.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricecomparison.model.Category;
import com.pricecomparison.payload.request.CategoryRequest;
import com.pricecomparison.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("john")
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }
    @Test
    void canGetAllCategories() throws Exception {
        //given
        Category category1 = new Category("Electronics", null);
        Category category2 = new Category("Smartphones", category1);
        List<Category> categories = List.of(category1, category2);
        categoryRepository.saveAll(categories);

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/categories"));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(categories.size()));
    }

    @Test
    void canGetCategoryById() throws Exception {
        //given
        Category category1 = new Category("Electronics", null);
        Category category2 = new Category("Smartphones", category1);
        List<Category> categories = List.of(category1, category2);
        categoryRepository.saveAll(categories);

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/categories/{id}", category2.getId()));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(category2.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.parentId").value(category2.getParent().getId()));
    }

    @Test
    void canCreateCategory() throws Exception {
        //given
        Category category1 = new Category("Electronics", null);
        categoryRepository.save(category1);
        CategoryRequest createRequest = new CategoryRequest("Smartphones", category1.getId());

        //when
        //then
        MvcResult result = mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        Long id = Long.valueOf(result.getResponse().getContentAsString());
        Category savedCategory = categoryRepository.findById(id).get();

        assertThat(savedCategory.getTitle()).isEqualTo(createRequest.title());
        assertThat(savedCategory.getParent().getId()).isEqualTo(createRequest.parentId());
    }

    @Test
    void canUpdateCategory() throws Exception {
        //given
        Category category1 = new Category("Electronics", null);
        Category category2 = new Category("Smartphones", category1);
        List<Category> categories = List.of(category1, category2);
        categoryRepository.saveAll(categories);

        CategoryRequest updateRequest = new CategoryRequest("Laptops", category1.getId());

        //when
        ResultActions response = mockMvc.perform(put("/api/v1/categories/{id}", category2.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        //then
        response.andExpect(status().isOk())
                .andDo(print());

        Category updatedCategory = categoryRepository.findById(category2.getId()).get();

        assertThat(updatedCategory.getTitle()).isEqualTo(updateRequest.title());
    }

    @Test
    void canDeleteCategory() throws Exception {
        //given
        Category category = new Category("Electronics", null);
        categoryRepository.save(category);

        //when
        ResultActions response = mockMvc.perform(delete("/api/v1/categories/{id}", category.getId()));

        //then
        response.andExpect(status().isOk())
                .andExpect(content().string("Category successfully deleted."))
                .andDo(print());

        boolean exists = categoryRepository.existsById(category.getId());
        assertThat(exists).isFalse();
    }
}