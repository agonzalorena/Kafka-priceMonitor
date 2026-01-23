package com.kafka.producer.service.provider;

import com.kafka.producer.presentation.dto.PriceDTO;
import com.kafka.producer.service.PriceSource;

import java.util.Optional;

public interface PriceProviderStrategy {
    PriceSource getSourceType();
    Optional<PriceDTO> getPrice();
}
