import { useState, useEffect, useCallback } from 'react';
import { dashboardService } from '../services/dashboardService';
import { formatCOP, hoy, diasAtras, COLORES, MEDIOS_PAGO_LABEL } from '../utils/format';
import { EstadoCarga, EstadoError, EstadoVacio } from '../components/Estados';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  PieChart, Pie, Cell, Legend
} from 'recharts';

function KpiCard({ titulo, valor, variacion, formato = 'cop' }) {
  const signo = variacion > 0 ? '+' : '';
  const colorVar = variacion >= 0 ? 'var(--verde)' : 'var(--rojo)';
  return (
    <div className="kpi-card">
      <p className="kpi-titulo">{titulo}</p>
      <p className="kpi-valor">{formato === 'cop' ? formatCOP(valor) : valor}</p>
      {variacion !== undefined && (
        <p className="kpi-variacion" style={{ color: colorVar }}>
          {signo}{variacion}% vs periodo anterior
        </p>
      )}
    </div>
  );
}

const PERIODOS = [
  { label: 'Hoy', desde: hoy(), hasta: hoy() },
  { label: 'Últimos 7 días', desde: diasAtras(6), hasta: hoy() },
  { label: 'Últimos 30 días', desde: diasAtras(29), hasta: hoy() },
  { label: 'Este mes', desde: `${hoy().slice(0, 7)}-01`, hasta: hoy() },
];

export default function Tablero() {
  const [desde, setDesde] = useState(diasAtras(29));
  const [hasta, setHasta] = useState(hoy());
  const [resumen, setResumen] = useState(null);
  const [porDia, setPorDia] = useState([]);
  const [porCategoria, setPorCategoria] = useState([]);
  const [porMedioPago, setPorMedioPago] = useState([]);
  const [topProductos, setTopProductos] = useState([]);
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState(null);

  const cargar = useCallback(async () => {
    setCargando(true);
    setError(null);
    try {
      const [r, dias, cat, medio, top] = await Promise.all([
        dashboardService.resumen(desde, hasta),
        dashboardService.ventasPorDia(desde, hasta),
        dashboardService.ventasPorCategoria(desde, hasta),
        dashboardService.ventasPorMedioPago(desde, hasta),
        dashboardService.topProductos(desde, hasta, 5),
      ]);
      setResumen(r);
      setPorDia(dias);
      setPorCategoria(cat);
      setPorMedioPago(medio);
      setTopProductos(top);
    } catch (e) {
      setError(e.message);
    } finally {
      setCargando(false);
    }
  }, [desde, hasta]);

  useEffect(() => { cargar(); }, [cargar]);

  const aplicarPeriodo = (p) => { setDesde(p.desde); setHasta(p.hasta); };

  if (cargando) return <EstadoCarga texto="Cargando tablero..." />;
  if (error) return <EstadoError mensaje={error} onReintentar={cargar} />;

  return (
    <div className="page">
      <div className="page-header">
        <h1>📊 Tablero de Ventas</h1>
        <div className="filtros-inline">
          <div className="periodo-rapido">
            {PERIODOS.map((p) => (
              <button key={p.label} className={`btn btn-sm ${desde === p.desde && hasta === p.hasta ? 'btn-primary' : 'btn-ghost'}`}
                onClick={() => aplicarPeriodo(p)}>
                {p.label}
              </button>
            ))}
          </div>
          <input type="date" className="input-fecha" value={desde} max={hasta} onChange={(e) => setDesde(e.target.value)} />
          <span>–</span>
          <input type="date" className="input-fecha" value={hasta} min={desde} onChange={(e) => setHasta(e.target.value)} />
        </div>
      </div>

      {/* KPIs */}
      {resumen ? (
        <div className="kpi-grid">
          <KpiCard titulo="Ventas Totales" valor={resumen.ventasTotales} variacion={resumen.variacionVentasPorcentaje} />
          <KpiCard titulo="Número de Ventas" valor={resumen.numeroVentas} variacion={resumen.variacionNumeroVentasPorcentaje} formato="num" />
          <KpiCard titulo="Ticket Promedio" valor={resumen.ticketPromedio} formato="cop" />
          <KpiCard titulo="Margen Bruto" valor={`${resumen.margenBruto}%`} formato="str" />
        </div>
      ) : <EstadoVacio mensaje="Sin ventas en el periodo seleccionado." />}

      {/* Gráficos */}
      <div className="graficos-grid">
        {/* Ventas por día */}
        <div className="card">
          <h2 className="card-titulo">Ventas por Día</h2>
          {porDia.length === 0 ? <EstadoVacio /> : (
            <ResponsiveContainer width="100%" height={220}>
              <BarChart data={porDia} margin={{ left: 10, right: 10, top: 5, bottom: 5 }}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="fecha" tick={{ fontSize: 11 }} />
                <YAxis tickFormatter={(v) => `$${(v / 1000).toFixed(0)}k`} tick={{ fontSize: 11 }} />
                <Tooltip formatter={(v) => formatCOP(v)} />
                <Bar dataKey="total" name="Total" fill={COLORES[0]} radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          )}
        </div>

        {/* Ventas por categoría */}
        <div className="card">
          <h2 className="card-titulo">Por Categoría</h2>
          {porCategoria.length === 0 ? <EstadoVacio /> : (
            <ResponsiveContainer width="100%" height={220}>
              <PieChart>
                <Pie data={porCategoria} dataKey="total" nameKey="categoriaNombre" cx="50%" cy="50%" outerRadius={80} label={({ name, porcentaje }) => `${name} ${porcentaje}%`}>
                  {porCategoria.map((_, i) => <Cell key={i} fill={COLORES[i % COLORES.length]} />)}
                </Pie>
                <Tooltip formatter={(v) => formatCOP(v)} />
              </PieChart>
            </ResponsiveContainer>
          )}
        </div>

        {/* Medios de pago */}
        <div className="card">
          <h2 className="card-titulo">Medios de Pago</h2>
          {porMedioPago.filter(m => m.cantidadVentas > 0).length === 0 ? <EstadoVacio /> : (
            <ResponsiveContainer width="100%" height={220}>
              <PieChart>
                <Pie data={porMedioPago.filter(m => m.cantidadVentas > 0)}
                  dataKey="total" nameKey="medioPago" cx="50%" cy="50%" outerRadius={80}
                  label={({ medioPago, porcentaje }) => `${MEDIOS_PAGO_LABEL[medioPago] || medioPago} ${porcentaje}%`}>
                  {porMedioPago.filter(m => m.cantidadVentas > 0).map((_, i) => <Cell key={i} fill={COLORES[i + 2]} />)}
                </Pie>
                <Tooltip formatter={(v) => formatCOP(v)} />
              </PieChart>
            </ResponsiveContainer>
          )}
        </div>

        {/* Top 5 productos */}
        <div className="card">
          <h2 className="card-titulo">Top 5 Productos</h2>
          {topProductos.length === 0 ? <EstadoVacio /> : (
            <table className="tabla">
              <thead>
                <tr><th>#</th><th>Producto</th><th>Unidades</th><th>Total</th></tr>
              </thead>
              <tbody>
                {topProductos.map((p, i) => (
                  <tr key={p.productoId}>
                    <td><strong>{i + 1}</strong></td>
                    <td>{p.productoNombre}</td>
                    <td>{p.unidadesVendidas}</td>
                    <td>{formatCOP(p.totalVendido)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
}
