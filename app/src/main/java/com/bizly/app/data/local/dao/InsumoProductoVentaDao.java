package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.InsumoProductoVentaEntity;
import java.util.List;

/**
 * DAO para la tabla insumo_producto_venta
 * RF-16, RF-21
 */
@Dao
public interface InsumoProductoVentaDao {
    
    @Query("SELECT * FROM insumo_producto_venta WHERE id = :id")
    InsumoProductoVentaEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM insumo_producto_venta WHERE producto_venta_id = :productoVentaId")
    List<InsumoProductoVentaEntity> obtenerPorProducto(int productoVentaId);
    
    @Query("SELECT * FROM insumo_producto_venta WHERE insumo_id = :insumoId")
    List<InsumoProductoVentaEntity> obtenerPorInsumo(int insumoId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(InsumoProductoVentaEntity relacion);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarTodos(List<InsumoProductoVentaEntity> relaciones);
    
    @Update
    void actualizar(InsumoProductoVentaEntity relacion);
    
    @Delete
    void eliminar(InsumoProductoVentaEntity relacion);
    
    @Query("DELETE FROM insumo_producto_venta WHERE producto_venta_id = :productoVentaId")
    void eliminarPorProducto(int productoVentaId);
}

