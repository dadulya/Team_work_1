package com.star.bank_products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.bank_products.model.DynamicRuleEntity;
import com.star.bank_products.model.RuleQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class RuleApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private DynamicRuleEntity buildRule() {
        return new DynamicRuleEntity(
                "Test product",
                UUID.randomUUID(),
                "Test text",
                List.of(new RuleQuery("USER_OF", List.of("CREDIT"), false))
        );
    }

    @Test
    void testCreateGetDeleteRule() throws Exception {

        DynamicRuleEntity rule = buildRule();

        String response = mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rule)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DynamicRuleEntity created =
                objectMapper.readValue(response, DynamicRuleEntity.class);

        UUID id = created.getId();


        mockMvc.perform(get("/rule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());


        mockMvc.perform(delete("/rule/" + id))
                .andExpect(status().isNoContent());
    }
}