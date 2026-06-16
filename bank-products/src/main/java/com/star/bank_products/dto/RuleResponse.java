package com.star.bank_products.dto;

import com.star.bank_products.model.DynamicRuleEntity;
import com.star.bank_products.model.RuleQuery;

import java.util.List;
import java.util.UUID;

public class RuleResponse {

    private UUID id;
    private String productName;
    private UUID productId;
    private String productText;
    private List<RuleQuery> rule;

    public RuleResponse(UUID id, String productName, UUID productId, String productText, List<RuleQuery> rule) {
        this.id = id;
        this.productName = productName;
        this.productId = productId;
        this.productText = productText;
        this.rule = rule;
    }

    public static RuleResponse from(DynamicRuleEntity entity) {
        return new RuleResponse(
                entity.getId(),
                entity.getProductName(),
                entity.getProductId(),
                entity.getProductText(),
                entity.getRule()
        );
    }

    public UUID getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductText() {
        return productText;
    }

    public List<RuleQuery> getRule() {
        return rule;
    }
}
