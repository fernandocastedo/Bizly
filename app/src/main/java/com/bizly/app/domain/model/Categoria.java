package com.bizly.app.domain.model;

import java.util.Date;

/**
 * Modelo de dominio para Categoria
 * Corresponde a la tabla categorias en la BD
 */
public class Categoria {
    private int id;
    private int empresaId;
    private String nombre;
    private String descripcion;
    private Date createdAt;

    public Categoria() {
    }

    public Categoria(int id, int empresaId, String nombre, String descripcion, Date createdAt) {
        this.id = id;
        this.empresaId = empresaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(int empresaId) {
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

