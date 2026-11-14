package com.mimercado.ventas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VentaPorDiaDTO {
    private LocalDate fecha;
    private BigDecimal total;
    private Long cantidadVentas;

    public VentaPorDiaDTO() {}

    public VentaPorDiaDTO(LocalDate fecha, BigDecimal total, Long cantidadVentas) {
        this.fecha = fecha;
        this.total = total;
        this.cantidadVentas = cantidadVentas;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getCantidadVentas() {
        return cantidadVentas;
    }

    public void setCantidadVentas(Long cantidadVentas) {
        this.cantidadVentas = cantidadVentas;
    }
}
