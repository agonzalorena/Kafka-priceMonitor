import { useRef, useState } from "react";
import PriceChart from "./components/PriceChart";

function App() {
  return (
    <main className="h-dvh flex flex-col items-center p-4 gap-5">
      <h1 className="text-2xl font-bold">Monitor de Precios</h1>
      <div className="w-full md:w-1/2">
        <PriceChart />
      </div>
    </main>
  );
}

export default App;
