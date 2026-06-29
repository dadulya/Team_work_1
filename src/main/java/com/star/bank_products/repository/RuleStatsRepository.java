package com.star.bank_products.repository;

import com.star.bank_products.model.RuleStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RuleStatsRepository extends JpaRepository<RuleStatsEntity, UUID> {
    @Modifying
    @Query("""
        update RuleStatsEntity s
        set s.count = s.count + 1
        where s.ruleId = :ruleId
    """)
    void increment(@Param("ruleId") UUID ruleId);
}

