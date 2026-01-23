package com.kafka.producer.presentation.controller;

import com.kafka.producer.presentation.dto.response.SuccessResponse;
import com.kafka.producer.service.PriceProducerService;
import com.kafka.producer.service.PriceSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/config")
public class ConfigController {

    private final PriceProducerService priceProducerService;

    public ConfigController(PriceProducerService priceProducerService) {
        this.priceProducerService = priceProducerService;
    }

    @PostMapping("/source")
    public ResponseEntity<?> changeSource(@RequestParam PriceSource type){
        priceProducerService.setPriceSource(type);
        return ResponseEntity.ok().body(new SuccessResponse(200, "Source changed to " + type));
    }

    @GetMapping("/source")
    public ResponseEntity<?> getCurrentSource(){
        return ResponseEntity.ok().body(new SuccessResponse(200, priceProducerService.getCurrentSource()));
    }
}
