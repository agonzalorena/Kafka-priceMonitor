package com.kafka.consumer.listener;

import com.kafka.consumer.presentation.dto.AlertDTO;
import com.kafka.consumer.presentation.dto.AlertTrendDTO;
import com.kafka.consumer.presentation.dto.AlertVariationDTO;
import com.kafka.consumer.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AlertConsumer {

    private final NotificationService notificationService;

    public AlertConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @KafkaListener(topics = "ALERTS", groupId = "variation-consumer-group",filter = "variationFilter")
    public void consumeVariation(AlertVariationDTO alert) {
        System.out.println("Alerta de variación recibida: " + alert);
        notificationService.sendAlert(AlertDTO.variation(alert));
    }

    @KafkaListener(topics = "ALERTS", groupId = "trend-consumer-group",filter = "trendFilter")
    public void consumeTrend(AlertTrendDTO alert) {
        System.out.println("Alerta de tendencia recibida: " + alert);
        notificationService.sendAlert(AlertDTO.trend(alert));
    }
}