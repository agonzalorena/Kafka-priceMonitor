package com.kafka.analyzer.presentation.controller;

import com.kafka.analyzer.listener.PriceConsumer;
import com.kafka.analyzer.service.AnalyzerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private final AnalyzerService service;

    public TestController(AnalyzerService service){
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<?> getPrices(){
        return ResponseEntity.ok(service.getHistoryPriceMap());
    }
}
