package com.star.bank_products.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "rule_stats")
public class RuleStatsEntity {

    @Id
    @Column(name = "rule_id")
    private UUID ruleId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "rule_id")
    private DynamicRuleEntity rule;

    @Column(nullable = false)
    private long count;

    public RuleStatsEntity() {
    }

    public RuleStatsEntity(DynamicRuleEntity rule) {
        this.rule = rule;
        this.count = 0;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public DynamicRuleEntity getRule() {
        return rule;
    }

    public void setRule(DynamicRuleEntity rule) {
        this.rule = rule;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}