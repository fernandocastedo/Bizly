package com.example.bizly1.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Venta implements Serializable {
    
    @SerializedName("id")
    private Integer id;
    
    @SerializedName("empresa_id")
    private Integer empresaId;
    
    @SerializedName("sucursal_id")
    private Integer sucursalId;
    
    @SerializedName("usuario_id")
    private Integer usuarioId; // vendedor
    
    @SerializedName("cliente_id")
    private Integer clienteId;
    
    @SerializedName("fecha")
    private String fecha;
    
    @SerializedName("metodo_pago")
    private String metodoPago; // efectivo, QR, transferencia, etc.
    
    @SerializedName("total")
    private Double total;
    
    @SerializedName("es_envio")
    private Boolean esEnvio;
    
    @SerializedName("estado_pago")
    private String estadoPago; // "pagado", "pendiente"
    
    @SerializedName("estado_pedido")
    private String estadoPedido; // "pendiente", "completado", "cancelado"
    
    @SerializedName("created_at")
    private String createdAt;
    
    // Campos locales para sincronización
    private String syncStatus; // "synced", "pending", "error"
    private Integer serverId; // ID del servidor cuando se sincroniza
    
    // Lista de productos vendidos (para uso local, no se serializa)
    private List<VentaProducto> productos;
    
    // Cliente asociado (para uso local)
    private Cliente cliente;
    
    public Venta() {
        this.syncStatus = "pending";
        this.esEnvio = false;
        this.estadoPago = "pagado";
        this.estadoPedido = "completado";
    }
    
    // Getters y Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getEmpresaId() {
        return empresaId;
    }
    
    public void setEmpresaId(Integer empresaId) {
        this.empresaId = empresaId;
    }
    
    public Integer getSucursalId() {
        return sucursalId;
    }
    
    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }
    
    public Integer getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Integer getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getFecha() {
        return fecha;
    }
    
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    public String getMetodoPago() {
        return metodoPago;
    }
    
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    public Double getTotal() {
        return total;
    }
    
    public void setTotal(Double total) {
        this.total = total;
    }
    
    public Boolean getEsEnvio() {
        return esEnvio;
    }
    
    public void setEsEnvio(Boolean esEnvio) {
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
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getSyncStatus() {
        return syncStatus;
    }
    
    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }
    
    public Integer getServerId() {
        return serverId;
    }
    
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }
    
    public List<VentaProducto> getProductos() {
        return productos;
    }
    
    public void setProductos(List<VentaProducto> productos) {
        this.productos = productos;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    // Método helper para verificar si está pendiente
    public boolean isPendiente() {
        return "pendiente".equals(estadoPedido) || "pendiente".equals(estadoPago);
    }
}

