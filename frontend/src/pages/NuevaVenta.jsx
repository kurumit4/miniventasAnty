import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ventaService } from '../services/ventaService';
import { productoService } from '../services/productoService';
import { formatCOP, MEDIOS_PAGO_LABEL } from '../utils/format';
import { EstadoError } from '../components/Estados';

export default function NuevaVenta() {
  const navigate = useNavigate();
  const [productos, setProductos] = useState([]);
  const [buscar, setBuscar] = useState('');
  const [carrito, setCarrito] = useState([]);
  const [medioPago, setMedioPago] = useState('EFECTIVO');
  const [descuento, setDescuento] = useState('0');
  const [enviando, setEnviando] = useState(false);
  const [error, setError] = useState(null);
  const [exito, setExito] = useState(null);
  const [cargandoProd, setCargandoProd] = useState(false);

  useEffect(() => {
    setCargandoProd(true);
    productoService.listar({ activo: true, buscar: buscar || undefined })
      .then(setProductos)
      .catch(() => setProductos([]))
      .finally(() => setCargandoProd(false));
  }, [buscar]);

  function agregarAlCarrito(prod) {
    setCarrito(prev => {
      const idx = prev.findIndex(i => i.productoId === prod.id);
      if (idx >= 0) {
        const nuevo = [...prev];
        if (nuevo[idx].cantidad < prod.stockActual) {
          nuevo[idx] = { ...nuevo[idx], cantidad: nuevo[idx].cantidad + 1 };
        }
        return nuevo;
      }
      return [...prev, { productoId: prod.id, nombre: prod.nombre, precioUnitario: prod.precioVenta, stockActual: prod.stockActual, cantidad: 1 }];
    });
  }

  function cambiarCantidad(productoId, cantidad) {
    setCarrito(prev => {
      const item = prev.find(i => i.productoId === productoId);
      if (!item) return prev;
      if (cantidad <= 0) return prev.filter(i => i.productoId !== productoId);
      if (cantidad > item.stockActual) return prev;
      return prev.map(i => i.productoId === productoId ? { ...i, cantidad } : i);
    });
  }

  function quitarDelCarrito(productoId) {
    setCarrito(prev => prev.filter(i => i.productoId !== productoId));
  }

  const subtotal = carrito.reduce((sum, i) => sum + i.precioUnitario * i.cantidad, 0);
  const descNum = parseFloat(descuento) || 0;
  const total = Math.max(0, subtotal - descNum);

  async function enviarVenta() {
    if (carrito.length === 0) { setError('Agrega al menos un producto al carrito.'); return; }
    if (descNum < 0 || descNum > subtotal) { setError('El descuento debe ser entre 0 y el subtotal.'); return; }

    setEnviando(true);
    setError(null);
    try {
      const payload = {
        medioPago,
        descuento: descNum,
        detalles: carrito.map(i => ({ productoId: i.productoId, cantidad: i.cantidad })),
      };
      const nueva = await ventaService.registrar(payload);
      setExito(nueva.id);
      setCarrito([]);
    } catch (e) {
      setError(e.message);
    } finally {
      setEnviando(false);
    }
  }

  if (exito) {
    return (
      <div className="page">
        <div className="card confirmacion-exito">
          <div className="icono-exito">✅</div>
          <h2>¡Venta registrada!</h2>
          <p>La venta <strong>#{exito}</strong> fue registrada correctamente.</p>
          <div className="confirmacion-acciones">
            <button className="btn btn-secondary" onClick={() => { setExito(null); }}>Registrar otra venta</button>
            <button className="btn btn-primary" onClick={() => navigate(`/ventas/${exito}`)}>Ver detalle</button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="page">
      <div className="page-header"><h1>➕ Nueva Venta</h1></div>
      {error && <EstadoError mensaje={error} />}

      <div className="nueva-venta-grid">
        {/* Panel izquierdo: buscador de productos */}
        <div className="panel-productos card">
          <h2>Buscar productos</h2>
          <input className="input" placeholder="🔍 Nombre del producto..." value={buscar} onChange={e => setBuscar(e.target.value)} />
          <div className="lista-productos">
            {cargandoProd && <p className="cargando-texto">Buscando...</p>}
            {!cargandoProd && productos.length === 0 && <p className="cargando-texto">Sin resultados.</p>}
            {productos.map(p => (
              <div key={p.id} className={`item-producto ${p.stockActual === 0 ? 'sin-stock' : ''}`}>
                <div>
                  <strong>{p.nombre}</strong>
                  <div className="item-meta">{p.categoriaNombre} · Stock: {p.stockActual}</div>
                </div>
                <div className="item-acciones">
                  <span className="precio-tag">{formatCOP(p.precioVenta)}</span>
                  <button className="btn btn-sm btn-primary" disabled={p.stockActual === 0} onClick={() => agregarAlCarrito(p)}>
                    {p.stockActual === 0 ? 'Sin stock' : '+ Agregar'}
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Panel derecho: carrito */}
        <div className="panel-carrito">
          <div className="card">
            <h2>🛒 Carrito</h2>
            {carrito.length === 0 ? <p className="cargando-texto">El carrito está vacío.</p> : (
              <div className="carrito-items">
                {carrito.map(i => (
                  <div key={i.productoId} className="carrito-item">
                    <div className="carrito-nombre">{i.nombre}</div>
                    <div className="carrito-cantidad">
                      <button className="btn btn-sm btn-ghost" onClick={() => cambiarCantidad(i.productoId, i.cantidad - 1)}>−</button>
                      <span>{i.cantidad}</span>
                      <button className="btn btn-sm btn-ghost" onClick={() => cambiarCantidad(i.productoId, i.cantidad + 1)} disabled={i.cantidad >= i.stockActual}>+</button>
                    </div>
                    <div className="carrito-subtotal">{formatCOP(i.precioUnitario * i.cantidad)}</div>
                    <button className="btn btn-sm btn-danger-ghost" onClick={() => quitarDelCarrito(i.productoId)}>✕</button>
                  </div>
                ))}
              </div>
            )}

            <div className="form-grupo" style={{ marginTop: '1rem' }}>
              <label>Medio de pago</label>
              <select className="select" value={medioPago} onChange={e => setMedioPago(e.target.value)}>
                {Object.entries(MEDIOS_PAGO_LABEL).map(([k, v]) => <option key={k} value={k}>{v}</option>)}
              </select>
            </div>

            <div className="form-grupo">
              <label>Descuento (COP)</label>
              <input className="input" type="number" min="0" max={subtotal} value={descuento}
                onChange={e => setDescuento(e.target.value)} placeholder="0" />
            </div>

            <div className="totales">
              <div className="totales-row"><span>Subtotal</span><span>{formatCOP(subtotal)}</span></div>
              {descNum > 0 && <div className="totales-row descuento"><span>Descuento</span><span>-{formatCOP(descNum)}</span></div>}
              <div className="totales-row total"><span>Total</span><span>{formatCOP(total)}</span></div>
            </div>

            <button className="btn btn-primary btn-full" onClick={enviarVenta} disabled={enviando || carrito.length === 0}>
              {enviando ? 'Registrando...' : '✅ Confirmar Venta'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
