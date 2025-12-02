package com.bizly.app.domain.model;

import java.util.Date;

/**
 * Modelo de dominio para Trabajador
 * Corresponde a la tabla trabajadores en la BD
 * RF-46, RF-47, RF-48, RF-50, RF-51
 */
public class Trabajador {
    private int id;
    private int empresaId;
    private Integer sucursalId;      // Opcional
    private String nombre;
    private String cargo;
    private double sueldoMensual;   // RF-46
    private String tipoGasto;       // fijo / variable (RF-46, RF-34)
    private Date createdAt;
    private Date updatedAt;

    public Trabajador() {
    }

    public Trabajador(int id, int empresaId, Integer sucursalId, String nombre,
                     String cargo, double sueldoMensual, String tipoGasto,
                     Date createdAt, Date updatedAt) {
        this.id = id;
        this.empresaId = empresaId;
        this.sucursalId = sucursalId;
        this.nombre = nombre;
        this.cargo = cargo;
        this.sueldoMensual = sueldoMensual;
        this.tipoGasto = tipoGasto;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSueldoMensual() {
        return sueldoMensual;
    }

    public void setSueldoMensual(double sueldoMensual) {
        this.sueldoMensual = sueldoMensual;
    }

    public String getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(String tipoGasto) {
        this.tipoGasto = tipoGasto;
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

