// Formatea un valor en pesos colombianos (COP)
export function formatCOP(value) {
  if (value === null || value === undefined) return '—';
  return new Intl.NumberFormat('es-CO', {
    style: 'currency',
    currency: 'COP',
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(value);
}

// Formatea una fecha ISO a formato legible Colombia
export function formatFecha(isoString) {
  if (!isoString) return '—';
  return new Intl.DateTimeFormat('es-CO', {
    year: 'numeric',
    month: 'short',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(isoString));
}

// Convierte LocalDate a string ISO (yyyy-MM-dd)
export function hoy() {
  return new Date().toISOString().slice(0, 10);
}

export function diasAtras(n) {
  const d = new Date();
  d.setDate(d.getDate() - n);
  return d.toISOString().slice(0, 10);
}

// Colores para gráficos (paleta profesional)
export const COLORES = ['#2563eb', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#06b6d4'];

export const MEDIOS_PAGO_LABEL = {
  EFECTIVO: 'Efectivo',
  TRANSFERENCIA: 'Transferencia',
  TARJETA: 'Tarjeta',
};

export const ESTADOS_LABEL = {
  COMPLETADA: 'Completada',
  ANULADA: 'Anulada',
};
