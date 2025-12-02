package com.example.bizly1.models;

import java.io.Serializable;

public class ProductoVentaInsumo implements Serializable {
    
    private Integer id;
    private Integer productoVentaId;
    private Integer insumoId;
    private Double cantidadUsada;
    
    // Para uso local (no se serializa a JSON)
    private Insumo insumo;
    
    public ProductoVentaInsumo() {
    }
    
    public ProductoVentaInsumo(Integer productoVentaId, Integer insumoId, Double cantidadUsada) {
        this.productoVentaId = productoVentaId;
        this.insumoId = insumoId;
        this.cantidadUsada = cantidadUsada;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getProductoVentaId() {
        return productoVentaId;
    }
    
    public void setProductoVentaId(Integer productoVentaId) {
        this.productoVentaId = productoVentaId;
    }
    
    public Integer getInsumoId() {
        return insumoId;
    }
    
    public void setInsumoId(Integer insumoId) {
        this.insumoId = insumoId;
    }
    
    public Double getCantidadUsada() {
        return cantidadUsada;
    }
    
    public void setCantidadUsada(Double cantidadUsada) {
        this.cantidadUsada = cantidadUsada;
    }
    
    public Insumo getInsumo() {
        return insumo;
    }
    
    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }
    
    // Método helper para calcular costo de este insumo
    public Double calcularCosto() {
        if (insumo != null && insumo.getPrecioUnitario() != null && cantidadUsada != null) {
            return insumo.getPrecioUnitario() * cantidadUsada;
        }
        return 0.0;
    }
}

