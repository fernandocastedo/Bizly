package com.bizly.app.domain.model;

import java.util.Date;

/**
 * Modelo de dominio para Insumo
 * Corresponde a la tabla insumos en la BD
 * RF-08, RF-11, RF-12, RF-13, RF-14
 */
public class Insumo {
    private int id;
    private int empresaId;
    private int sucursalId;          // Stock por sucursal
    private Integer categoriaId;     // Opcional
    private String nombre;
    private String descripcion;
    private double cantidad;        // Stock actual (RF-08, RF-11, RF-12)
    private String unidadMedida;
    private double precioUnitario;
    private double precioTotal;      // RF-08
    private double stockMinimo;      // Para alertas (RF-14)
    private boolean activo;          // Eliminación lógica (RF-13)
    private Date createdAt;
    private Date updatedAt;

    public Insumo() {
    }

    public Insumo(int id, int empresaId, int sucursalId, Integer categoriaId,
                  String nombre, String descripcion, double cantidad, String unidadMedida,
                  double precioUnitario, double precioTotal, double stockMinimo,
                  boolean activo, Date createdAt, Date updatedAt) {
        this.id = id;
        this.empresaId = empresaId;
        this.sucursalId = sucursalId;
        this.categoriaId = categoriaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.unidadMedida = unidadMedida;
        this.precioUnitario = precioUnitario;
        this.precioTotal = precioTotal;
        this.stockMinimo = stockMinimo;
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

    public int getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(int sucursalId) {
        this.sucursalId = sucursalId;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
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

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public double getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(double stockMinimo) {
        this.stockMinimo = stockMinimo;
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

    /**
     * Verifica si el stock está por debajo del mínimo (RF-14)
     */
    public boolean isStockBajo() {
        return cantidad <= stockMinimo;
    }
}

