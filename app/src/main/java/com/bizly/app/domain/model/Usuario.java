package com.bizly.app.domain.model;

import java.util.Date;

/**
 * Modelo de dominio para Usuario
 * Corresponde a la tabla usuarios en la BD
 * RF-01, RF-02, RF-49
 */
public class Usuario {
    private int id;
    private int empresaId;
    private Integer sucursalId;      // Opcional
    private Integer trabajadorId;    // Opcional - usuario vinculado (RF-48)
    private String nombre;
    private String email;
    private String password;
    private String tipoUsuario;      // EMPRENDEDOR / TRABAJADOR (RF-01, RF-49)
    private boolean activo;          // Para desactivar acceso (RF-52)
    private Date createdAt;
    private Date updatedAt;

    public Usuario() {
    }

    public Usuario(int id, int empresaId, Integer sucursalId, Integer trabajadorId,
                   String nombre, String email, String password, String tipoUsuario,
                   boolean activo, Date createdAt, Date updatedAt) {
        this.id = id;
        this.empresaId = empresaId;
        this.sucursalId = sucursalId;
        this.trabajadorId = trabajadorId;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
        this.activo = activo;
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

    public Integer getTrabajadorId() {
        return trabajadorId;
    }

    public void setTrabajadorId(Integer trabajadorId) {
        this.trabajadorId = trabajadorId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
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

