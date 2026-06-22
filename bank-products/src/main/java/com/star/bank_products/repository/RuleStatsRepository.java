package com.star.bank_products.repository;

import com.star.bank_products.model.RuleStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RuleStatsRepository extends JpaRepository<RuleStatsEntity, UUID> {
}
