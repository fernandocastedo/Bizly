package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.VentaEntity;
import java.util.Date;
import java.util.List;

/**
 * DAO para la tabla ventas
 * RF-22, RF-24, RF-25, RF-26, RF-27, RF-29, RF-30, RF-31
 */
@Dao
public interface VentaDao {
    
    @Query("SELECT * FROM ventas WHERE id = :id")
    VentaEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM ventas WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "ORDER BY fecha DESC")
    List<VentaEntity> obtenerTodas(int empresaId, int sucursalId);
    
    @Query("SELECT * FROM ventas WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "AND fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY fecha DESC")
    List<VentaEntity> obtenerPorRangoFechas(int empresaId, int sucursalId, Date fechaInicio, Date fechaFin);
    
    @Query("SELECT * FROM ventas WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "AND usuario_id = :usuarioId ORDER BY fecha DESC")
    List<VentaEntity> obtenerPorUsuario(int empresaId, int sucursalId, int usuarioId);
    
    @Query("SELECT * FROM ventas WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "AND (estado_pedido = 'pendiente' OR estado_pago = 'pendiente') " +
           "ORDER BY created_at DESC")
    List<VentaEntity> obtenerPedidosPendientes(int empresaId, int sucursalId);
    
    @Query("SELECT usuario_id as usuarioId, COUNT(*) as totalVentas, SUM(total) as totalMonto " +
           "FROM ventas WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "AND fecha BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY usuario_id ORDER BY totalMonto DESC")
    List<com.bizly.app.data.local.entity.TopVendedorResult> obtenerTopVendedores(int empresaId, int sucursalId, Date fechaInicio, Date fechaFin);
    
    @Query("SELECT SUM(total) FROM ventas WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "AND fecha BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotalVentas(int empresaId, int sucursalId, Date fechaInicio, Date fechaFin);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(VentaEntity venta);
    
    @Update
    void actualizar(VentaEntity venta);
    
    @Query("UPDATE ventas SET estado_pedido = :estadoPedido, estado_pago = :estadoPago WHERE id = :id")
    void actualizarEstado(int id, String estadoPedido, String estadoPago);
    
    @Delete
    void eliminar(VentaEntity venta);
}

