import { api } from './api';

export const ventaService = {
  listar: ({ desde, hasta, medioPago, estado, page = 0, size = 10 } = {}) => {
    const params = new URLSearchParams();
    if (desde) params.append('desde', desde);
    if (hasta) params.append('hasta', hasta);
    if (medioPago) params.append('medioPago', medioPago);
    if (estado) params.append('estado', estado);
    params.append('page', page);
    params.append('size', size);
    return api.get(`/ventas?${params}`);
  },

  obtener: (id) => api.get(`/ventas/${id}`),

  registrar: (venta) => api.post('/ventas', venta),

  anular: (id, motivo) => api.post(`/ventas/${id}/anular`, { motivo }),
};
