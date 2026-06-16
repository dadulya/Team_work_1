package com.star.bank_products.rule;

import com.star.bank_products.model.rules.RecommendationRule3;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RecommendationRule3Test {

    @Autowired
    private RecommendationRule3 recommendationRule3;

    @Test
    void shouldReturnSimpleCredit() {

        UUID userId =
                UUID.fromString(
                        "1f9b149c-6577-448a-bc94-16bea229b71a"
                );

        var result = recommendationRule3.check(userId);

        assertTrue(result.isPresent());

        assertEquals(
                "Простой кредит",
                result.get().getName()
        );
    }
}
