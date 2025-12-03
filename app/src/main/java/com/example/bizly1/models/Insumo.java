package com.example.bizly1.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Insumo implements Serializable {
    
    @SerializedName("id")
    private Integer id;
    
    @SerializedName("empresa_id")
    private Integer empresaId;
    
    @SerializedName("nombre")
    private String nombre;
    
    @SerializedName("descripcion")
    private String descripcion;
    
    @SerializedName("cantidad")
    private Double cantidad;
    
    @SerializedName("unidad_medida")
    private String unidadMedida;
    
    @SerializedName("precio_unitario")
    private Double precioUnitario;
    
    @SerializedName("precio_total")
    private Double precioTotal;
    
    @SerializedName("categoria")
    private String categoria;
    
    @SerializedName("stock_minimo")
    private Double stockMinimo;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    // Campos locales para sincronización
    private String syncStatus; // "synced", "pending", "error"
    private Integer serverId; // ID del servidor cuando se sincroniza
    
    public Insumo() {
        this.syncStatus = "pending";
    }
    
    // Constructor para creación local
    public Insumo(String nombre, String descripcion, Double cantidad, String unidadMedida, 
                  Double precioUnitario, Double precioTotal, String categoria, Double stockMinimo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
        this.categoria = categoria;
        this.stockMinimo = stockMinimo;
        this.syncStatus = "pending";
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getEmpresaId() {
        return empresaId;
    }
    
    public void setEmpresaId(Integer empresaId) {
        this.empresaId = empresaId;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Double getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }
    
    public String getUnidadMedida() {
        return unidadMedida;
    }
    
    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
    
    public Double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    
    public Double getPrecioTotal() {
        return precioTotal;
    }
    
    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public Double getStockMinimo() {
        return stockMinimo;
    }
    
    public void setStockMinimo(Double stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getSyncStatus() {
        return syncStatus;
    }
    
    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }
    
    public Integer getServerId() {
        return serverId;
    }
    
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }
    
    // Método helper para calcular precio total
    public void calcularPrecioTotal() {
        if (cantidad != null && precioUnitario != null && cantidad > 0) {
            this.precioTotal = cantidad * precioUnitario;
        }
    }
    
    // Método helper para verificar si está bajo stock mínimo
    public boolean isStockBajo() {
        if (stockMinimo == null || cantidad == null) {
            return false;
        }
        return cantidad < stockMinimo;
    }
}

