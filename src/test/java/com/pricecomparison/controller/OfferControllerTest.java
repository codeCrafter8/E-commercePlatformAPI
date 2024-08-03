package com.pricecomparison.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pricecomparison.model.Category;
import com.pricecomparison.model.Offer;
import com.pricecomparison.model.Product;
import com.pricecomparison.repository.CategoryRepository;
import com.pricecomparison.repository.OfferRepository;
import com.pricecomparison.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("john")
class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        offerRepository.deleteAll();
    }

    @Test
    void getAllOffers() throws Exception {
        //given
        Category category = new Category("Electronics", null);
        Product product = new Product("HUB USB Unitek 4x USB-A 3.1 Gen1", category, "4894160044402");
        List<Offer> offers = List.of(
                new Offer("Allegro", "12345", 15.99F, product),
                new Offer("Ebay", "8823", 30.99F, product)
        );
        categoryRepository.save(category);
        productRepository.save(product);
        offerRepository.saveAll(offers);

        //when
        ResultActions response = mockMvc.perform(get("/api/v1/products/{id}/offers", product.getId()));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(offers.size()));
    }
}