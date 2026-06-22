package com.star.bank_products.service;

import com.star.bank_products.dto.RuleStatDto;
import com.star.bank_products.dto.RuleStatsResponse;
import com.star.bank_products.model.DynamicRuleEntity;
import com.star.bank_products.model.RuleStatsEntity;
import com.star.bank_products.repository.RuleStatsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RuleStatsService {

    private final RuleStatsRepository repository;

    public RuleStatsService(
            RuleStatsRepository repository
    ) {
        this.repository = repository;
    }

    public void createStatsForRule(
            DynamicRuleEntity rule
    ) {
        repository.save(
                new RuleStatsEntity(rule)
        );
    }

    public void deleteStats(
            UUID ruleId
    ) {
        repository.deleteById(ruleId);
    }

    public void increment(
            UUID ruleId
    ) {
        RuleStatsEntity stats =
                repository.findById(ruleId)
                        .orElseThrow();

        stats.increment();

        repository.save(stats);
    }

    public RuleStatsResponse getStats() {

        List<RuleStatDto> stats =
                repository.findAll()
                        .stream()
                        .map(entity ->
                                new RuleStatDto(
                                        entity.getRuleId(),
                                        entity.getCount()
                                ))
                        .toList();

        return new RuleStatsResponse(stats);
    }
}
