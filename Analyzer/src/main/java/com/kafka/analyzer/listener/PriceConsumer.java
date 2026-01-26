package com.kafka.analyzer.listener;

import com.kafka.analyzer.presentation.dto.PriceDTO;
import com.kafka.analyzer.service.AnalyzerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PriceConsumer {
    private final AnalyzerService analyzerService;

    public PriceConsumer(AnalyzerService analyzerService){
        this.analyzerService = analyzerService;
    }

    @KafkaListener(topics = "USDTPRICES",groupId = "g2")
    private void consume(PriceDTO dto){
        System.out.println("Consumed price: " + dto);
        analyzerService.analize(dto);
    }

}
