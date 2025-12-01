package com.bizly.app.domain.model;

import java.util.Date;

/**
 * Modelo de dominio para Empresa/Emprendimiento
 * Corresponde a la tabla empresas en la BD
 */
public class Empresa {
    private int id;
    private String nombre;
    private String rubro;
    private String descripcion;
    private double margenGanancia;  // RF-06
    private String logoUrl;         // RF-04
    private Date createdAt;
    private Date updatedAt;

    public Empresa() {
    }

    public Empresa(int id, String nombre, String rubro, String descripcion, 
                   double margenGanancia, String logoUrl, Date createdAt, Date updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.rubro = rubro;
        this.descripcion = descripcion;
        this.margenGanancia = margenGanancia;
        this.logoUrl = logoUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMargenGanancia() {
        return margenGanancia;
    }

    public void setMargenGanancia(double margenGanancia) {
        this.margenGanancia = margenGanancia;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

