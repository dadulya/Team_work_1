package com.star.bank_products.service;

import com.star.bank_products.model.DynamicRuleEntity;
import com.star.bank_products.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RuleService {

    private final DynamicRuleRepository repository;

    public RuleService(DynamicRuleRepository repository) {
        this.repository = repository;
    }

    public DynamicRuleEntity createRule(DynamicRuleEntity rule) {
        return repository.save(rule);
    }

    public List<DynamicRuleEntity> getAllRules() {
        return repository.findAll();
    }

    public void deleteRule(UUID id) {
        repository.deleteById(id);
    }
}
