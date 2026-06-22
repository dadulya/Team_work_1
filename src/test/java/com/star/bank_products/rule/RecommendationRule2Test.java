package com.star.bank_products.rule;

import com.star.bank_products.model.rules.RecommendationRule2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecommendationRule2Test {

    @Autowired
    private RecommendationRule2 recommendationRule2;

    @Test
    void testUserWhoQualifiesForTopSaving() {
        // Пользователь из ТЗ, для которого выполняются все правила рекомендации 2
        UUID testUserId = UUID.fromString("d4a4d619-9a0c-4fc5-b0cb-76c49409546b");

        var result = recommendationRule2.check(testUserId);

        assertTrue(result.isPresent(), "Пользователь должен получить рекомендацию Top Saving");
        assertEquals("Top Saving", result.get().getName());
        assertEquals(UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"), result.get().getId());
        System.out.println("✅ Тест пройден! Рекомендация: " + result.get().getName());
    }

    @Test
    void testUserWhoDoesNotQualifyForTopSaving() {
        // Пользователь из ТЗ для правила 1 (не должен подходить под правило 2)
        UUID testUserId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");

        var result = recommendationRule2.check(testUserId);

        assertTrue(result.isEmpty(), "Этот пользователь НЕ должен получать рекомендацию Top Saving");
        System.out.println("✅ Тест пройден! Пользователю не подходит Top Saving");
    }

    @Test
    void testAnotherUserWhoDoesNotQualify() {
        // Пользователь из ТЗ для правила 3 (тоже не должен подходить)
        UUID testUserId = UUID.fromString("1f9b149c-6577-448a-bc94-16bea229b71a");

        var result = recommendationRule2.check(testUserId);

        assertTrue(result.isEmpty(), "Этот пользователь НЕ должен получать рекомендацию Top Saving");
        System.out.println("✅ Тест пройден! Пользователю не подходит Top Saving");
    }
}