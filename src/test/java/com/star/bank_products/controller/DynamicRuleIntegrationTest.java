package com.star.bank_products.controller;

import com.star.bank_products.model.DynamicRuleEntity;
import com.star.bank_products.model.RuleQuery;
import com.star.bank_products.repository.DynamicRuleRepository;
import com.star.bank_products.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DynamicRuleIntegrationTest {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private DynamicRuleRepository ruleRepository;

    @Test
    void testDynamicRuleAffectsRecommendations() {

        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

        DynamicRuleEntity rule = new DynamicRuleEntity(
                "Dynamic product",
                UUID.randomUUID(),
                "Dynamic text",
                List.of(new RuleQuery("USER_OF", List.of("DEBIT"), false))
        );

        ruleRepository.save(rule);

        var result = recommendationService.getRecommendations(userId);

        boolean found = result.stream()
                .anyMatch(r -> r.getName().equals("Dynamic product"));

        assertTrue(found);
    }
}