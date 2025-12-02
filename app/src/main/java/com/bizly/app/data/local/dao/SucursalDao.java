package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.SucursalEntity;
import java.util.List;

/**
 * DAO para la tabla sucursales
 */
@Dao
public interface SucursalDao {
    
    @Query("SELECT * FROM sucursales WHERE id = :id")
    SucursalEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM sucursales WHERE empresa_id = :empresaId")
    List<SucursalEntity> obtenerPorEmpresa(int empresaId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(SucursalEntity sucursal);
    
    @Update
    void actualizar(SucursalEntity sucursal);
    
    @Delete
    void eliminar(SucursalEntity sucursal);
    
    @Query("DELETE FROM sucursales WHERE id = :id")
    void eliminarPorId(int id);
}

