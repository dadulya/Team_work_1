package com.star.bank_products.model.rules;

import com.star.bank_products.dto.RecommendationDto;
import com.star.bank_products.repository.RecommendationRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRule3 implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f");
    private static final String PRODUCT_NAME = "Простой кредит";
    private static final String PRODUCT_TEXT = "Откройте мир выгодных кредитов с нами!\n\n" +
            "Ищете способ быстро и без лишних хлопот получить нужную сумму? " +
            "Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, " +
            "гибкие условия и индивидуальный подход к каждому клиенту.\n\n" +
            "Почему выбирают нас:\n\n" +
            "Быстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки " +
            "занимает всего несколько часов.\n\n" +
            "Удобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n\n" +
            "Широкий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, " +
            "автомобиля, образование, лечение и многое другое.\n\n" +
            "Не упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!";

    private final RecommendationRepository repository;

    public RecommendationRule3(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        RecommendationRepository.UserMetrics metrics = repository.getUserMetrics(userId);

        // Правило 1: Пользователь НЕ использует продукты с типом CREDIT.
        // hasCreditProduct == true означает, что транзакции по кредиту есть -> не подходит.
        if (metrics.isHasCreditProduct()) {
            return Optional.empty();
        }

        BigDecimal debitDeposits = metrics.getSumDebitDeposits();
        BigDecimal debitWithdraws = metrics.getSumDebitWithdraws();

        // Правило 2: Сумма пополнений DEBIT > суммы трат DEBIT
        // compareTo возвращает 0 если равны, <0 если меньше. Нам нужно строго больше.
        if (debitDeposits.compareTo(debitWithdraws) <= 0) {
            return Optional.empty();
        }

        // Правило 3: Сумма трат по DEBIT > 100 000 ₽
        BigDecimal threshold = BigDecimal.valueOf(100000);
        if (debitWithdraws.compareTo(threshold) <= 0) {
            return Optional.empty();
        }

        return Optional.of(new RecommendationDto(PRODUCT_NAME, PRODUCT_ID, PRODUCT_TEXT));
    }
}