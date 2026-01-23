package com.kafka.producer.service.provider;

import com.kafka.producer.presentation.dto.PriceDTO;
import com.kafka.producer.service.PriceSource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HyperPriceStrategy implements PriceProviderStrategy {
    @Override
    public PriceSource getSourceType() {
        return PriceSource.HYPER;
    }

    @Override
    public Optional<PriceDTO> getPrice() {
        String minPrice = "1400";
        String maxPrice = "1700";
        String value =  String.valueOf((int)(Math.random() * (Integer.parseInt(maxPrice) - Integer.parseInt(minPrice))) + Integer.parseInt(minPrice));
        return Optional.of(new PriceDTO("HYPERUSDT", value, System.currentTimeMillis()));
    }
}
