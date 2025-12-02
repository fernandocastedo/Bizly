package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.EmpresaEntity;
import java.util.List;

/**
 * DAO para la tabla empresas
 */
@Dao
public interface EmpresaDao {
    
    @Query("SELECT * FROM empresas WHERE id = :id")
    EmpresaEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM empresas")
    List<EmpresaEntity> obtenerTodas();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(EmpresaEntity empresa);
    
    @Update
    void actualizar(EmpresaEntity empresa);
    
    @Delete
    void eliminar(EmpresaEntity empresa);
    
    @Query("DELETE FROM empresas WHERE id = :id")
    void eliminarPorId(int id);
}

