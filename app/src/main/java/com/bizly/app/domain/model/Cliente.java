package com.bizly.app.domain.model;

import java.util.Date;

/**
 * Modelo de dominio para Cliente
 * Corresponde a la tabla clientes en la BD
 * RF-26, RF-29, RF-40
 */
public class Cliente {
    private int id;
    private int empresaId;
    private Integer sucursalId;      // Opcional - dónde se registró principalmente
    private String nombre;
    private Integer nit;
    private String telefono;
    private String email;
    private String direccion;        // Útil para envíos (RF-29)
    private Date createdAt;

    public Cliente() {
    }

    public Cliente(int id, int empresaId, Integer sucursalId, String nombre,
                  Integer nit, String telefono, String email, String direccion, Date createdAt) {
        this.id = id;
        this.empresaId = empresaId;
        this.sucursalId = sucursalId;
        this.nombre = nombre;
        this.nit = nit;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
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

    public Integer getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNit() {
        return nit;
    }

    public void setNit(Integer nit) {
        this.nit = nit;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

