import { useState, useEffect, useCallback } from 'react';
import { productoService } from '../services/productoService';
import { formatCOP, ESTADOS_LABEL } from '../utils/format';
import { EstadoCarga, EstadoError, EstadoVacio } from '../components/Estados';

function ModalProducto({ producto, categorias, onGuardar, onCerrar }) {
  const [form, setForm] = useState(producto || {
    nombre: '', categoriaId: categorias[0]?.id || '', precioVenta: '', costoUnitario: '', stockActual: '', stockMinimo: '', activo: true
  });
  const [guardando, setGuardando] = useState(false);
  const [error, setError] = useState(null);

  async function handleSubmit(e) {
    e.preventDefault();
    setGuardando(true); setError(null);
    try {
      if (producto?.id) {
        await productoService.actualizar(producto.id, form);
      } else {
        await productoService.crear(form);
      }
      onGuardar();
    } catch (err) {
      setError(err.message);
    } finally {
      setGuardando(false);
    }
  }

  return (
    <div className="modal-overlay">
      <div className="modal">
        <div className="modal-header">
          <h2>{producto?.id ? 'Editar Producto' : 'Nuevo Producto'}</h2>
          <button className="btn btn-ghost" onClick={onCerrar}>✕</button>
        </div>
        {error && <div className="alerta alerta-error">{error}</div>}
        <form onSubmit={handleSubmit} className="form-stack">
          <div className="form-grupo">
            <label>Nombre*</label>
            <input className="input" required maxLength={120} value={form.nombre} onChange={e => setForm({ ...form, nombre: e.target.value })} />
          </div>
          <div className="form-grupo">
            <label>Categoría*</label>
            <select className="select" required value={form.categoriaId} onChange={e => setForm({ ...form, categoriaId: parseInt(e.target.value) })}>
              {categorias.map(c => <option key={c.id} value={c.id}>{c.nombre}</option>)}
            </select>
          </div>
          <div className="form-fila-2">
            <div className="form-grupo">
              <label>Precio venta (COP)*</label>
              <input className="input" type="number" required min="0" value={form.precioVenta} onChange={e => setForm({ ...form, precioVenta: e.target.value })} />
            </div>
            <div className="form-grupo">
              <label>Costo unitario (COP)*</label>
              <input className="input" type="number" required min="0" value={form.costoUnitario} onChange={e => setForm({ ...form, costoUnitario: e.target.value })} />
            </div>
          </div>
          <div className="form-fila-2">
            <div className="form-grupo">
              <label>Stock actual*</label>
              <input className="input" type="number" required min="0" value={form.stockActual} onChange={e => setForm({ ...form, stockActual: parseInt(e.target.value) })} />
            </div>
            <div className="form-grupo">
              <label>Stock mínimo*</label>
              <input className="input" type="number" required min="0" value={form.stockMinimo} onChange={e => setForm({ ...form, stockMinimo: parseInt(e.target.value) })} />
            </div>
          </div>
          <div className="form-grupo">
            <label className="checkbox-label">
              <input type="checkbox" checked={form.activo} onChange={e => setForm({ ...form, activo: e.target.checked })} />
              Producto activo
            </label>
          </div>
          <div className="modal-footer">
            <button type="button" className="btn btn-secondary" onClick={onCerrar}>Cancelar</button>
            <button type="submit" className="btn btn-primary" disabled={guardando}>{guardando ? 'Guardando...' : 'Guardar'}</button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default function Productos() {
  const [productos, setProductos] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [buscar, setBuscar] = useState('');
  const [categoriaId, setCategoriaId] = useState('');
  const [soloActivos, setSoloActivos] = useState('');
  const [cargando, setCargando] = useState(false);
  const [error, setError] = useState(null);
  const [modal, setModal] = useState(null); // null | 'nuevo' | producto

  const cargar = useCallback(async () => {
    setCargando(true); setError(null);
    try {
      const [prods, cats] = await Promise.all([
        productoService.listar({ categoriaId: categoriaId || undefined, activo: soloActivos !== '' ? soloActivos === 'true' : undefined, buscar: buscar || undefined }),
        productoService.listarCategorias(),
      ]);
      setProductos(prods);
      setCategorias(cats);
    } catch (e) {
      setError(e.message);
    } finally {
      setCargando(false);
    }
  }, [buscar, categoriaId, soloActivos]);

  useEffect(() => { cargar(); }, [cargar]);

  async function toggleActivo(prod) {
    try {
      await productoService.cambiarActivo(prod.id, !prod.activo);
      cargar();
    } catch (e) {
      alert(e.message);
    }
  }

  return (
    <div className="page">
      <div className="page-header">
        <h1>📦 Catálogo de Productos</h1>
        <button className="btn btn-primary" onClick={() => setModal('nuevo')}>+ Nuevo Producto</button>
      </div>

      <div className="card filtros-card">
        <div className="filtros-row">
          <input className="input" placeholder="🔍 Buscar..." value={buscar} onChange={e => setBuscar(e.target.value)} />
          <select className="select" value={categoriaId} onChange={e => setCategoriaId(e.target.value)}>
            <option value="">Todas las categorías</option>
            {categorias.map(c => <option key={c.id} value={c.id}>{c.nombre}</option>)}
          </select>
          <select className="select" value={soloActivos} onChange={e => setSoloActivos(e.target.value)}>
            <option value="">Todos</option>
            <option value="true">Solo activos</option>
            <option value="false">Solo inactivos</option>
          </select>
        </div>
      </div>

      {cargando && <EstadoCarga />}
      {error && <EstadoError mensaje={error} onReintentar={cargar} />}
      {!cargando && !error && (
        productos.length === 0 ? <EstadoVacio /> : (
          <div className="card">
            <table className="tabla">
              <thead>
                <tr><th>Nombre</th><th>Categoría</th><th>Precio venta</th><th>Stock</th><th>Mín.</th><th>Estado</th><th>Acciones</th></tr>
              </thead>
              <tbody>
                {productos.map(p => (
                  <tr key={p.id} className={!p.activo ? 'fila-inactiva' : ''}>
                    <td>
                      <strong>{p.nombre}</strong>
                      {p.stockBajo && <span className="badge badge-alerta ml-1">⚠ Stock bajo</span>}
                    </td>
                    <td>{p.categoriaNombre}</td>
                    <td>{formatCOP(p.precioVenta)}</td>
                    <td className={p.stockBajo ? 'texto-peligro' : ''}>{p.stockActual}</td>
                    <td>{p.stockMinimo}</td>
                    <td><span className={`badge badge-${p.activo ? 'completada' : 'anulada'}`}>{p.activo ? 'Activo' : 'Inactivo'}</span></td>
                    <td>
                      <button className="btn btn-sm btn-ghost" onClick={() => setModal(p)}>✏ Editar</button>
                      <button className="btn btn-sm btn-ghost" onClick={() => toggleActivo(p)}>
                        {p.activo ? '🔴 Desactivar' : '🟢 Activar'}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )
      )}

      {modal !== null && (
        <ModalProducto
          producto={modal === 'nuevo' ? null : modal}
          categorias={categorias}
          onGuardar={() => { setModal(null); cargar(); }}
          onCerrar={() => setModal(null)}
        />
      )}
    </div>
  );
}
