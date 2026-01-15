package com.kafka.producer.presentation.dto;

import java.io.Serializable;

public record PriceDTO(String symbol, String price, long timestamp) implements Serializable {
}
