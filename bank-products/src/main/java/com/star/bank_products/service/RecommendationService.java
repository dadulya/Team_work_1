package com.star.bank_products.service;

import com.star.bank_products.dto.RecommendationDto;
import com.star.bank_products.model.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {


    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationService(
            List<RecommendationRuleSet> ruleSets
    ) {
        this.ruleSets = ruleSets;
    }

    public List<RecommendationDto> getRecommendations(
            UUID userId
    ) {
        return ruleSets.stream()
                .map(rule -> rule.check(userId))
                .flatMap(Optional::stream)
                .toList();
    }
}

