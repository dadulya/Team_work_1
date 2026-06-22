package com.star.bank_products.rule;

import com.star.bank_products.dto.RecommendationDto;
import com.star.bank_products.model.rules.RecommendationRule1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecommendationRule1Test {

    @Autowired
    private RecommendationRule1 invest500Rule;

    @Test
    void testUserWhoQualifiesForRecomendationRule1() {
        // Пользователь из ТЗ, для которого ВЫПОЛНЯЮТСЯ все правила рекомендации 1
        UUID testUserId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

        Optional<RecommendationDto> result = invest500Rule.check(testUserId);

        assertTrue(result.isPresent(), "Пользователь должен получить рекомендацию Invest 500");
        assertEquals("Invest 500", result.get().getName());
        assertEquals(UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"), result.get().getId());
        System.out.println("✅ Тест пройден! Рекомендация: " + result.get().getName());
    }

    @Test
    void testUserWhoDoesNotQualify() {
        // Пользователь, который НЕ должен получить эту рекомендацию
        // (можно взять любого другого пользователя из БД или специального)
        UUID testUserId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");

        Optional<RecommendationDto> result = invest500Rule.check(testUserId);

        assertTrue(result.isEmpty(), "Этот пользователь НЕ должен получать рекомендацию Invest 500");
        System.out.println("✅ Тест пройден! Пользователю не подходит Invest 500 (как и ожидалось)");
    }
}