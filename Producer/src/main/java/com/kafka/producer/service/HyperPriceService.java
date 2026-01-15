package com.kafka.producer.service;

import com.kafka.producer.presentation.dto.PriceDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HyperPriceService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public HyperPriceService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void sendMsgAsync(){
        String topic = "t-priceTest";
        PriceDTO price = new PriceDTO("HYPERUSDT", generateRandomPrice(), System.currentTimeMillis());
        String key = String.valueOf(price.price().hashCode());
        kafkaTemplate.send(topic, key, price)
                        .whenComplete((result,exeption)->{
                            if(exeption !=null){
                                System.err.println("Error enviando mensaje: " + exeption.getMessage());
                            }else {
                                System.out.println("Mensaje enviado en offset: " + result.getRecordMetadata().offset());
                            }
                        });
        System.out.println("Mensaje enviado: " + price);
    }

    public void sendMsgSync(){
        String topic = "t-priceTest";
        String msg = "Precio actual: " + Math.random() * 100;
        String key = String.valueOf(msg.hashCode());
        try {
            var result = kafkaTemplate.send(topic, key, msg).get();
            System.out.println("Mensaje enviado en offset: " + result.getRecordMetadata().offset());
        } catch (Exception e) {
            System.err.println("Error enviando mensaje: " + e.getMessage());
        }
        System.out.println("Mensaje enviado: " + msg);
    }

    private String generateRandomPrice(){
        String minPrice = "1400";
        String maxPrice = "1700";
        return String.valueOf((int)(Math.random() * (Integer.parseInt(maxPrice) - Integer.parseInt(minPrice))) + Integer.parseInt(minPrice));
    }
}
