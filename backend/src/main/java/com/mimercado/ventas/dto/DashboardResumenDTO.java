package com.mimercado.ventas.dto;

import java.math.BigDecimal;

public class DashboardResumenDTO {
    private BigDecimal ventasTotales;
    private Long numeroVentas;
    private BigDecimal ticketPromedio;
    private BigDecimal margenBruto; // Porcentaje, ej: 28.5%

    // Comparativo con periodo anterior
    private BigDecimal ventasTotalesAnterior;
    private Long numeroVentasAnterior;
    private BigDecimal variacionVentasPorcentaje;
    private BigDecimal variacionNumeroVentasPorcentaje;

    public DashboardResumenDTO() {}

    public BigDecimal getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(BigDecimal ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

    public Long getNumeroVentas() {
        return numeroVentas;
    }

    public void setNumeroVentas(Long numeroVentas) {
        this.numeroVentas = numeroVentas;
    }

    public BigDecimal getTicketPromedio() {
        return ticketPromedio;
    }

    public void setTicketPromedio(BigDecimal ticketPromedio) {
        this.ticketPromedio = ticketPromedio;
    }

    public BigDecimal getMargenBruto() {
        return margenBruto;
    }

    public void setMargenBruto(BigDecimal margenBruto) {
        this.margenBruto = margenBruto;
    }

    public BigDecimal getVentasTotalesAnterior() {
        return ventasTotalesAnterior;
    }

    public void setVentasTotalesAnterior(BigDecimal ventasTotalesAnterior) {
        this.ventasTotalesAnterior = ventasTotalesAnterior;
    }

    public Long getNumeroVentasAnterior() {
        return numeroVentasAnterior;
    }

    public void setNumeroVentasAnterior(Long numeroVentasAnterior) {
        this.numeroVentasAnterior = numeroVentasAnterior;
    }

    public BigDecimal getVariacionVentasPorcentaje() {
        return variacionVentasPorcentaje;
    }

    public void setVariacionVentasPorcentaje(BigDecimal variacionVentasPorcentaje) {
        this.variacionVentasPorcentaje = variacionVentasPorcentaje;
    }

    public BigDecimal getVariacionNumeroVentasPorcentaje() {
        return variacionNumeroVentasPorcentaje;
    }

    public void setVariacionNumeroVentasPorcentaje(BigDecimal variacionNumeroVentasPorcentaje) {
        this.variacionNumeroVentasPorcentaje = variacionNumeroVentasPorcentaje;
    }
}
