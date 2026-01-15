# Precio usdt en tiempo real con Spring Boot y Kafka

Este proyecto es una prueba de concepto (PoC) para implementar una arquitectura orientada a eventos utilizando Apache Kafka y Spring Boot. El objetivo es simular un flujo de datos de mercado en tiempo real (precios de USDT) y distribuirlos a una interfaz gráfica.

## 🚀 Arquitectura del Proyecto

El flujo de datos está diseñado para ser totalmente desacoplado y reactivo:

1. Producer Service: Un @Scheduled genera un precio aleatorio de USDT cada 10 segundos.

2. Kafka Broker: Recibe el evento y lo almacena en el tópico t-usdt-prices.

3. Consumer Service: Lee el mensaje del tópico.

4. WebSocket Push: Envía el dato al frontend conectado.

5. Frontend: Renderiza el gráfico en tiempo real.

## 🛠️ Tecnologías

- Java: [17]

- Framework: Spring Boot 3.5.9

- Messaging: Spring Kafka (Apache Kafka)

- Formatos: JSON (Jackson)

- Build Tool: Maven
