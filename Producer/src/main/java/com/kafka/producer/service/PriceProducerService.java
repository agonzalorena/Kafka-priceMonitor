package com.kafka.producer.service;

import com.kafka.producer.presentation.dto.PriceDTO;
import com.kafka.producer.service.http.BinanceClient;
import com.kafka.producer.service.provider.PriceProviderStrategy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class PriceProducerService {
    private final AtomicReference<PriceSource> currentSource = new AtomicReference<>(PriceSource.HYPER);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic = "USDTPRICES";

    // Asocia el ENUM con la CLASE que lo ejecuta
    private final Map<PriceSource, PriceProviderStrategy> strategies;

    public PriceProducerService(KafkaTemplate<String, Object> kafkaTemplate, List<PriceProviderStrategy> strategyList) {
        this.kafkaTemplate = kafkaTemplate;
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(PriceProviderStrategy::getSourceType, strategy -> strategy));
    }

    @Scheduled(fixedRate = 4000)// Cada 4 segundos
    public void sendPrice(){
        PriceSource source = currentSource.get();

        PriceProviderStrategy strategy = strategies.get(source);
        if(strategy != null){
            strategy.getPrice().ifPresent(this::send);
        }else{
            System.err.println("No hay estrategia definida para: " + source);
        }
    }

    public void setPriceSource(PriceSource source){
       this.currentSource.set(source);
    }
    public PriceSource getCurrentSource(){
        return this.currentSource.get();
    }

    private void send(PriceDTO price){
        String key = price.symbol();
        kafkaTemplate.send(topic, key, price)
                .whenComplete((result,exception)->{
                    if(exception !=null){
                        System.err.println("Error enviando mensaje: " + exception.getMessage());
                    }else {
                        System.out.println("Mensaje enviado en offset: " + result.getRecordMetadata().offset());
                    }
                });
        System.out.println("Mensaje enviado: " + price);
    }
}
