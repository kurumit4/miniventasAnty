import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { ventaService } from '../services/ventaService';
import { formatCOP, formatFecha, MEDIOS_PAGO_LABEL, ESTADOS_LABEL } from '../utils/format';
import { EstadoCarga, EstadoError } from '../components/Estados';

export default function DetalleVenta() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [venta, setVenta] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);
  const [anulando, setAnulando] = useState(false);
  const [confirmando, setConfirmando] = useState(false);
  const [motivo, setMotivo] = useState('');

  useEffect(() => {
    ventaService.obtener(id)
      .then(setVenta)
      .catch(e => setError(e.message))
      .finally(() => setCargando(false));
  }, [id]);

  async function anular() {
    setAnulando(true);
    try {
      const actualizada = await ventaService.anular(id, motivo);
      setVenta(actualizada);
      setConfirmando(false);
    } catch (e) {
      setError(e.message);
    } finally {
      setAnulando(false);
    }
  }

  if (cargando) return <EstadoCarga />;
  if (error && !venta) return <EstadoError mensaje={error} onReintentar={() => navigate(0)} />;

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <Link to="/ventas" className="btn btn-ghost btn-sm">‹ Volver al historial</Link>
          <h1>🧾 Venta #{id}</h1>
        </div>
        {venta?.estado === 'COMPLETADA' && !confirmando && (
          <button className="btn btn-danger" onClick={() => setConfirmando(true)}>🗑 Anular venta</button>
        )}
      </div>

      {error && <div className="alerta alerta-error">{error}</div>}

      {confirmando && (
        <div className="card confirmacion">
          <p><strong>¿Confirmas la anulación de la venta #{id}?</strong></p>
          <p>El stock de todos los productos se repondrá. Esta acción no se puede deshacer.</p>
          <input className="input" placeholder="Motivo (opcional)" value={motivo} onChange={e => setMotivo(e.target.value)} />
          <div className="confirmacion-acciones">
            <button className="btn btn-secondary" onClick={() => setConfirmando(false)}>Cancelar</button>
            <button className="btn btn-danger" onClick={anular} disabled={anulando}>
              {anulando ? 'Anulando...' : 'Confirmar anulación'}
            </button>
          </div>
        </div>
      )}

      {venta && (
        <div className="card">
          <div className="detalle-grid">
            <div><span className="detalle-label">Fecha</span><span>{formatFecha(venta.fechaHora)}</span></div>
            <div><span className="detalle-label">Medio de pago</span><span>{MEDIOS_PAGO_LABEL[venta.medioPago]}</span></div>
            <div><span className="detalle-label">Estado</span>
              <span className={`badge badge-${venta.estado.toLowerCase()}`}>{ESTADOS_LABEL[venta.estado]}</span>
            </div>
            {venta.estado === 'ANULADA' && (
              <>
                <div><span className="detalle-label">Anulada el</span><span>{formatFecha(venta.fechaAnulacion)}</span></div>
                <div><span className="detalle-label">Motivo</span><span>{venta.motivoAnulacion || '—'}</span></div>
              </>
            )}
          </div>

          <table className="tabla" style={{ marginTop: '1.5rem' }}>
            <thead>
              <tr><th>Producto</th><th>Cant.</th><th>Precio unit.</th><th>Subtotal</th></tr>
            </thead>
            <tbody>
              {venta.detalles.map(d => (
                <tr key={d.id}>
                  <td>{d.productoNombre}</td>
                  <td>{d.cantidad}</td>
                  <td>{formatCOP(d.precioUnitario)}</td>
                  <td>{formatCOP(d.subtotal)}</td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="totales">
            <div className="totales-row"><span>Subtotal</span><span>{formatCOP(venta.subtotal)}</span></div>
            {venta.descuento > 0 && <div className="totales-row descuento"><span>Descuento</span><span>-{formatCOP(venta.descuento)}</span></div>}
            <div className="totales-row total"><span>Total</span><span>{formatCOP(venta.total)}</span></div>
          </div>
        </div>
      )}
    </div>
  );
}
