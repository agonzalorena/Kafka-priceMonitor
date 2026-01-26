package com.kafka.analyzer.presentation.dto;

import java.io.Serializable;

public record PriceDTO(String symbol, Double price, Long timestamp) implements Serializable {
}