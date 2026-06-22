package com.star.bank_products.controller;

import com.star.bank_products.dto.RuleListResponse;
import com.star.bank_products.dto.RuleResponse;
import com.star.bank_products.dto.RuleStatsResponse;
import com.star.bank_products.model.DynamicRuleEntity;
import com.star.bank_products.service.RuleService;
import com.star.bank_products.service.RuleStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final RuleService ruleService;
    private final RuleStatsService ruleStatsService;

    public RuleController(
            RuleService ruleService,
            RuleStatsService ruleStatsService
    ) {
        this.ruleService = ruleService;
        this.ruleStatsService = ruleStatsService;
    }

    @PostMapping
    public DynamicRuleEntity createRule(
            @RequestBody DynamicRuleEntity rule
    ) {
        return ruleService.createRule(rule);
    }

    @GetMapping
    public RuleListResponse getRules() {
        return new RuleListResponse(
                ruleService.getAllRules()
                        .stream()
                        .map(RuleResponse::from)
                        .toList()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(
            @PathVariable UUID id
    ) {

        ruleService.deleteRule(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public RuleStatsResponse getStats() {
        return ruleStatsService.getStats();
    }
}