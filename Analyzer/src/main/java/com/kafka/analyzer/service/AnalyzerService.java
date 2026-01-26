package com.kafka.analyzer.service;

import com.kafka.analyzer.presentation.dto.PriceDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnalyzerService {
    private final double THRESHOLD = 0.1; // 10% threshold
    private final int MAX = 3;
    private final Map<String, Deque<PriceDTO>> historyPriceMap = new  ConcurrentHashMap<>();

    public void analize(PriceDTO dto){
        Deque<PriceDTO> history = historyPriceMap.computeIfAbsent(dto.symbol(),k -> new ArrayDeque<>());

        PriceDTO lastPrice = history.peekLast();

        if(history.size() == MAX){
            history.pollFirst(); // sacamos el mas antiguo
        }
        history.addLast(dto);

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
            String dir = (news > last) ? "subio" : "bajo";
            System.out.println("ALERTA: el precio " + dir + " "+ (THRESHOLD*100)+ "% o mas para el simbolo: " + newPrice.symbol());

        }
    }
    private void checkTrend(String symbol,Deque<PriceDTO> history){
        //List<PriceDTO> list = history.stream().toList();
        // Se usa var para no especificar el tipo y si cambia Deque a otro tipo no hay que cambiarlo en esta linea
        var list = history.stream().toList();

        double p1 = list.get(0).price();
        double p2 = list.get(1).price();
        double p3 = list.get(2).price();

        if(p3 > p2 && p2 > p1){
            System.out.println("Tendencia ALCISTA para el simbolo: " + symbol);
        } else if (p3 < p2 && p2 < p1) {
            System.out.println("Tendencia BAJISTA para el simbolo: " + symbol);
        }
    }


    public Map<String, Deque<PriceDTO>> getHistoryPriceMap() {
        // ordenar cada deque por timestamp antes de devolver
        historyPriceMap.values().forEach(deque ->
                deque.stream().sorted((p1, p2) -> Long.compare(p1.timestamp(), p2.timestamp()))
        );

        return historyPriceMap;
    }

}
