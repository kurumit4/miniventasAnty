import { api } from './api';

export const productoService = {
  listar: ({ categoriaId, activo, buscar } = {}) => {
    const params = new URLSearchParams();
    if (categoriaId) params.append('categoriaId', categoriaId);
    if (activo !== undefined && activo !== null) params.append('activo', activo);
    if (buscar) params.append('buscar', buscar);
    return api.get(`/productos?${params}`);
  },

  obtener: (id) => api.get(`/productos/${id}`),

  stockBajo: () => api.get('/productos/stock-bajo'),

  bajaRotacion: (dias = 30) => api.get(`/productos/baja-rotacion?dias=${dias}`),

  crear: (producto) => api.post('/productos', producto),

  actualizar: (id, producto) => api.put(`/productos/${id}`, producto),

  cambiarActivo: (id, activo) =>
    api.patch(`/productos/${id}/activo?activo=${activo}`),

  listarCategorias: () => api.get('/categorias'),
};
