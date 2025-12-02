package com.bizly.app.domain.model;

import java.util.Date;

/**
 * Modelo de dominio para InsumoProductoVenta
 * Corresponde a la tabla insumo_producto_venta en la BD
 * RF-16, RF-21 - Relación entre insumos y productos de venta
 */
public class InsumoProductoVenta {
    private int id;
    private int productoVentaId;
    private int insumoId;
    private double cantidadUtilizada;  // Para descuento automático (RF-16, RF-21)
    private Date createdAt;

    public InsumoProductoVenta() {
    }

    public InsumoProductoVenta(int id, int productoVentaId, int insumoId,
                                double cantidadUtilizada, Date createdAt) {
        this.id = id;
        this.productoVentaId = productoVentaId;
        this.insumoId = insumoId;
        this.cantidadUtilizada = cantidadUtilizada;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductoVentaId() {
        return productoVentaId;
    }

    public void setProductoVentaId(int productoVentaId) {
        this.productoVentaId = productoVentaId;
    }

    public int getInsumoId() {
        return insumoId;
    }

    public void setInsumoId(int insumoId) {
        this.insumoId = insumoId;
    }

    public double getCantidadUtilizada() {
        return cantidadUtilizada;
    }

    public void setCantidadUtilizada(double cantidadUtilizada) {
        this.cantidadUtilizada = cantidadUtilizada;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

