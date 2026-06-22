package com.star.bank_products.controller;

import com.star.bank_products.repository.TransactionQueryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final TransactionQueryRepository transactionQueryRepository;

    public ManagementController(TransactionQueryRepository transactionQueryRepository) {
        this.transactionQueryRepository = transactionQueryRepository;
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<Map<String, String>> clearCaches() {
        transactionQueryRepository.clearAllCaches();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "All caches cleared successfully"
        ));
    }
}