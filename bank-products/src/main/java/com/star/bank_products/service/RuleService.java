package com.star.bank_products.service;

import com.star.bank_products.entity.DynamicRuleEntity;
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

    public DynamicRuleEntity create(DynamicRuleEntity rule) {
        return repository.save(rule);
    }

    public List<DynamicRuleEntity> getAll() {
        return repository.findAll();
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}