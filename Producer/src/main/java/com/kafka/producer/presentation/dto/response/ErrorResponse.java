package com.kafka.producer.presentation.dto.response;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({"timestamp", "status", "message"})
public record ErrorResponse(String timestamp, int status, String message) {
    public ErrorResponse(int status, String message){
        this(LocalDateTime.now().toString(), status, message);
    }
}
