package com.star.bank_products.service;

import com.star.bank_products.dto.RecommendationDto;
import com.star.bank_products.model.DynamicRuleEntity;
import com.star.bank_products.model.rules.RecommendationRuleSet;
import com.star.bank_products.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> fixedRules;
    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleEvaluator dynamicRuleEvaluator;
    private final RuleStatsService ruleStatsService;

    public RecommendationService(
            List<RecommendationRuleSet> fixedRules,
            DynamicRuleRepository dynamicRuleRepository,
            DynamicRuleEvaluator dynamicRuleEvaluator,
            RuleStatsService ruleStatsService
    ) {
        this.fixedRules = fixedRules;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.dynamicRuleEvaluator = dynamicRuleEvaluator;
        this.ruleStatsService = ruleStatsService;
    }



    public List<RecommendationDto> getRecommendations(
            UUID userId
    ) {
        List<RecommendationDto> recommendations = new ArrayList<>();

        for (RecommendationRuleSet rule : fixedRules) {
            Optional<RecommendationDto> result = rule.check(userId);
            result.ifPresent(recommendations::add);
        }
        List<DynamicRuleEntity> dynamicRules = dynamicRuleRepository.findAll();
        for (DynamicRuleEntity rule : dynamicRules) {
            if (dynamicRuleEvaluator.evaluate(userId, rule)) {
                ruleStatsService.increment(
                        rule.getId()
                );
                RecommendationDto dto =
                        new RecommendationDto(
                                rule.getProductName(),
                                rule.getProductId(),
                                rule.getProductText()
                        );

                recommendations.add(dto);
            }
        }

        return recommendations;
    }
}

