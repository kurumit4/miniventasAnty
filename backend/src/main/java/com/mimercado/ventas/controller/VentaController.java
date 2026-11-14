package com.mimercado.ventas.controller;

import com.mimercado.ventas.dto.VentaDTO;
import com.mimercado.ventas.model.EstadoVenta;
import com.mimercado.ventas.model.MedioPago;
import com.mimercado.ventas.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ventas")
@CrossOrigin(origins = "*")
@Tag(name = "Ventas", description = "Operaciones de registro, consulta y anulación de ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    @Operation(summary = "Listar ventas con filtros y paginación")
    public ResponseEntity<Page<VentaDTO>> listarVentas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) MedioPago medioPago,
            @RequestParam(required = false) EstadoVenta estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        LocalDateTime dtDesde = desde != null ? desde.atStartOfDay() : null;
        LocalDateTime dtHasta = hasta != null ? hasta.atTime(LocalTime.MAX) : null;

        Page<VentaDTO> resultado = ventaService.listarVentas(dtDesde, dtHasta, medioPago, estado, PageRequest.of(page, size));
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle completo de una venta")
    public ResponseEntity<VentaDTO> obtenerVentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerVentaPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva venta con validación de stock y cálculo de totales")
    public ResponseEntity<VentaDTO> registrarVenta(@Valid @RequestBody VentaDTO dto) {
        VentaDTO guardada = ventaService.registrarVenta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PostMapping("/{id}/anular")
    @Operation(summary = "Anular una venta y reponer el stock")
    public ResponseEntity<VentaDTO> anularVenta(@PathVariable Long id, @RequestBody(required = false) Map<String, String> body) {
        String motivo = body != null ? body.get("motivo") : null;
        return ResponseEntity.ok(ventaService.anularVenta(id, motivo));
    }
}
