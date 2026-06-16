package com.star.bank_products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.bank_products.entity.DynamicRuleEntity;
import com.star.bank_products.repository.DynamicRuleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DynamicRuleRepository repository;

    @Test
    void shouldCreateRule() throws Exception {

        String json = """
        {
          "productName":"Test Product",
          "productId":"11111111-1111-1111-1111-111111111111",
          "productText":"Test Text",
          "rule":[]
        }
        """;

        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName")
                        .value("Test Product"));
    }

    @Test
    void shouldReturnAllRules() throws Exception {

        mockMvc.perform(get("/rule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    void shouldDeleteRule() throws Exception {

        DynamicRuleEntity rule =
                new DynamicRuleEntity(
                        "Delete Product",
                        UUID.randomUUID(),
                        "Text",
                        List.of()
                );

        rule = repository.save(rule);

        mockMvc.perform(
                        delete("/rule/" + rule.getId())
                )
                .andExpect(status().isNoContent());
    }
}