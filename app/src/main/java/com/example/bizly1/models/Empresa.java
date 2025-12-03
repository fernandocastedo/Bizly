package com.example.bizly1.models;

import com.google.gson.annotations.SerializedName;

public class Empresa {
    @SerializedName("id")
    private Integer id;
    
    @SerializedName("nombre")
    private String nombre;
    
    @SerializedName("rubro")
    private String rubro;
    
    @SerializedName("margen_ganancia")
    private Double margenGanancia;
    
    @SerializedName("descripcion")
    private String descripcion;
    
    @SerializedName("logo_url")
    private String logoUrl;
    
    @SerializedName("usuario_id")
    private Integer usuarioId;
    
    public Empresa() {
    }
    
    public Empresa(String nombre, String rubro, Double margenGanancia, String descripcion) {
        this.nombre = nombre;
        this.rubro = rubro;
        this.margenGanancia = margenGanancia;
        this.descripcion = descripcion;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getRubro() {
        return rubro;
    }
    
    public void setRubro(String rubro) {
        this.rubro = rubro;
    }
    
    public Double getMargenGanancia() {
        return margenGanancia;
    }
    
    public void setMargenGanancia(Double margenGanancia) {
        this.margenGanancia = margenGanancia;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getLogoUrl() {
        return logoUrl;
    }
    
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    
    public Integer getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}

