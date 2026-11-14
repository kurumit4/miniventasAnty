package com.mimercado.ventas.dto;

import java.math.BigDecimal;

public class TopProductoDTO {
    private Long productoId;
    private String productoNombre;
    private String categoriaNombre;
    private Long unidadesVendidas;
    private BigDecimal totalVendido;

    public TopProductoDTO() {}

    public TopProductoDTO(Long productoId, String productoNombre, String categoriaNombre, Long unidadesVendidas, BigDecimal totalVendido) {
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.categoriaNombre = categoriaNombre;
        this.unidadesVendidas = unidadesVendidas;
        this.totalVendido = totalVendido;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public Long getUnidadesVendidas() {
        return unidadesVendidas;
    }

    public void setUnidadesVendidas(Long unidadesVendidas) {
        this.unidadesVendidas = unidadesVendidas;
    }

    public BigDecimal getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(BigDecimal totalVendido) {
        this.totalVendido = totalVendido;
    }
}
