package com.kafka.producer.service.http;

import com.kafka.producer.presentation.dto.BinancePriceResponseDTO;
import com.kafka.producer.presentation.dto.PriceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class BinanceClient {

    private static final Logger log = LoggerFactory.getLogger(BinanceClient.class);
    private static final String BINANCE_API_URL = "https://api.binance.com/api/v3/ticker/price?symbol=";

    private final RestTemplate restTemplate;

    public BinanceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<PriceDTO> getPrice(String symbol){
        String url = BINANCE_API_URL + symbol;
        try{
            BinancePriceResponseDTO response = restTemplate.getForObject(url, BinancePriceResponseDTO.class);
            if(response == null){
                log.warn("Binance respondio pero con null para el simbolo: {}", symbol);
                return Optional.empty();
            }

            PriceDTO dto = new PriceDTO(
                    response.symbol(),
                    response.price(),
                    System.currentTimeMillis()
            );
            return Optional.of(dto);
        } catch (Exception e) {
            log.error("Error al obtener precio de Binance para el simbolo {}: {}", symbol, e.getMessage());
            return Optional.empty();
        }
    }

}
