package com.kafka.analyzer.service;

import com.kafka.analyzer.presentation.dto.AlertTrendDTO;
import com.kafka.analyzer.presentation.dto.AlertVariationDTO;
import com.kafka.analyzer.presentation.dto.PriceDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnalyzerService {
    private final double THRESHOLD = 0.1; // 10% threshold
    private final int MAX = 3;
    private final Map<String, Deque<PriceDTO>> historyPriceMap = new  ConcurrentHashMap<>();

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic = "ALERTS";
    public AnalyzerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void analize(PriceDTO dto){
        Deque<PriceDTO> history = historyPriceMap.computeIfAbsent(dto.symbol(),k -> new ArrayDeque<>());

        PriceDTO lastPrice = history.peekFirst();

        if(history.size() == MAX){
            history.pollLast();
        }
        history.addFirst(dto);

        if(lastPrice != null){
            checkVariation(lastPrice, dto);
        }
        if(history.size() == MAX){
            checkTrend(dto.symbol(), history);
        }
    }

    private void checkVariation(PriceDTO lastPrice, PriceDTO newPrice){
        double last = lastPrice.price();
        double news = newPrice.price();

        double variation = Math.abs(news - last) / last; // abs elimina el signo

        if(variation >= THRESHOLD){
            String dir = (news > last) ? "SUBIO" : "BAJO";
            AlertVariationDTO alert = new AlertVariationDTO(newPrice.symbol(), dir, (variation * 100));
            send(alert, "VARIATION");
        }
    }
    private void checkTrend(String symbol,Deque<PriceDTO> history){
        //List<PriceDTO> list = history.stream().toList();
        // Se usa var para no especificar el tipo y si cambia Deque a otro tipo no hay que cambiarlo en esta linea
        var list = history.stream().toList();

        double p1 = list.get(0).price();
        double p2 = list.get(1).price();
        double p3 = list.get(2).price();

        if(p1 > p2 && p2 > p3){
            AlertTrendDTO alert = new AlertTrendDTO(symbol, "ALCISTA");
            send(alert, "TREND");
        } else if (p3 > p2 && p2 > p1) {
            AlertTrendDTO alert = new AlertTrendDTO(symbol, "BAJISTA");
            send(alert, "TREND");
        }
    }

    private void send(Object msg, String key){
        kafkaTemplate.send(topic, key, msg)
                .whenComplete((result,exception)->{
                    if(exception !=null){
                        System.err.println("Error enviando mensaje: " + exception.getMessage());
                    }else {
                        System.out.println("Mensaje enviado en offset: " + result.getRecordMetadata().offset());
                    }
                });
        System.out.println("Mensaje enviado: " + msg);
    }

    public Map<String, Deque<PriceDTO>> getHistoryPriceMap() {
        return historyPriceMap;
    }

}
