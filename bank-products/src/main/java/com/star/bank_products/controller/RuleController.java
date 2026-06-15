package com.star.bank_products.controller;

import com.star.bank_products.dto.RuleListResponse;
import com.star.bank_products.entity.DynamicRuleEntity;
import com.star.bank_products.service.RuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final RuleService ruleService;

    public RuleController(
            RuleService ruleService
    ) {
        this.ruleService = ruleService;
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
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(
            @PathVariable UUID id
    ) {

        ruleService.deleteRule(id);

        return ResponseEntity.noContent().build();
    }
}