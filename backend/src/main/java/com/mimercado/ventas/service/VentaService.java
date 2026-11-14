package com.mimercado.ventas.service;

import com.mimercado.ventas.dto.DetalleVentaDTO;
import com.mimercado.ventas.dto.VentaDTO;
import com.mimercado.ventas.exception.RecursoNoEncontradoException;
import com.mimercado.ventas.exception.ReglaNegocioException;
import com.mimercado.ventas.model.*;
import com.mimercado.ventas.repository.ProductoRepository;
import com.mimercado.ventas.repository.VentaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VentaService {

    private final VentaRepository ventaRepo;
    private final ProductoRepository productoRepo;

    public VentaService(VentaRepository ventaRepo, ProductoRepository productoRepo) {
        this.ventaRepo = ventaRepo;
        this.productoRepo = productoRepo;
    }

    public Page<VentaDTO> listarVentas(LocalDateTime desde, LocalDateTime hasta, MedioPago medioPago, EstadoVenta estado, Pageable pageable) {
        return ventaRepo.findVentasFiltradas(desde, hasta, medioPago, estado, pageable)
                .map(this::mapVentaToDTO);
    }

    public VentaDTO obtenerVentaPorId(Long id) {
        Venta v = ventaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
        return mapVentaToDTO(v);
    }

    @Transactional
    public VentaDTO registrarVenta(VentaDTO dto) {
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new ReglaNegocioException("La venta debe contener al menos un producto");
        }

        Venta venta = new Venta();
        venta.setFechaHora(LocalDateTime.now());
        venta.setMedioPago(dto.getMedioPago());
        venta.setEstado(EstadoVenta.COMPLETADA);

        BigDecimal subtotalAcumulado = BigDecimal.ZERO;
        List<DetalleVenta> detalles = new ArrayList<>();

        for (DetalleVentaDTO itemDTO : dto.getDetalles()) {
            Producto prod = productoRepo.findById(itemDTO.getProductoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + itemDTO.getProductoId()));

            // RN-02: solo productos activos
            if (!prod.getActivo()) {
                throw new ReglaNegocioException("El producto '" + prod.getNombre() + "' está inactivo y no se puede vender");
            }

            // RN-02: stock suficiente
            if (prod.getStockActual() < itemDTO.getCantidad()) {
                throw new ReglaNegocioException("Stock insuficiente para el producto '" + prod.getNombre() +
                        "'. Solicitado: " + itemDTO.getCantidad() + ", disponible: " + prod.getStockActual());
            }

            // Descontar stock
            prod.setStockActual(prod.getStockActual() - itemDTO.getCantidad());
            productoRepo.save(prod);

            // Subtotal del ítem
            BigDecimal subtotalItem = prod.getPrecioVenta().multiply(BigDecimal.valueOf(itemDTO.getCantidad()));
            subtotalAcumulado = subtotalAcumulado.add(subtotalItem);

            DetalleVenta d = new DetalleVenta();
            d.setProducto(prod);
            d.setCantidad(itemDTO.getCantidad());
            d.setPrecioUnitario(prod.getPrecioVenta());
            d.setCostoUnitario(prod.getCostoUnitario());
            d.setSubtotal(subtotalItem);

            venta.addDetalle(d);
        }

        // RN-01: validación de descuento
        BigDecimal descuento = dto.getDescuento() != null ? dto.getDescuento() : BigDecimal.ZERO;
        if (descuento.compareTo(BigDecimal.ZERO) < 0 || descuento.compareTo(subtotalAcumulado) > 0) {
            throw new ReglaNegocioException("El descuento debe ser mayor o igual a 0 y no puede superar el subtotal (" + subtotalAcumulado + ")");
        }

        venta.setSubtotal(subtotalAcumulado);
        venta.setDescuento(descuento);
        venta.setTotal(subtotalAcumulado.subtract(descuento));

        Venta guardada = ventaRepo.save(venta);
        return mapVentaToDTO(guardada);
    }

    @Transactional
    public VentaDTO anularVenta(Long id, String motivo) {
        Venta v = ventaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));

        // RN-06: solo se pueden anular ventas COMPLETADA
        if (v.getEstado() == EstadoVenta.ANULADA) {
            throw new ReglaNegocioException("La venta con ID " + id + " ya se encuentra anulada");
        }

        // RN-06: Reponer stock de los productos
        for (DetalleVenta detalle : v.getDetalles()) {
            Producto prod = detalle.getProducto();
            prod.setStockActual(prod.getStockActual() + detalle.getCantidad());
            productoRepo.save(prod);
        }

        v.setEstado(EstadoVenta.ANULADA);
        v.setFechaAnulacion(LocalDateTime.now());
        v.setMotivoAnulacion(motivo != null && !motivo.isBlank() ? motivo.trim() : "Anulación solicitada por el usuario");

        return mapVentaToDTO(ventaRepo.save(v));
    }

    private VentaDTO mapVentaToDTO(Venta v) {
        VentaDTO dto = new VentaDTO();
        dto.setId(v.getId());
        dto.setFechaHora(v.getFechaHora());
        dto.setMedioPago(v.getMedioPago());
        dto.setSubtotal(v.getSubtotal());
        dto.setDescuento(v.getDescuento());
        dto.setTotal(v.getTotal());
        dto.setEstado(v.getEstado());
        dto.setFechaAnulacion(v.getFechaAnulacion());
        dto.setMotivoAnulacion(v.getMotivoAnulacion());

        List<DetalleVentaDTO> detallesDTO = v.getDetalles().stream().map(d -> {
            DetalleVentaDTO item = new DetalleVentaDTO();
            item.setId(d.getId());
            item.setProductoId(d.getProducto().getId());
            item.setProductoNombre(d.getProducto().getNombre());
            item.setCantidad(d.getCantidad());
            item.setPrecioUnitario(d.getPrecioUnitario());
            item.setCostoUnitario(d.getCostoUnitario());
            item.setSubtotal(d.getSubtotal());
            return item;
        }).collect(Collectors.toList());

        dto.setDetalles(detallesDTO);
        return dto;
    }
}
