package com.example.bizly1.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sucursal implements Serializable {
    
    @SerializedName("id")
    private Integer id;
    
    @SerializedName("empresa_id")
    private Integer empresaId;
    
    @SerializedName("nombre")
    private String nombre;
    
    @SerializedName("direccion")
    private String direccion;
    
    @SerializedName("ciudad")
    private String ciudad;
    
    @SerializedName("latitud")
    private Double latitud;
    
    @SerializedName("longitud")
    private Double longitud;
    
    @SerializedName("departamento")
    private String departamento;
    
    @SerializedName("telefono")
    private String telefono;
    
    @SerializedName("created_at")
    private String createdAt;
    
    @SerializedName("updated_at")
    private String updatedAt;
    
    // Campos locales para sincronización
    private String syncStatus; // "synced", "pending", "error"
    private Integer serverId; // ID del servidor cuando se sincroniza
    
    public Sucursal() {
        this.syncStatus = "pending";
    }
    
    // Constructor para creación local
    public Sucursal(String nombre, String direccion, String ciudad, Double latitud, Double longitud, String departamento, String telefono) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.latitud = latitud;
        this.longitud = longitud;
        this.departamento = departamento;
        this.telefono = telefono;
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
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getCiudad() {
        return ciudad;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    public Double getLatitud() {
        return latitud;
    }
    
    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }
    
    public Double getLongitud() {
        return longitud;
    }
    
    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
    
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

