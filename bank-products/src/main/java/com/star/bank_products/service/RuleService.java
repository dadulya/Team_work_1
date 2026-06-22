package com.star.bank_products.service;

import com.star.bank_products.model.DynamicRuleEntity;
import com.star.bank_products.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RuleService {

    private final DynamicRuleRepository repository;
    private final RuleStatsService ruleStatsService;

    public RuleService(
            DynamicRuleRepository repository,
            RuleStatsService ruleStatsService
    ) {
        this.repository = repository;
        this.ruleStatsService = ruleStatsService;
    }

    public DynamicRuleEntity createRule(
            DynamicRuleEntity rule
    ) {

        DynamicRuleEntity savedRule =
                repository.save(rule);

        ruleStatsService.createStatsForRule(savedRule);

        return savedRule;
    }

    public List<DynamicRuleEntity> getAllRules() {
        return repository.findAll();
    }

    public void deleteRule(
            UUID id
    ) {

        ruleStatsService.deleteStats(id);

        repository.deleteById(id);
    }
}
