package com.kafka.producer.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Autowired
    private KafkaProperties kafkaProperties;

    //El Admin: El cliente con permisos para modificar el cluster
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic priceTest(){
        Map<String, String> advancedConfigs = getStringStringMap();

        return TopicBuilder.name("t-priceTest")
                .partitions(2) // Paralelismo
                .replicas(1)   // Copias de seguridad (1 en local/Docker simple)
                .configs(advancedConfigs)
                .build();
    }

    private static Map<String, String> getStringStringMap() {
        // Configuraciones finas para gestión de disco
        Map<String, String> advancedConfigs = new HashMap<>();

        // Política de borrado: DELETE (borra viejos) vs COMPACT (guarda el último estado)
        advancedConfigs.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE);

        // Retención: 86400000 ms = 24 Horas. ¡Vital para no llenar el disco!
        advancedConfigs.put(TopicConfig.RETENTION_MS_CONFIG, "86400000");

        // Tamaño máx mensaje: por defecto es 1MB, aquí aseguramos ese límite
        advancedConfigs.put(TopicConfig.MAX_MESSAGE_BYTES_CONFIG, "1000000");
        return advancedConfigs;
    }
}
