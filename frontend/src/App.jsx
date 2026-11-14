import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Layout } from './components/Layout';
import Tablero from './pages/Tablero';
import HistorialVentas from './pages/HistorialVentas';
import DetalleVenta from './pages/DetalleVenta';
import NuevaVenta from './pages/NuevaVenta';
import Productos from './pages/Productos';
import BajaRotacion from './pages/BajaRotacion';
import './App.css';

export default function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<Tablero />} />
          <Route path="/ventas" element={<HistorialVentas />} />
          <Route path="/ventas/nueva" element={<NuevaVenta />} />
          <Route path="/ventas/:id" element={<DetalleVenta />} />
          <Route path="/productos" element={<Productos />} />
          <Route path="/productos/baja-rotacion" element={<BajaRotacion />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}
