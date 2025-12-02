package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.DetalleVentaEntity;
import java.util.List;

/**
 * DAO para la tabla detalle_ventas
 */
@Dao
public interface DetalleVentaDao {
    
    @Query("SELECT * FROM detalle_ventas WHERE id = :id")
    DetalleVentaEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM detalle_ventas WHERE venta_id = :ventaId")
    List<DetalleVentaEntity> obtenerPorVenta(int ventaId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(DetalleVentaEntity detalle);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTodos(List<DetalleVentaEntity> detalles);
    
    @Update
    void actualizar(DetalleVentaEntity detalle);
    
    @Delete
    void eliminar(DetalleVentaEntity detalle);
    
    @Query("DELETE FROM detalle_ventas WHERE venta_id = :ventaId")
    void eliminarPorVenta(int ventaId);
}

