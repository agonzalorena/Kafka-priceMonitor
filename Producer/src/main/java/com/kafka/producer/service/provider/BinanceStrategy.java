package com.kafka.producer.service.provider;

import com.kafka.producer.presentation.dto.PriceDTO;
import com.kafka.producer.service.PriceSource;
import com.kafka.producer.service.http.BinanceClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BinanceStrategy implements PriceProviderStrategy{
    private final BinanceClient binanceClient;

    public BinanceStrategy (BinanceClient binanceClient) {
        this.binanceClient = binanceClient;
    }
    @Override
    public PriceSource getSourceType() {
        return PriceSource.BINANCE;
    }
    @Override
    public Optional<PriceDTO> getPrice(){
        return binanceClient.getPrice("PEPEUSDT");
    }
}
