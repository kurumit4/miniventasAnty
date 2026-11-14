package com.mimercado.ventas.controller;

import com.mimercado.ventas.dto.*;
import com.mimercado.ventas.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin(origins = "*")
@Tag(name = "Tablero de Ventas", description = "Métricas agregadas, comparativos y gráficos del dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumen")
    @Operation(summary = "Resumen ejecutivo con 4 KPIs principales y comparativo con periodo anterior")
    public ResponseEntity<DashboardResumenDTO> obtenerResumen(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(dashboardService.obtenerResumen(desde, hasta));
    }

    @GetMapping("/ventas-por-dia")
    @Operation(summary = "Serie temporal de ventas por día en el periodo")
    public ResponseEntity<List<VentaPorDiaDTO>> obtenerVentasPorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(dashboardService.obtenerVentasPorDia(desde, hasta));
    }

    @GetMapping("/ventas-por-categoria")
    @Operation(summary = "Distribución de ventas por categoría con porcentajes")
    public ResponseEntity<List<VentaPorCategoriaDTO>> obtenerVentasPorCategoria(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(dashboardService.obtenerVentasPorCategoria(desde, hasta));
    }

    @GetMapping("/ventas-por-medio-pago")
    @Operation(summary = "Distribución de ventas por medio de pago")
    public ResponseEntity<List<VentaPorMedioPagoDTO>> obtenerVentasPorMedioPago(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(dashboardService.obtenerVentasPorMedioPago(desde, hasta));
    }

    @GetMapping("/top-productos")
    @Operation(summary = "Ranking de productos más vendidos en el periodo")
    public ResponseEntity<List<TopProductoDTO>> obtenerTopProductos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "5") int limite) {
        return ResponseEntity.ok(dashboardService.obtenerTopProductos(desde, hasta, limite));
    }
}
