package com.mimercado.ventas.controller;

import com.mimercado.ventas.dto.CategoriaDTO;
import com.mimercado.ventas.dto.ProductoDTO;
import com.mimercado.ventas.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
@Tag(name = "Productos y Categorías", description = "Gestión de catálogo y rotación")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/categorias")
    @Operation(summary = "Listar todas las categorías")
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        return ResponseEntity.ok(productoService.listarCategorias());
    }

    @GetMapping("/productos")
    @Operation(summary = "Listar productos con filtros opcionales")
    public ResponseEntity<List<ProductoDTO>> listarProductos(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String buscar) {
        return ResponseEntity.ok(productoService.listarProductos(categoriaId, activo, buscar));
    }

    @GetMapping("/productos/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<ProductoDTO> obtenerProductoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
    }

    @GetMapping("/productos/stock-bajo")
    @Operation(summary = "Listar productos con stock bajo o agotado")
    public ResponseEntity<List<ProductoDTO>> listarStockBajo() {
        return ResponseEntity.ok(productoService.listarStockBajo());
    }

    @GetMapping("/productos/baja-rotacion")
    @Operation(summary = "Listar productos sin ventas en N días")
    public ResponseEntity<List<ProductoDTO>> listarBajaRotacion(
            @RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(productoService.listarBajaRotacion(dias));
    }

    @PostMapping("/productos")
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoDTO dto) {
        ProductoDTO creado = productoService.crearProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/productos/{id}")
    @Operation(summary = "Actualizar producto existente")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, dto));
    }

    @PatchMapping("/productos/{id}/activo")
    @Operation(summary = "Activar o desactivar producto")
    public ResponseEntity<ProductoDTO> cambiarEstadoActivo(@PathVariable Long id, @RequestParam boolean activo) {
        return ResponseEntity.ok(productoService.cambiarEstadoActivo(id, activo));
    }
}
