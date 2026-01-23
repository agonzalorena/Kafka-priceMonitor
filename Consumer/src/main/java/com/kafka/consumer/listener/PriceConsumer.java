package com.kafka.consumer.listener;

import com.kafka.consumer.presentation.dto.PriceDTO;
import com.kafka.consumer.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class PriceConsumer {

    private final NotificationService notificationService;
    public PriceConsumer (NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "t-priceTest", groupId = "g1")
    public void consume(PriceDTO price){
        System.out.println("Precio USDT recibido: " + price.price());
        notificationService.sendPriceUpdate(price);

    }


    // Ejemplo de ACK manual (ACK se utiliza para confirmar la recepción del mensaje)
    /*@KafkaListener(topics = "t-usdt-prices", groupId = "grupo-precios-usdt")
    public void consumirPrecio(PriceDTO priceDTO, Acknowledgment ack) { // <--- Inyectas el objeto Ack
        try {
            System.out.println("Procesando precio: " + priceDTO.precio());

            // Imagina que aquí guardas en DB o mandas al WebSocket
            // guardarEnBaseDeDatos(priceDTO);

            // SI LLEGAMOS AQUI, TOD0 SALIÓ BIEN.
            // Firmamos el acuse de recibo.
            ack.acknowledge();
            System.out.println("✅ Ack enviado a Kafka");

        } catch (Exception e) {
            System.err.println("❌ Error procesando. NO enviamos Ack.");
            // Al no enviar Ack, Kafka sabe que este mensaje sigue pendiente.
            // Dependiendo de tu config de reintentos, lo volverá a entregar.
        }
    }*/

}
