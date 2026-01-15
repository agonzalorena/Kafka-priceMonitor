package com.kafka.consumer.listener;

import com.kafka.consumer.presentation.dto.PriceDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PriceConsumer {

    @KafkaListener(topics = "t-priceTest", groupId = "g1")
    public void consume(PriceDTO price){
        System.out.println("Precio USDT recibido: " + price.price());
    }
}
