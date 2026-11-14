package com.mimercado.ventas.dto;

import com.mimercado.ventas.model.MedioPago;
import java.math.BigDecimal;

public class VentaPorMedioPagoDTO {
    private MedioPago medioPago;
    private BigDecimal total;
    private Long cantidadVentas;
    private BigDecimal porcentaje;

    public VentaPorMedioPagoDTO() {}

    public VentaPorMedioPagoDTO(MedioPago medioPago, BigDecimal total, Long cantidadVentas, BigDecimal porcentaje) {
        this.medioPago = medioPago;
        this.total = total;
        this.cantidadVentas = cantidadVentas;
        this.porcentaje = porcentaje;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
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

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }
}
