package com.bizly.app.domain.model;

import java.util.Date;

/**
 * Modelo de dominio para Venta
 * Corresponde a la tabla ventas en la BD
 * RF-22, RF-24, RF-25, RF-26, RF-29, RF-30, RF-31
 */
public class Venta {
    private int id;
    private int empresaId;
    private int sucursalId;
    private int usuarioId;          // Vendedor (RF-25, RF-50)
    private Integer clienteId;       // Opcional - top clientes, envíos (RF-26, RF-40)
    private Date fecha;
    private String metodoPago;       // Opcional (RF-22)
    private double total;            // RF-24
    private boolean esEnvio;         // RF-29
    private String estadoPago;        // pagado / pendiente (RF-29, RF-30)
    private String estadoPedido;     // pendiente / completado / cancelado (RF-30, RF-31)
    private Date createdAt;

    public Venta() {
    }

    public Venta(int id, int empresaId, int sucursalId, int usuarioId, Integer clienteId,
                Date fecha, String metodoPago, double total, boolean esEnvio,
                String estadoPago, String estadoPedido, Date createdAt) {
        this.id = id;
        this.empresaId = empresaId;
        this.sucursalId = sucursalId;
        this.usuarioId = usuarioId;
        this.clienteId = clienteId;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.total = total;
        this.esEnvio = esEnvio;
        this.estadoPago = estadoPago;
        this.estadoPedido = estadoPedido;
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

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isEnvio() {
        return esEnvio;
    }

    public void setEnvio(boolean esEnvio) {
        this.esEnvio = esEnvio;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

