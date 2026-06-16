package com.star.bank_products.dto;

import com.star.bank_products.model.DynamicRuleEntity;

import java.util.List;

public class RuleListResponse {
    private List<DynamicRuleEntity> data;

    public RuleListResponse(List<DynamicRuleEntity> data) {
        this.data = data;
    }

    public List<DynamicRuleEntity> getData() {
        return data;
    }

    public void setData(List<DynamicRuleEntity> data) {
        this.data = data;
    }
}
