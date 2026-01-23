import React, { useEffect, useRef } from "react";
import { createChart, ColorType } from "lightweight-charts";
import { Client } from "@stomp/stompjs";

const PriceChart = () => {
  const chartContainerRef = useRef(null);

  useEffect(() => {
    // Guardamos la referencia actual del contenedor para usarla en el cleanup
    const container = chartContainerRef.current;

    if (!container) return;

    // 1. CONFIGURACIÓN DEL GRÁFICO
    const chartOptions = {
      layout: {
        background: { type: ColorType.Solid, color: "#131722" },
        textColor: "#d1d4dc",
      },
      timeScale: {
        timeVisible: true,
        secondsVisible: true,
        barSpacing: 15,
        minBarSpacing: 5,
        rightOffset: 10,
      },
      grid: {
        vertLines: { color: "#2B2B43" },
        horzLines: { color: "#2B2B43" },
      },
      width: container.clientWidth,
      height: 400, // Altura fija para mejor visibilidad
    };

    const chart = createChart(container, chartOptions);

    // 2. CREACIÓN DE LA SERIE
    const baselineSeries = chart.addBaselineSeries({
      baseValue: { type: "price", price: 1500 }, // Valor base para el color
      topLineColor: "#2962FF",
      topFillColor1: "rgba(41, 98, 255, 0.28)",
      topFillColor2: "rgba(41, 98, 255, 0.05)",
      bottomLineColor: "#ef5350",
      bottomFillColor1: "rgba(239, 83, 80, 0.05)",
      bottomFillColor2: "rgba(239, 83, 80, 0.28)",
    });

    // 3. CONFIGURACIÓN STOMP (WebSocket Nativo)
    const stompClient = new Client({
      brokerURL: "ws://localhost:8080/ws-prices",
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    stompClient.onConnect = (frame) => {
      console.log("🟢 Conectado al WebSocket");

      stompClient.subscribe("/topic/test", (mensaje) => {
        try {
          const rawData = JSON.parse(mensaje.body);
          console.log("price", rawData);
          // VALIDACIÓN Y FORMATO DE DATOS
          // Lightweight Charts necesita 'time' en SEGUNDOS
          const timeInSeconds = rawData.timestamp
            ? Math.floor(rawData.timestamp / 1000)
            : Math.floor(Date.now() / 1000);

          const price = parseFloat(rawData.price);

          if (!isNaN(price)) {
            baselineSeries.update({
              time: timeInSeconds,
              value: price,
            });
          }
        } catch (error) {
          console.error("Error parseando mensaje:", error);
        }
      });
    };

    stompClient.onStompError = (frame) => {
      console.error("🔴 Error en STOMP:", frame.headers["message"]);
    };

    // Iniciar la conexión
    stompClient.activate();

    // 4. RESPONSIVIDAD (Resize)
    const handleResize = () => {
      if (container) {
        chart.applyOptions({ width: container.clientWidth });
      }
    };

    window.addEventListener("resize", handleResize);

    // 5. CLEANUP (Esencial para React 18)
    return () => {
      window.removeEventListener("resize", handleResize);
      stompClient.deactivate(); // Cierra la conexión WS
      chart.remove(); // Destruye el gráfico del DOM
    };
  }, []);

  return (
    <div>
      <h2 className="text-white/70">USDT</h2>
      {/* El contenedor del gráfico */}
      <div
        ref={chartContainerRef}
        style={{ position: "relative", width: "100%" }}
      />
    </div>
  );
};

export default PriceChart;
