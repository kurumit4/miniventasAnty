package com.mimercado.ventas.dto;

import java.math.BigDecimal;

public class VentaPorCategoriaDTO {
    private Long categoriaId;
    private String categoriaNombre;
    private BigDecimal total;
    private BigDecimal porcentaje;

    public VentaPorCategoriaDTO() {}

    public VentaPorCategoriaDTO(Long categoriaId, String categoriaNombre, BigDecimal total, BigDecimal porcentaje) {
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.total = total;
        this.porcentaje = porcentaje;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }
}
