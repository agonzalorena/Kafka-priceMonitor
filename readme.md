# Precio usdt en tiempo real con Spring Boot y Kafka

Este proyecto es una prueba de concepto (PoC) para implementar una arquitectura orientada a eventos utilizando Apache Kafka y Spring Boot. El objetivo es simular un flujo de datos de mercado en tiempo real (precios de USDT) y distribuirlos a una interfaz gráfica.

## 🚀 Arquitectura del Proyecto

El flujo de datos está diseñado para ser totalmente desacoplado y reactivo:

1. Producer Service: Genera eventos de mercado simulados (precios de USDT) mediante tareas programadas (@Scheduled) y los publica en el tópico crypto-prices.

2. Kafka Broker: Actúa como la columna vertebral de mensajería, desacoplando los servicios y garantizando la persistencia y entrega de los eventos.

3. Analyzer Service: Consume eventos de crypto-prices en tiempo real. Aplica lógica de negocio para detectar tendencias o variaciones bruscas y, si es necesario, emite eventos de alerta al tópico alerts.

4. Consumer Service (WebSocket Gateway): Un servicio integrador que escucha ambos tópicos (crypto-prices y alerts) y retransmite la información hacia el frontend mediante WebSockets en tiempo real.

5. Frontend: Interfaz reactiva que se suscribe a los WebSockets para renderizar gráficos de precios y mostrar notificaciones de alertas instantáneas al usuario.

## 🛠️ Tecnologías

- Java: [17]

- Framework: Spring Boot 3.5.9

- Messaging: Spring Kafka (Apache Kafka)

- Formatos: JSON (Jackson)

- Build Tool: Maven
