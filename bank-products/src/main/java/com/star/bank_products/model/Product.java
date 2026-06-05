package com.star.bank_products.model;

import com.star.bank_products.model.rules.RecommendationRuleSet;

public class Product {
    private final String name;
    private final String ID;
    private RecommendationRuleSet rule;
    private final String annotation;

    public Product(String name, String ID, RecommendationRuleSet rule, String annotation) {
        this.name = name;
        this.ID = ID;
        this.rule = rule;
        this.annotation = annotation;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return ID;
    }

    public RecommendationRuleSet getRule() {
        return rule;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setRule(RecommendationRuleSet rule) {
        this.rule = rule;
    }
}
