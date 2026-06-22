package com.star.bank_products.controller;

import com.star.bank_products.dto.RecommendationResponse;
import com.star.bank_products.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(
            RecommendationService service
    ) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public RecommendationResponse getRecommendations(
            @PathVariable UUID userId
    ) {

        return new RecommendationResponse(
                userId,
                service.getRecommendations(userId)
        );
    }
}