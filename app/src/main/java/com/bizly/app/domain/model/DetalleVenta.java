package com.bizly.app.domain.model;

/**
 * Modelo de dominio para DetalleVenta
 * Corresponde a la tabla detalle_ventas en la BD
 */
public class DetalleVenta {
    private int id;
    private int ventaId;
    private int productoVentaId;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public DetalleVenta() {
    }

    public DetalleVenta(int id, int ventaId, int productoVentaId, int cantidad,
                       double precioUnitario, double subtotal) {
        this.id = id;
        this.ventaId = ventaId;
        this.productoVentaId = productoVentaId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVentaId() {
        return ventaId;
    }

    public void setVentaId(int ventaId) {
        this.ventaId = ventaId;
    }

    public int getProductoVentaId() {
        return productoVentaId;
    }

    public void setProductoVentaId(int productoVentaId) {
        this.productoVentaId = productoVentaId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}

