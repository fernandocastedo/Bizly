package com.bizly.app.domain.model;

import java.util.Date;

/**
 * Modelo de dominio para CostoGasto
 * Corresponde a la tabla costos_gastos en la BD
 * RF-32, RF-33, RF-34, RF-36, RF-37, RF-46, RF-47
 */
public class CostoGasto {
    private int id;
    private int empresaId;
    private Integer sucursalId;          // Opcional - algunos gastos son por sucursal, otros generales
    private int usuarioId;               // COSTO / GASTO (RF-32)
    private String categoriaFinanciera;  // DIRECTO / ADMINISTRATIVO (RF-32)
    private String descripcion;
    private double monto;
    private Date fecha;
    private String clasificacion;        // FIJO / VARIABLE (RF-34)
    private Integer insumoId;            // Opcional - costos de insumos (RF-33)
    private Integer trabajadorId;        // Opcional - sueldos como gasto (RF-46, RF-47)
    private Date createdAt;

    public CostoGasto() {
    }

    public CostoGasto(int id, int empresaId, Integer sucursalId, int usuarioId,
                     String categoriaFinanciera, String descripcion, double monto,
                     Date fecha, String clasificacion, Integer insumoId,
                     Integer trabajadorId, Date createdAt) {
        this.id = id;
        this.empresaId = empresaId;
        this.sucursalId = sucursalId;
        this.usuarioId = usuarioId;
        this.categoriaFinanciera = categoriaFinanciera;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
        this.clasificacion = clasificacion;
        this.insumoId = insumoId;
        this.trabajadorId = trabajadorId;
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

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getCategoriaFinanciera() {
        return categoriaFinanciera;
    }

    public void setCategoriaFinanciera(String categoriaFinanciera) {
        this.categoriaFinanciera = categoriaFinanciera;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public Integer getInsumoId() {
        return insumoId;
    }

    public void setInsumoId(Integer insumoId) {
        this.insumoId = insumoId;
    }

    public Integer getTrabajadorId() {
        return trabajadorId;
    }

    public void setTrabajadorId(Integer trabajadorId) {
        this.trabajadorId = trabajadorId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

