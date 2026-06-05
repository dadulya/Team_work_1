package com.star.bank_products.model.rules;

import com.star.bank_products.dto.RecommendationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRule1 implements RecommendationRuleSet {
    private static final UUID PRODUCT_ID = UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a");
    private static final String PRODUCT_NAME = "Invest 500";
    private static final String PRODUCT_TEXT = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде. Не упустите возможность разнообразить свой портфель, снизить риски и следить за актуальными рыночными тенденциями. Откройте ИИС сегодня и станьте ближе к финансовой независимости!";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RecommendationRule1(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        if (hasDebitProduct(userId) && !hasInvestProduct(userId) && getSavingDepositSum(userId) > 1000) {
            return Optional.of(new RecommendationDto(PRODUCT_NAME, PRODUCT_ID, PRODUCT_TEXT));
        }
        return Optional.empty();
    }

    private boolean hasDebitProduct(UUID userId) {
        String sql = """
            SELECT COUNT(*) > 0
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = 'DEBIT'
        """;

        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, userId);
        return Boolean.TRUE.equals(result);
    }

    private boolean hasInvestProduct(UUID userId) {
        String sql = """
            SELECT COUNT(*) > 0
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = 'INVEST'
        """;

        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, userId);
        return Boolean.TRUE.equals(result);
    }

    private double getSavingDepositSum(UUID userId) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? 
              AND p.type = 'SAVING' 
              AND t.type = 'DEPOSIT'
        """;

        Double sum = jdbcTemplate.queryForObject(sql, Double.class, userId);
        return sum != null ? sum : 0.0;
    }
}