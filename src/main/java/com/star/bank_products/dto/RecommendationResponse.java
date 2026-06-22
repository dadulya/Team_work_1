package com.star.bank_products.dto;

import java.util.List;
import java.util.UUID;

public class RecommendationResponse {
    private UUID userId;
    private List<RecommendationDto> recommendations;

    public RecommendationResponse(
            UUID userId,
            List<RecommendationDto> recommendations
    ) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<RecommendationDto> getRecommendations() {
        return recommendations;
    }
}
