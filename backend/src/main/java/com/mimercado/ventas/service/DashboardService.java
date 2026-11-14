package com.mimercado.ventas.service;

import com.mimercado.ventas.dto.*;
import com.mimercado.ventas.model.DetalleVenta;
import com.mimercado.ventas.model.EstadoVenta;
import com.mimercado.ventas.model.MedioPago;
import com.mimercado.ventas.model.Venta;
import com.mimercado.ventas.repository.DetalleVentaRepository;
import com.mimercado.ventas.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final VentaRepository ventaRepo;
    private final DetalleVentaRepository detalleRepo;

    public DashboardService(VentaRepository ventaRepo, DetalleVentaRepository detalleRepo) {
        this.ventaRepo = ventaRepo;
        this.detalleRepo = detalleRepo;
    }

    public DashboardResumenDTO obtenerResumen(LocalDate desde, LocalDate hasta) {
        LocalDateTime dtDesde = desde.atStartOfDay();
        LocalDateTime dtHasta = hasta.atTime(LocalTime.MAX);

        // Ventas periodo actual
        List<Venta> ventasActuales = ventaRepo.findVentasEnPeriodo(dtDesde, dtHasta, EstadoVenta.COMPLETADA);
        BigDecimal ventasTotales = ventasActuales.stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long numVentas = ventasActuales.size();

        BigDecimal ticketPromedio = numVentas > 0
                ? ventasTotales.divide(BigDecimal.valueOf(numVentas), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Costo total de los detalles de ventas del periodo actual
        List<DetalleVenta> detallesActuales = detalleRepo.findDetallesEnPeriodoCompletadas(dtDesde, dtHasta);
        BigDecimal costoTotal = detallesActuales.stream()
                .map(d -> d.getCostoUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // RN-05: Margen bruto = ((Ventas netas - Costo total) / Ventas netas) * 100
        BigDecimal margenBruto = BigDecimal.ZERO;
        if (ventasTotales.compareTo(BigDecimal.ZERO) > 0) {
            margenBruto = ventasTotales.subtract(costoTotal)
                    .divide(ventasTotales, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(1, RoundingMode.HALF_UP);
        }

        // RN-08: Periodo anterior (mismo número de días inmediatamente anterior)
        long diasPeriodo = ChronoUnit.DAYS.between(desde, hasta) + 1;
        LocalDate desdeAnt = desde.minusDays(diasPeriodo);
        LocalDate hastaAnt = hasta.minusDays(diasPeriodo);
        LocalDateTime dtDesdeAnt = desdeAnt.atStartOfDay();
        LocalDateTime dtHastaAnt = hastaAnt.atTime(LocalTime.MAX);

        List<Venta> ventasAnteriores = ventaRepo.findVentasEnPeriodo(dtDesdeAnt, dtHastaAnt, EstadoVenta.COMPLETADA);
        BigDecimal ventasTotalesAnt = ventasAnteriores.stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long numVentasAnt = ventasAnteriores.size();

        // Variaciones porcentuales
        BigDecimal varVentas = BigDecimal.ZERO;
        if (ventasTotalesAnt.compareTo(BigDecimal.ZERO) > 0) {
            varVentas = ventasTotales.subtract(ventasTotalesAnt)
                    .divide(ventasTotalesAnt, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(1, RoundingMode.HALF_UP);
        }

        BigDecimal varNumVentas = BigDecimal.ZERO;
        if (numVentasAnt > 0) {
            varNumVentas = BigDecimal.valueOf(numVentas - numVentasAnt)
                    .divide(BigDecimal.valueOf(numVentasAnt), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(1, RoundingMode.HALF_UP);
        }

        DashboardResumenDTO dto = new DashboardResumenDTO();
        dto.setVentasTotales(ventasTotales);
        dto.setNumeroVentas(numVentas);
        dto.setTicketPromedio(ticketPromedio);
        dto.setMargenBruto(margenBruto);
        dto.setVentasTotalesAnterior(ventasTotalesAnt);
        dto.setNumeroVentasAnterior(numVentasAnt);
        dto.setVariacionVentasPorcentaje(varVentas);
        dto.setVariacionNumeroVentasPorcentaje(varNumVentas);

        return dto;
    }

    public List<VentaPorDiaDTO> obtenerVentasPorDia(LocalDate desde, LocalDate hasta) {
        LocalDateTime dtDesde = desde.atStartOfDay();
        LocalDateTime dtHasta = hasta.atTime(LocalTime.MAX);
        List<Venta> ventas = ventaRepo.findVentasEnPeriodo(dtDesde, dtHasta, EstadoVenta.COMPLETADA);

        Map<LocalDate, List<Venta>> porDia = ventas.stream()
                .collect(Collectors.groupingBy(v -> v.getFechaHora().toLocalDate()));

        List<VentaPorDiaDTO> resultado = new ArrayList<>();
        LocalDate cur = desde;
        while (!cur.isAfter(hasta)) {
            List<Venta> ventasDelDia = porDia.getOrDefault(cur, Collections.emptyList());
            BigDecimal totalDia = ventasDelDia.stream()
                    .map(Venta::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            resultado.add(new VentaPorDiaDTO(cur, totalDia, (long) ventasDelDia.size()));
            cur = cur.plusDays(1);
        }

        return resultado;
    }

    public List<VentaPorCategoriaDTO> obtenerVentasPorCategoria(LocalDate desde, LocalDate hasta) {
        LocalDateTime dtDesde = desde.atStartOfDay();
        LocalDateTime dtHasta = hasta.atTime(LocalTime.MAX);
        List<DetalleVenta> detalles = detalleRepo.findDetallesEnPeriodoCompletadas(dtDesde, dtHasta);

        BigDecimal granTotal = detalles.stream()
                .map(DetalleVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<Long, List<DetalleVenta>> porCat = detalles.stream()
                .collect(Collectors.groupingBy(d -> d.getProducto().getCategoria().getId()));

        List<VentaPorCategoriaDTO> resultado = new ArrayList<>();
        for (Map.Entry<Long, List<DetalleVenta>> entry : porCat.entrySet()) {
            List<DetalleVenta> lista = entry.getValue();
            String catNombre = lista.get(0).getProducto().getCategoria().getNombre();
            BigDecimal totalCat = lista.stream()
                    .map(DetalleVenta::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal porcentaje = granTotal.compareTo(BigDecimal.ZERO) > 0
                    ? totalCat.divide(granTotal, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            resultado.add(new VentaPorCategoriaDTO(entry.getKey(), catNombre, totalCat, porcentaje));
        }

        resultado.sort((a, b) -> b.getTotal().compareTo(a.getTotal()));
        return resultado;
    }

    public List<VentaPorMedioPagoDTO> obtenerVentasPorMedioPago(LocalDate desde, LocalDate hasta) {
        LocalDateTime dtDesde = desde.atStartOfDay();
        LocalDateTime dtHasta = hasta.atTime(LocalTime.MAX);
        List<Venta> ventas = ventaRepo.findVentasEnPeriodo(dtDesde, dtHasta, EstadoVenta.COMPLETADA);

        BigDecimal granTotal = ventas.stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<MedioPago, List<Venta>> porMedio = ventas.stream()
                .collect(Collectors.groupingBy(Venta::getMedioPago));

        List<VentaPorMedioPagoDTO> resultado = new ArrayList<>();
        for (MedioPago mp : MedioPago.values()) {
            List<Venta> lista = porMedio.getOrDefault(mp, Collections.emptyList());
            BigDecimal totalMedio = lista.stream()
                    .map(Venta::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal pct = granTotal.compareTo(BigDecimal.ZERO) > 0
                    ? totalMedio.divide(granTotal, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(1, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            resultado.add(new VentaPorMedioPagoDTO(mp, totalMedio, (long) lista.size(), pct));
        }

        return resultado;
    }

    public List<TopProductoDTO> obtenerTopProductos(LocalDate desde, LocalDate hasta, int limite) {
        LocalDateTime dtDesde = desde.atStartOfDay();
        LocalDateTime dtHasta = hasta.atTime(LocalTime.MAX);
        List<DetalleVenta> detalles = detalleRepo.findDetallesEnPeriodoCompletadas(dtDesde, dtHasta);

        Map<Long, List<DetalleVenta>> porProducto = detalles.stream()
                .collect(Collectors.groupingBy(d -> d.getProducto().getId()));

        List<TopProductoDTO> listaTop = new ArrayList<>();
        for (Map.Entry<Long, List<DetalleVenta>> entry : porProducto.entrySet()) {
            List<DetalleVenta> lista = entry.getValue();
            var prod = lista.get(0).getProducto();
            long totalUnidades = lista.stream().mapToLong(DetalleVenta::getCantidad).sum();
            BigDecimal totalVendido = lista.stream()
                    .map(DetalleVenta::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            listaTop.add(new TopProductoDTO(prod.getId(), prod.getNombre(), prod.getCategoria().getNombre(), totalUnidades, totalVendido));
        }

        return listaTop.stream()
                .sorted((a, b) -> Long.compare(b.getUnidadesVendidas(), a.getUnidadesVendidas()))
                .limit(limite)
                .collect(Collectors.toList());
    }
}
