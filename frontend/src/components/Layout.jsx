import { NavLink } from 'react-router-dom';

const NAV = [
  { to: '/', label: '📊 Tablero', end: true },
  { to: '/ventas', label: '🧾 Ventas' },
  { to: '/ventas/nueva', label: '➕ Nueva Venta' },
  { to: '/productos', label: '📦 Productos' },
  { to: '/productos/baja-rotacion', label: '📉 Baja Rotación' },
];

export function Layout({ children }) {
  return (
    <div className="layout">
      <nav className="sidebar">
        <div className="sidebar-brand">
          <span className="brand-icon">🏪</span>
          <span className="brand-name">MiMercado<br /><small>Ventas</small></span>
        </div>
        <ul className="nav-links">
          {NAV.map(({ to, label, end }) => (
            <li key={to}>
              <NavLink to={to} end={end} className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}>
                {label}
              </NavLink>
            </li>
          ))}
        </ul>
        <div className="sidebar-footer">
          <small>Datos de prueba · Sin autenticación</small>
        </div>
      </nav>
      <main className="main-content">{children}</main>
    </div>
  );
}
