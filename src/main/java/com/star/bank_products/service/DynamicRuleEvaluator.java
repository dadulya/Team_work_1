package com.star.bank_products.service;

import com.star.bank_products.model.DynamicRuleEntity;
import com.star.bank_products.model.RuleQuery;
import com.star.bank_products.repository.TransactionQueryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DynamicRuleEvaluator {

    private final TransactionQueryRepository queryRepository;

    public DynamicRuleEvaluator(TransactionQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public boolean evaluate(UUID userId, DynamicRuleEntity rule) {
        List<RuleQuery> queries = rule.getRule();

        for (RuleQuery query : queries) {
            boolean result = evaluateSingleQuery(userId, query);
            if (!result) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateSingleQuery(UUID userId, RuleQuery query) {
        String queryType = query.getQuery();
        List<String> args = query.getArguments();
        boolean negate = query.isNegate();

        boolean rawResult;

        switch (queryType) {
            case "USER_OF" -> rawResult = queryRepository.isUserOf(userId, args.get(0));
            case "ACTIVE_USER_OF" -> rawResult = queryRepository.isActiveUserOf(userId, args.get(0));
            case "TRANSACTION_SUM_COMPARE" -> {
                String productType = args.get(0);
                String txType = args.get(1);
                String operator = args.get(2);
                int constant = Integer.parseInt(args.get(3));
                rawResult = queryRepository.compareSum(userId, productType, txType, operator, constant);
            }
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> {
                String productType = args.get(0);
                String operator = args.get(1);
                rawResult = queryRepository.compareDepositWithdraw(userId, productType, operator);
            }
            default -> {
                System.err.println("Неизвестный тип запроса: " + queryType);
                rawResult = false;
            }
        }

        return negate ? !rawResult : rawResult;
    }
}