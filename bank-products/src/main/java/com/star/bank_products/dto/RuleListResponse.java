package com.star.bank_products.dto;

import com.star.bank_products.model.DynamicRuleEntity;

import java.util.List;

public class RuleListResponse {
    private List<RuleResponse> data;

    public RuleListResponse(List<RuleResponse> data) {
        this.data = data;
    }

    public List<RuleResponse> getData() {
        return data;
    }

    public void setData(List<RuleResponse> data) {
        this.data = data;
    }
}
