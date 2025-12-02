package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.RegistroInventarioEntity;
import java.util.List;

/**
 * DAO para la tabla registros_inventario
 * RF-12 - Historial de movimientos
 */
@Dao
public interface RegistroInventarioDao {
    
    @Query("SELECT * FROM registros_inventario WHERE id = :id")
    RegistroInventarioEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM registros_inventario WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "ORDER BY created_at DESC")
    List<RegistroInventarioEntity> obtenerTodos(int empresaId, int sucursalId);
    
    @Query("SELECT * FROM registros_inventario WHERE insumo_id = :insumoId " +
           "ORDER BY created_at DESC")
    List<RegistroInventarioEntity> obtenerPorInsumo(int insumoId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(RegistroInventarioEntity registro);
    
    @Query("DELETE FROM registros_inventario WHERE id = :id")
    void eliminar(int id);
}

