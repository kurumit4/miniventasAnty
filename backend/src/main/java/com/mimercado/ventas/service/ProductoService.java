package com.mimercado.ventas.service;

import com.mimercado.ventas.dto.CategoriaDTO;
import com.mimercado.ventas.dto.ProductoDTO;
import com.mimercado.ventas.exception.RecursoNoEncontradoException;
import com.mimercado.ventas.exception.ReglaNegocioException;
import com.mimercado.ventas.model.Categoria;
import com.mimercado.ventas.model.Producto;
import com.mimercado.ventas.repository.CategoriaRepository;
import com.mimercado.ventas.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepo;
    private final CategoriaRepository categoriaRepo;

    public ProductoService(ProductoRepository productoRepo, CategoriaRepository categoriaRepo) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
    }

    public List<CategoriaDTO> listarCategorias() {
        return categoriaRepo.findAll().stream()
                .map(this::mapCategoriaToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarProductos(Long categoriaId, Boolean activo, String buscar) {
        return productoRepo.filtrar(categoriaId, activo, buscar).stream()
                .map(this::mapProductoToDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto p = productoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));
        return mapProductoToDTO(p);
    }

    public List<ProductoDTO> listarStockBajo() {
        return productoRepo.findStockBajo().stream()
                .map(this::mapProductoToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarBajaRotacion(int dias) {
        LocalDateTime desde = LocalDateTime.now().minusDays(dias);
        return productoRepo.findBajaRotacion(desde).stream()
                .map(this::mapProductoToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductoDTO crearProducto(ProductoDTO dto) {
        Categoria cat = categoriaRepo.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

        if (dto.getPrecioVenta().compareTo(dto.getCostoUnitario()) < 0) {
            throw new ReglaNegocioException("El precio de venta no puede ser inferior al costo unitario");
        }

        Producto p = new Producto();
        p.setNombre(dto.getNombre().trim());
        p.setCategoria(cat);
        p.setPrecioVenta(dto.getPrecioVenta());
        p.setCostoUnitario(dto.getCostoUnitario());
        p.setStockActual(dto.getStockActual());
        p.setStockMinimo(dto.getStockMinimo());
        p.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        return mapProductoToDTO(productoRepo.save(p));
    }

    @Transactional
    public ProductoDTO actualizarProducto(Long id, ProductoDTO dto) {
        Producto p = productoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        Categoria cat = categoriaRepo.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

        if (dto.getPrecioVenta().compareTo(dto.getCostoUnitario()) < 0) {
            throw new ReglaNegocioException("El precio de venta no puede ser inferior al costo unitario");
        }

        p.setNombre(dto.getNombre().trim());
        p.setCategoria(cat);
        p.setPrecioVenta(dto.getPrecioVenta());
        p.setCostoUnitario(dto.getCostoUnitario());
        p.setStockActual(dto.getStockActual());
        p.setStockMinimo(dto.getStockMinimo());
        if (dto.getActivo() != null) {
            p.setActivo(dto.getActivo());
        }

        return mapProductoToDTO(productoRepo.save(p));
    }

    @Transactional
    public ProductoDTO cambiarEstadoActivo(Long id, boolean activo) {
        Producto p = productoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));
        p.setActivo(activo);
        return mapProductoToDTO(productoRepo.save(p));
    }

    private ProductoDTO mapProductoToDTO(Producto p) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setCategoriaId(p.getCategoria().getId());
        dto.setCategoriaNombre(p.getCategoria().getNombre());
        dto.setPrecioVenta(p.getPrecioVenta());
        dto.setCostoUnitario(p.getCostoUnitario());
        dto.setStockActual(p.getStockActual());
        dto.setStockMinimo(p.getStockMinimo());
        dto.setActivo(p.getActivo());
        dto.setStockBajo(p.getActivo() && p.getStockActual() <= p.getStockMinimo());
        return dto;
    }

    private CategoriaDTO mapCategoriaToDTO(Categoria c) {
        return new CategoriaDTO(c.getId(), c.getNombre(), c.getDescripcion());
    }
}
