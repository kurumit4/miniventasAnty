package com.mimercado.ventas;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimercado.ventas.dto.DetalleVentaDTO;
import com.mimercado.ventas.dto.VentaDTO;
import com.mimercado.ventas.model.MedioPago;
import com.mimercado.ventas.model.Producto;
import com.mimercado.ventas.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VentasApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    @DisplayName("I1/I3: GET /api/v1/productos devuelve los 20 productos semilla")
    void testGetProductosDevuelveSemilla() throws Exception {
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(20)));
    }

    @Test
    @DisplayName("I2/I3: GET /api/v1/productos/stock-bajo devuelve exactamente 3 productos")
    void testGetProductosStockBajo() throws Exception {
        mockMvc.perform(get("/api/v1/productos/stock-bajo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("I2/I3: GET /api/v1/productos/baja-rotacion devuelve los 3 productos sin ventas")
    void testGetProductosBajaRotacion() throws Exception {
        mockMvc.perform(get("/api/v1/productos/baja-rotacion?dias=30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("RN-02: Registrar venta con stock insuficiente debe retornar 409 Conflict")
    void testRegistrarVentaStockInsuficiente() throws Exception {
        // Producto 1 tiene stock inicial 4
        VentaDTO venta = new VentaDTO();
        venta.setMedioPago(MedioPago.EFECTIVO);
        DetalleVentaDTO detalle = new DetalleVentaDTO();
        detalle.setProductoId(1L);
        detalle.setCantidad(100); // Excede el stock disponible
        venta.setDetalles(List.of(detalle));

        mockMvc.perform(post("/api/v1/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje", containsString("Stock insuficiente")));
    }

    @Test
    @DisplayName("RN-01/RN-02/RN-06: Registrar venta válida, verificar decremento de stock y anularla")
    void testFlujoCompletoVentaYAnulacion() throws Exception {
        Producto prodAntes = productoRepository.findById(2L).orElseThrow();
        int stockOriginal = prodAntes.getStockActual();

        // 1. Registrar venta de 2 unidades
        VentaDTO venta = new VentaDTO();
        venta.setMedioPago(MedioPago.TARJETA);
        DetalleVentaDTO detalle = new DetalleVentaDTO();
        detalle.setProductoId(2L);
        detalle.setCantidad(2);
        venta.setDetalles(List.of(detalle));
        venta.setDescuento(BigDecimal.ZERO);

        String responseStr = mockMvc.perform(post("/api/v1/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.estado").value("COMPLETADA"))
                .andReturn().getResponse().getContentAsString();

        VentaDTO ventaCreada = objectMapper.readValue(responseStr, VentaDTO.class);
        Long ventaId = ventaCreada.getId();

        // Verificar que el stock disminuyó en 2
        Producto prodDespuesVenta = productoRepository.findById(2L).orElseThrow();
        assertEquals(stockOriginal - 2, prodDespuesVenta.getStockActual());

        // 2. Anular la venta
        mockMvc.perform(post("/api/v1/ventas/" + ventaId + "/anular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ANULADA"));

        // Verificar que el stock se repuso
        Producto prodDespuesAnular = productoRepository.findById(2L).orElseThrow();
        assertEquals(stockOriginal, prodDespuesAnular.getStockActual());

        // 3. Intentar anular por segunda vez debe fallar con 409 Conflict (RN-06)
        mockMvc.perform(post("/api/v1/ventas/" + ventaId + "/anular"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje", containsString("ya se encuentra anulada")));
    }

    @Test
    @DisplayName("RF-01 a RF-07: Dashboard resumen responde correctamente con comparativos")
    void testDashboardResumen() throws Exception {
        LocalDate hoy = LocalDate.now();
        LocalDate desde = hoy.minusDays(30);

        mockMvc.perform(get("/api/v1/dashboard/resumen")
                        .param("desde", desde.toString())
                        .param("hasta", hoy.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ventasTotales").isNumber())
                .andExpect(jsonPath("$.numeroVentas").isNumber())
                .andExpect(jsonPath("$.ticketPromedio").isNumber())
                .andExpect(jsonPath("$.margenBruto").isNumber());
    }
}
