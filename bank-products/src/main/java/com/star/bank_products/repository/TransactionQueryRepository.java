package com.star.bank_products.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class TransactionQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    private final Cache<String, Boolean> userOfCache;
    private final Cache<String, Boolean> activeUserOfCache;
    private final Cache<String, Boolean> compareSumCache;
    private final Cache<String, Boolean> compareDepositWithdrawCache;

    public TransactionQueryRepository(@Qualifier("primaryDataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.userOfCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .build();

        this.activeUserOfCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .build();

        this.compareSumCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .build();

        this.compareDepositWithdrawCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .build();
    }

    public boolean isUserOf(UUID userId, String productType) {
        String key = userId + ":" + productType;
        return userOfCache.get(key, k -> {
            String sql = """
                SELECT COUNT(*) > 0 FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ?
            """;
            Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, userId, productType);
            return Boolean.TRUE.equals(result);
        });
    }

    public boolean isActiveUserOf(UUID userId, String productType) {
        String key = userId + ":" + productType;
        return activeUserOfCache.get(key, k -> {
            String sql = """
                SELECT COUNT(*) >= 5 FROM transactions t
                JOIN products p ON t.product_id = p.id
                WHERE t.user_id = ? AND p.type = ?
            """;
            Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, userId, productType);
            return Boolean.TRUE.equals(result);
        });
    }

    public long getSumByType(UUID userId, String productType, String transactionType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount), 0) FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.type = ?
        """;
        Long sum = jdbcTemplate.queryForObject(sql, Long.class, userId, productType, transactionType);
        return sum != null ? sum : 0L;
    }

    public boolean compareSum(UUID userId, String productType, String txType, String operator, int constant) {
        String key = userId + ":" + productType + ":" + txType + ":" + operator + ":" + constant;
        return compareSumCache.get(key, k -> {
            long sum = getSumByType(userId, productType, txType);
            return switch (operator) {
                case ">" -> sum > constant;
                case "<" -> sum < constant;
                case "=" -> sum == constant;
                case ">=" -> sum >= constant;
                case "<=" -> sum <= constant;
                default -> false;
            };
        });
    }

    public boolean compareDepositWithdraw(UUID userId, String productType, String operator) {
        String key = userId + ":" + productType + ":" + operator;
        return compareDepositWithdrawCache.get(key, k -> {
            long deposit = getSumByType(userId, productType, "DEPOSIT");
            long withdraw = getSumByType(userId, productType, "WITHDRAW");
            return switch (operator) {
                case ">" -> deposit > withdraw;
                case "<" -> deposit < withdraw;
                case "=" -> deposit == withdraw;
                case ">=" -> deposit >= withdraw;
                case "<=" -> deposit <= withdraw;
                default -> false;
            };
        });
    }
    public void clearAllCaches() {
        userOfCache.invalidateAll();
        activeUserOfCache.invalidateAll();
        compareSumCache.invalidateAll();
        compareDepositWithdrawCache.invalidateAll();
    }
}