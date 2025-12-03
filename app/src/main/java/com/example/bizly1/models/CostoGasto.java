package com.example.bizly1.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CostoGasto implements Serializable {
    
    @SerializedName("id")
    private Integer id;
    
    @SerializedName("empresa_id")
    private Integer empresaId;
    
    @SerializedName("sucursal_id")
    private Integer sucursalId;
    
    @SerializedName("tipo")
    private String tipo; // "costo", "gasto", "sueldo", "adelanto"
    
    @SerializedName("categoria")
    private String categoria; // Categoría específica según el tipo
    
    @SerializedName("descripcion")
    private String descripcion;
    
    @SerializedName("monto")
    private Double monto;
    
    @SerializedName("fecha")
    private String fecha;
    
    @SerializedName("trabajador_id")
    private Integer trabajadorId; // Para sueldos y adelantos
    
    @SerializedName("metodo_pago")
    private String metodoPago; // efectivo, transferencia, etc.
    
    @SerializedName("comprobante")
    private String comprobante; // Número de factura, recibo, etc.
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    // Campos locales para sincronización
    private String syncStatus; // "synced", "pending", "error"
    private Integer serverId; // ID del servidor cuando se sincroniza
    
    public CostoGasto() {
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
    
    public Integer getSucursalId() {
        return sucursalId;
    }
    
    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Double getMonto() {
        return monto;
    }
    
    public void setMonto(Double monto) {
        this.monto = monto;
    }
    
    public String getFecha() {
        return fecha;
    }
    
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    public Integer getTrabajadorId() {
        return trabajadorId;
    }
    
    public void setTrabajadorId(Integer trabajadorId) {
        this.trabajadorId = trabajadorId;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public String getComprobante() {
        return comprobante;
    }
    
    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
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
}

