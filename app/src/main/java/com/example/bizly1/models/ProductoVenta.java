package com.example.bizly1.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductoVenta implements Serializable {
    
    @SerializedName("id")
    private Integer id;
    
    @SerializedName("empresa_id")
    private Integer empresaId;
    
    @SerializedName("nombre")
    private String nombre;
    
    @SerializedName("descripcion")
    private String descripcion;
    
    @SerializedName("precio_venta")
    private Double precioVenta;
    
    @SerializedName("costo_total")
    private Double costoTotal; // Costo total de los insumos
    
    @SerializedName("margen_ganancia")
    private Double margenGanancia; // Porcentaje aplicado
    
    @SerializedName("activo")
    private Boolean activo;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    // Campos locales para sincronización
    private String syncStatus; // "synced", "pending", "error"
    private Integer serverId; // ID del servidor cuando se sincroniza
    
    // Lista de insumos asociados (para uso local, no se serializa)
    private List<ProductoVentaInsumo> insumos;
    
    public ProductoVenta() {
        this.syncStatus = "pending";
        this.activo = true;
    }
    
    // Constructor para creación local
    public ProductoVenta(String nombre, String descripcion, Double precioVenta, Double costoTotal, Double margenGanancia) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.costoTotal = costoTotal;
        this.margenGanancia = margenGanancia;
        this.activo = true;
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
    
    public Double getPrecioVenta() {
        return precioVenta;
    }
    
    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }
    
    public Double getCostoTotal() {
        return costoTotal;
    }
    
    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }
    
    public Double getMargenGanancia() {
        return margenGanancia;
    }
    
    public void setMargenGanancia(Double margenGanancia) {
        this.margenGanancia = margenGanancia;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
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
    
    public List<ProductoVentaInsumo> getInsumos() {
        return insumos;
    }
    
    public void setInsumos(List<ProductoVentaInsumo> insumos) {
        this.insumos = insumos;
    }
    
    // Método helper para calcular precio sugerido
    public void calcularPrecioSugerido(Double margenGananciaEmpresa) {
        if (costoTotal != null && costoTotal > 0 && margenGananciaEmpresa != null) {
            this.precioVenta = costoTotal * (1 + (margenGananciaEmpresa / 100));
            this.margenGanancia = margenGananciaEmpresa;
        }
    }
    
    // Método helper para verificar si tiene stock suficiente
    public boolean tieneStockSuficiente() {
        if (insumos == null || insumos.isEmpty()) {
            return false;
        }
        for (ProductoVentaInsumo pvi : insumos) {
            if (pvi.getInsumo() != null) {
                if (pvi.getInsumo().getCantidad() == null || 
                    pvi.getInsumo().getCantidad() < pvi.getCantidadUsada()) {
                    return false;
                }
            }
        }
        return true;
    }
}

