package com.star.bank_products.model.rules;

import com.star.bank_products.dto.RecommendationDto;
import com.star.bank_products.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRule2 implements RecommendationRuleSet {

    private static final UUID PRODUCT_ID = UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925");
    private static final String PRODUCT_NAME = "Top Saving";
    private static final String PRODUCT_TEXT = "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n\n" +
            "Преимущества «Копилки»:\n\n" +
            "Накопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n\n" +
            "Прозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n\n" +
            "Безопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n\n" +
            "Начните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!";

    @Autowired
    private RecommendationRepository repository;

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        // Правило 1: пользователь использует хотя бы один продукт с типом DEBIT
        if (!repository.hasDebitTransaction(userId)) {
            return Optional.empty();
        }

        // Правило 2: сумма пополнений DEBIT >= 50 000 ИЛИ SAVING >= 50 000
        BigDecimal debitDeposits = repository.sumDebitDeposits(userId);
        BigDecimal savingDeposits = repository.sumSavingDeposits(userId);

        boolean enoughDeposits = debitDeposits.compareTo(BigDecimal.valueOf(50000)) >= 0 ||
                savingDeposits.compareTo(BigDecimal.valueOf(50000)) >= 0;

        if (!enoughDeposits) {
            return Optional.empty();
        }

        // Правило 3: сумма пополнений DEBIT > суммы трат DEBIT
        BigDecimal debitWithdraws = repository.sumDebitWithdraws(userId);

        if (debitDeposits.compareTo(debitWithdraws) <= 0) {
            return Optional.empty();
        }

        return Optional.of(new RecommendationDto(PRODUCT_NAME, PRODUCT_ID, PRODUCT_TEXT));
    }
}