package com.kafka.consumer.presentation.dto;

import java.time.LocalDateTime;

public record AlertDTO(String alertType, Object data, LocalDateTime timestamp) {
    public static AlertDTO trend(AlertTrendDTO trendData){
        return new AlertDTO("TREND", trendData, LocalDateTime.now());
    }
    public static AlertDTO variation(AlertVariationDTO variationData){
        return new AlertDTO("VARIATION", variationData, LocalDateTime.now());
    }

}
