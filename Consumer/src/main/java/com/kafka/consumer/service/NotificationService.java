package com.kafka.consumer.service;

import com.kafka.consumer.presentation.dto.PriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate template;


    public void sendTest(){
        System.out.println("Enviando mensaje de prueba via WebSocket...");
        this.template.convertAndSend("/topic/test", "Mensaje de prueba desde el servidor");
    }
    public void sendPriceUpdate(PriceDTO price){
        System.out.println("Enviando actualización de precio via WebSocket: " + price.price());
        this.template.convertAndSend("/topic/test", price);
    }
}
