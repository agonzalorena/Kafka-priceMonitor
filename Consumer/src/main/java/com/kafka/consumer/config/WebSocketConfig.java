package com.kafka.consumer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Los clientes se suscribirán a rutas que empiecen con /topic
        config.enableSimpleBroker("/topic");
        // Prefijo para los mensajes que vienen DEL cliente HACIA el servidor
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Punto de conexión WebSocket
        registry.addEndpoint("/ws-prices")
                .setAllowedOriginPatterns("*"); // Permitir CORS desde cualquier origen (ajustar en producción)
                /*.withSockJS(); // Habilitar SockJS como fallback*/
    }
}
