import { api } from './api';

export const dashboardService = {
  resumen: (desde, hasta) =>
    api.get(`/dashboard/resumen?desde=${desde}&hasta=${hasta}`),

  ventasPorDia: (desde, hasta) =>
    api.get(`/dashboard/ventas-por-dia?desde=${desde}&hasta=${hasta}`),

  ventasPorCategoria: (desde, hasta) =>
    api.get(`/dashboard/ventas-por-categoria?desde=${desde}&hasta=${hasta}`),

  ventasPorMedioPago: (desde, hasta) =>
    api.get(`/dashboard/ventas-por-medio-pago?desde=${desde}&hasta=${hasta}`),

  topProductos: (desde, hasta, limite = 5) =>
    api.get(`/dashboard/top-productos?desde=${desde}&hasta=${hasta}&limite=${limite}`),
};
