import { useState, useEffect, useCallback } from 'react';
import { productoService } from '../services/productoService';
import { formatCOP } from '../utils/format';
import { EstadoCarga, EstadoError, EstadoVacio } from '../components/Estados';

export default function BajaRotacion() {
  const [dias, setDias] = useState(30);
  const [productos, setProductos] = useState([]);
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState(null);

  const cargar = useCallback(async () => {
    setCargando(true); setError(null);
    try {
      const res = await productoService.bajaRotacion(dias);
      setProductos(res);
    } catch (e) {
      setError(e.message);
    } finally {
      setCargando(false);
    }
  }, [dias]);

  useEffect(() => { cargar(); }, [cargar]);

  return (
    <div className="page">
      <div className="page-header">
        <h1>📉 Productos de Baja Rotación</h1>
        <div className="filtros-inline">
          <label>Sin ventas en los últimos
            <select className="select ml-1" value={dias} onChange={e => setDias(Number(e.target.value))}>
              <option value={7}>7 días</option>
              <option value={15}>15 días</option>
              <option value={30}>30 días</option>
              <option value={60}>60 días</option>
              <option value={90}>90 días</option>
            </select>
            días
          </label>
        </div>
      </div>

      <div className="alerta alerta-info">
        Muestra los productos activos que <strong>no han tenido ninguna venta completada</strong> en los últimos <strong>{dias} días</strong>. Considera promocionarlos o retirarlos del catálogo.
      </div>

      {cargando && <EstadoCarga />}
      {error && <EstadoError mensaje={error} onReintentar={cargar} />}
      {!cargando && !error && (
        productos.length === 0
          ? <EstadoVacio mensaje={`¡Excelente! Todos los productos activos tuvieron ventas en los últimos ${dias} días.`} />
          : (
            <div className="card">
              <p className="tabla-contador">{productos.length} producto(s) sin ventas en {dias} días</p>
              <table className="tabla">
                <thead>
                  <tr><th>Producto</th><th>Categoría</th><th>Precio venta</th><th>Stock</th><th>Stock mín.</th></tr>
                </thead>
                <tbody>
                  {productos.map(p => (
                    <tr key={p.id}>
                      <td><strong>{p.nombre}</strong></td>
                      <td>{p.categoriaNombre}</td>
                      <td>{formatCOP(p.precioVenta)}</td>
                      <td>{p.stockActual}</td>
                      <td>{p.stockMinimo}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )
      )}
    </div>
  );
}
