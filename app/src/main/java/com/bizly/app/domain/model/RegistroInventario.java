package com.bizly.app.domain.model;

import java.util.Date;

/**
 * Modelo de dominio para RegistroInventario
 * Corresponde a la tabla registros_inventario en la BD
 * RF-12 - Historial de movimientos de inventario
 */
public class RegistroInventario {
    private int id;
    private int empresaId;
    private int sucursalId;
    private int usuarioId;
    private int insumoId;
    private String tipoMovimiento;   // entrada / salida / ajuste (RF-12)
    private double cantidadAnterior;
    private double cantidadNueva;
    private String motivo;
    private Date createdAt;

    public RegistroInventario() {
    }

    public RegistroInventario(int id, int empresaId, int sucursalId, int usuarioId,
                             int insumoId, String tipoMovimiento, double cantidadAnterior,
                             double cantidadNueva, String motivo, Date createdAt) {
        this.id = id;
        this.empresaId = empresaId;
        this.sucursalId = sucursalId;
        this.usuarioId = usuarioId;
        this.insumoId = insumoId;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidadAnterior = cantidadAnterior;
        this.cantidadNueva = cantidadNueva;
        this.motivo = motivo;
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

    public int getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(int sucursalId) {
        this.sucursalId = sucursalId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getInsumoId() {
        return insumoId;
    }

    public void setInsumoId(int insumoId) {
        this.insumoId = insumoId;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public double getCantidadAnterior() {
        return cantidadAnterior;
    }

    public void setCantidadAnterior(double cantidadAnterior) {
        this.cantidadAnterior = cantidadAnterior;
    }

    public double getCantidadNueva() {
        return cantidadNueva;
    }

    public void setCantidadNueva(double cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

