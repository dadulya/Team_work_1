package com.star.bank_products.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rule_stats")
public class RuleStatsEntity {

    @Id
    private java.util.UUID ruleId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "rule_id")
    private DynamicRuleEntity rule;

    @Column(nullable = false)
    private long count = 0;

    public RuleStatsEntity() {
    }

    public RuleStatsEntity(DynamicRuleEntity rule) {
        this.rule = rule;
        this.count = 0;
    }

    public java.util.UUID getRuleId() {
        return ruleId;
    }

    public DynamicRuleEntity getRule() {
        return rule;
    }

    public long getCount() {
        return count;
    }

    public void increment() {
        count++;
    }
}
