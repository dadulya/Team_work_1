package com.star.bank_products.integration;

import com.star.bank_products.repository.DynamicRuleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DynamicRecommendationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DynamicRuleRepository repository;

    @Test
    void shouldApplyDynamicRule() {
    }
}
