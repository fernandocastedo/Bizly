package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.TrabajadorEntity;
import java.util.List;

/**
 * DAO para la tabla trabajadores
 * RF-46, RF-47
 */
@Dao
public interface TrabajadorDao {
    
    @Query("SELECT * FROM trabajadores WHERE id = :id")
    TrabajadorEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM trabajadores WHERE empresa_id = :empresaId")
    List<TrabajadorEntity> obtenerPorEmpresa(int empresaId);
    
    @Query("SELECT * FROM trabajadores WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId")
    List<TrabajadorEntity> obtenerPorEmpresaYSucursal(int empresaId, int sucursalId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(TrabajadorEntity trabajador);
    
    @Update
    void actualizar(TrabajadorEntity trabajador);
    
    @Delete
    void eliminar(TrabajadorEntity trabajador);
    
    @Query("DELETE FROM trabajadores WHERE id = :id")
    void eliminarPorId(int id);
}

