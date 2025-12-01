package com.bizly.app.data.repository;

import com.bizly.app.domain.model.Venta;
import java.util.List;
import java.util.Date;

/**
 * Interfaz del repositorio de ventas
 * Define las operaciones de acceso a datos para ventas
 * RF-22, RF-24, RF-25, RF-26, RF-27, RF-28, RF-29, RF-30, RF-31
 */
public interface VentaRepository {
    
    /**
     * Registra una nueva venta (RF-22, RF-25)
     */
    Venta registrarVenta(Venta venta);
    
    /**
     * Obtiene todas las ventas (RF-26)
     */
    List<Venta> obtenerVentas(int empresaId, int sucursalId);
    
    /**
     * Obtiene ventas con filtros (RF-26)
     */
    List<Venta> obtenerVentasFiltradas(int empresaId, int sucursalId, Date fechaInicio, Date fechaFin, Integer usuarioId);
    
    /**
     * Obtiene top vendedores (RF-27)
     */
    List<Object[]> obtenerTopVendedores(int empresaId, int sucursalId, Date fechaInicio, Date fechaFin);
    
    /**
     * Cancela o corrige una venta (RF-28)
     */
    boolean cancelarVenta(int ventaId);
    
    /**
     * Obtiene pedidos pendientes (RF-30)
     */
    List<Venta> obtenerPedidosPendientes(int empresaId, int sucursalId);
    
    /**
     * Actualiza el estado de un pedido (RF-31)
     */
    boolean actualizarEstadoPedido(int ventaId, String estadoPedido, String estadoPago);
    
    /**
     * Obtiene una venta por ID
     */
    Venta obtenerVentaPorId(int id);
    
    /**
     * Calcula el total de ventas en un período (RF-24)
     */
    double calcularTotalVentas(int empresaId, int sucursalId, Date fechaInicio, Date fechaFin);
}

