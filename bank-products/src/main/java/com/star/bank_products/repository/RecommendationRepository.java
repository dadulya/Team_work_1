package com.star.bank_products.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class RecommendationRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Проверяет, есть ли у пользователя хотя бы одна транзакция по продуктам типа DEBIT
    public boolean hasDebitTransaction(UUID userId) {
        String sql = """
            SELECT COUNT(*) > 0
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = 'DEBIT'
        """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userId));
    }

    // Сумма пополнений (DEPOSIT) по всем продуктам типа DEBIT
    public BigDecimal sumDebitDeposits(UUID userId) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = 'DEBIT' AND t.type = 'DEPOSIT'
        """;
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    // Сумма трат (WITHDRAW) по всем продуктам типа DEBIT
    public BigDecimal sumDebitWithdraws(UUID userId) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = 'DEBIT' AND t.type = 'WITHDRAW'
        """;
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    // Сумма пополнений (DEPOSIT) по всем продуктам типа SAVING
    public BigDecimal sumSavingDeposits(UUID userId) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = 'SAVING' AND t.type = 'DEPOSIT'
        """;
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }
}