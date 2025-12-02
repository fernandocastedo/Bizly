package com.example.bizly1.models;

import java.io.Serializable;

public class VentaProducto implements Serializable {
    
    private Integer id;
    private Integer ventaId;
    private Integer productoVentaId;
    private Double cantidad;
    private Double precioUnitario;
    private Double subtotal;
    
    // Para uso local (no se serializa a JSON)
    private ProductoVenta productoVenta;
    
    public VentaProducto() {
    }
    
    public VentaProducto(Integer ventaId, Integer productoVentaId, Double cantidad, Double precioUnitario) {
        this.ventaId = ventaId;
        this.productoVentaId = productoVentaId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getVentaId() {
        return ventaId;
    }
    
    public void setVentaId(Integer ventaId) {
        this.ventaId = ventaId;
    }
    
    public Integer getProductoVentaId() {
        return productoVentaId;
    }
    
    public void setProductoVentaId(Integer productoVentaId) {
        this.productoVentaId = productoVentaId;
    }
    
    public Double getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }
    
    public Double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }
    
    public Double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
    
    public ProductoVenta getProductoVenta() {
        return productoVenta;
    }
    
    public void setProductoVenta(ProductoVenta productoVenta) {
        this.productoVenta = productoVenta;
    }
    
    private void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            this.subtotal = cantidad * precioUnitario;
        }
    }
}

