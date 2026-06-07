package com.star.bank_products.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class RecomendationRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecomendationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Запрос для получения всех метрик.
    // hasCreditActivity: true, если есть хоть одна транзакция по продукту типа CREDIT.
    // sumDebitDeposits: сумма транзакций DEPOSIT по продуктам типа DEBIT.
    // sumDebitWithdraws: сумма транзакций WITHDRAW по продуктам типа DEBIT.
    public UserMetrics getUserMetrics(UUID userId) {
        String sql = """
                    SELECT
                        -- Проверяем наличие транзакций по кредитным продуктам
                        CASE 
                            WHEN EXISTS (
                                SELECT 1 FROM transactions t
                                JOIN products p ON t.product_id = p.id
                                WHERE p.user_id = ? 
                                  AND p.product_type = 'CREDIT'
                                  AND t.id IS NOT NULL
                            ) THEN TRUE 
                            ELSE FALSE 
                        END AS hasCreditProduct,
                
                        -- Сумма пополнений (DEPOSIT) только по DEBIT продуктам
                        COALESCE(SUM(CASE 
                            WHEN p.product_type = 'DEBIT' AND t.transaction_type = 'DEPOSIT' THEN t.amount 
                            ELSE 0 
                        END), 0) AS sumDebitDeposits,
                
                        -- Сумма трат (WITHDRAW) только по DEBIT продуктам
                        COALESCE(SUM(CASE 
                            WHEN p.product_type = 'DEBIT' AND t.transaction_type = 'WITHDRAW' THEN t.amount 
                            ELSE 0 
                        END), 0) AS sumDebitWithdraws
                    FROM products p
                    LEFT JOIN transactions t ON p.id = t.product_id
                    WHERE p.user_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            return new UserMetrics(
                    rs.getBoolean("hasCreditProduct"),
                    rs.getBigDecimal("sumDebitDeposits"),
                    rs.getBigDecimal("sumDebitWithdraws")
            );
        }, userId, userId); // Передаем userId дважды: один раз для EXISTS, второй для основного WHERE
    }

    // Вспомогательный класс для хранения результатов запроса
    public static class UserMetrics {
        private final boolean hasCreditProduct;
        private final BigDecimal sumDebitDeposits;
        private final BigDecimal sumDebitWithdraws;

        public UserMetrics(boolean hasCreditProduct, BigDecimal sumDebitDeposits, BigDecimal sumDebitWithdraws) {
            this.hasCreditProduct = hasCreditProduct;
            this.sumDebitDeposits = sumDebitDeposits;
            this.sumDebitWithdraws = sumDebitWithdraws;
        }

        public boolean isHasCreditProduct() {
            return hasCreditProduct;
        }

        public BigDecimal getSumDebitDeposits() {
            return sumDebitDeposits;
        }

        public BigDecimal getSumDebitWithdraws() {
            return sumDebitWithdraws;
        }
    }
}