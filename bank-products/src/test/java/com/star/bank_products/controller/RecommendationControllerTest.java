package com.star.bank_products.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnRecommendations() throws Exception {

        UUID userId =
                UUID.fromString(
                        "cd515076-5d8a-44be-930e-8d4fcb79f42d"
                );

        mockMvc.perform(
                        get("/recommendation/" + userId)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.userId").exists()
                )
                .andExpect(
                        jsonPath("$.recommendations").isArray()
                );
    }
}