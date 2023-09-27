package com.pricecomparison.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricecomparison.model.Category;
import com.pricecomparison.model.Product;
import com.pricecomparison.payload.request.create.CreateProductRequest;
import com.pricecomparison.payload.request.update.UpdateProductRequest;
import com.pricecomparison.repository.CategoryRepository;
import com.pricecomparison.repository.ProductRepository;
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
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void canGetAllProducts() throws Exception {
        //given
        Category category = new Category("Electronics", null);
        List<Product> products = List.of(
                new Product("HUB USB Unitek 4x USB-A 3.1 Gen1", category, "4894160044402"),
                new Product("JBL JBLT110BLK", category, "6925281918926")
        );
        categoryRepository.save(category);
        productRepository.saveAll(products);

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/products"));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(products.size()));
    }

    @Test
    void canGetProductById() throws Exception {
        //given
        Category category = new Category("Electronics", null);
        Product product = new Product("HUB USB Unitek 4x USB-A 3.1 Gen1", category, "4894160044402");
        categoryRepository.save(category);
        productRepository.save(product);

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/products/{id}", product.getId()));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(product.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryId").value(product.getCategory().getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.EAN").value(product.getEAN()));
    }

    @Test
    void canCreateProduct() throws Exception {
        //given
        Category category = new Category("Electronics", null);
        categoryRepository.save(category);
        CreateProductRequest createRequest = new CreateProductRequest("HUB USB Unitek 4x USB-A 3.1 Gen1", category.getId(), "4894160044402");

        //when
        //then
        MvcResult result = mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        Long id = Long.valueOf(result.getResponse().getContentAsString());
        Product product = productRepository.findById(id).get();

        assertThat(product.getTitle()).isEqualTo(createRequest.title());
        assertThat(product.getCategory().getId()).isEqualTo(createRequest.categoryId());
        assertThat(product.getEAN()).isEqualTo(createRequest.EAN());
    }

    @Test
    void canUpdateProduct() throws Exception {
        //given
        Category category = new Category("Electronics", null);
        Product product = new Product("HUB USB Unitek 4x USB-A 3.1 Gen1", category, "4894160044402");
        categoryRepository.save(category);
        productRepository.save(product);

        UpdateProductRequest updateRequest = new UpdateProductRequest("HUB USB Unitek 4x USB-A 3.1 Gen2", category.getId(), "345667233445");

        //when
        ResultActions response = mockMvc.perform(put("/api/v1/products/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        //then
        response.andExpect(status().isOk())
                .andDo(print());

        Product updatedProduct = productRepository.findById(product.getId()).get();

        assertThat(updatedProduct.getTitle()).isEqualTo(updateRequest.title());
        assertThat(updatedProduct.getEAN()).isEqualTo(updateRequest.EAN());
    }

    @Test
    void canDeleteProduct() throws Exception {
        //given
        Category category = new Category("Electronics", null);
        Product product = new Product("HUB USB Unitek 4x USB-A 3.1 Gen1", category, "4894160044402");
        categoryRepository.save(category);
        productRepository.save(product);

        //when
        ResultActions response = mockMvc.perform(delete("/api/v1/products/{id}", product.getId()));

        //then
        response.andExpect(status().isOk())
                .andExpect(content().string("Product successfully deleted."))
                .andDo(print());

        boolean exists = productRepository.existsById(product.getId());
        assertThat(exists).isFalse();
    }
}