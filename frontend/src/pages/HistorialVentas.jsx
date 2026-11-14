import { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { ventaService } from '../services/ventaService';
import { formatCOP, formatFecha, hoy, diasAtras, MEDIOS_PAGO_LABEL, ESTADOS_LABEL } from '../utils/format';
import { EstadoCarga, EstadoError, EstadoVacio } from '../components/Estados';

export default function HistorialVentas() {
  const [desde, setDesde] = useState(diasAtras(29));
  const [hasta, setHasta] = useState(hoy());
  const [medioPago, setMedioPago] = useState('');
  const [estado, setEstado] = useState('');
  const [page, setPage] = useState(0);
  const [data, setData] = useState(null);
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState(null);

  const cargar = useCallback(async () => {
    setCargando(true);
    setError(null);
    try {
      const res = await ventaService.listar({ desde, hasta, medioPago: medioPago || undefined, estado: estado || undefined, page, size: 10 });
      setData(res);
    } catch (e) {
      setError(e.message);
    } finally {
      setCargando(false);
    }
  }, [desde, hasta, medioPago, estado, page]);

  useEffect(() => { cargar(); }, [cargar]);

  const badgeEstado = (est) => <span className={`badge badge-${est.toLowerCase()}`}>{ESTADOS_LABEL[est] || est}</span>;

  return (
    <div className="page">
      <div className="page-header">
        <h1>🧾 Historial de Ventas</h1>
        <Link to="/ventas/nueva" className="btn btn-primary">➕ Nueva Venta</Link>
      </div>

      {/* Filtros */}
      <div className="card filtros-card">
        <div className="filtros-row">
          <label>Desde <input type="date" className="input-fecha" value={desde} max={hasta} onChange={e => { setDesde(e.target.value); setPage(0); }} /></label>
          <label>Hasta <input type="date" className="input-fecha" value={hasta} min={desde} onChange={e => { setHasta(e.target.value); setPage(0); }} /></label>
          <label>Medio de pago
            <select className="select" value={medioPago} onChange={e => { setMedioPago(e.target.value); setPage(0); }}>
              <option value="">Todos</option>
              {Object.entries(MEDIOS_PAGO_LABEL).map(([k, v]) => <option key={k} value={k}>{v}</option>)}
            </select>
          </label>
          <label>Estado
            <select className="select" value={estado} onChange={e => { setEstado(e.target.value); setPage(0); }}>
              <option value="">Todos</option>
              {Object.entries(ESTADOS_LABEL).map(([k, v]) => <option key={k} value={k}>{v}</option>)}
            </select>
          </label>
        </div>
      </div>

      {cargando && <EstadoCarga />}
      {error && <EstadoError mensaje={error} onReintentar={cargar} />}
      {!cargando && !error && data && (
        <>
          {data.content.length === 0 ? <EstadoVacio mensaje="No hay ventas que coincidan con los filtros." /> : (
            <div className="card">
              <table className="tabla">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Fecha y hora</th>
                    <th>Medio de pago</th>
                    <th>Total</th>
                    <th>Estado</th>
                    <th>Acción</th>
                  </tr>
                </thead>
                <tbody>
                  {data.content.map(v => (
                    <tr key={v.id}>
                      <td><strong>{v.id}</strong></td>
                      <td>{formatFecha(v.fechaHora)}</td>
                      <td>{MEDIOS_PAGO_LABEL[v.medioPago] || v.medioPago}</td>
                      <td>{formatCOP(v.total)}</td>
                      <td>{badgeEstado(v.estado)}</td>
                      <td><Link to={`/ventas/${v.id}`} className="btn btn-sm btn-ghost">Ver detalle →</Link></td>
                    </tr>
                  ))}
                </tbody>
              </table>
              {/* Paginación */}
              <div className="paginacion">
                <button className="btn btn-sm btn-ghost" disabled={page === 0} onClick={() => setPage(p => p - 1)}>‹ Anterior</button>
                <span>Página {page + 1} de {data.totalPages}</span>
                <button className="btn btn-sm btn-ghost" disabled={page >= data.totalPages - 1} onClick={() => setPage(p => p + 1)}>Siguiente ›</button>
              </div>
            </div>
          )}
        </>
      )}
    </div>
  );
}
