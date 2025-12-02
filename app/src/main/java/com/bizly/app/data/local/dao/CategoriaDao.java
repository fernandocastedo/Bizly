package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.CategoriaEntity;
import java.util.List;

/**
 * DAO para la tabla categorias
 */
@Dao
public interface CategoriaDao {
    
    @Query("SELECT * FROM categorias WHERE id = :id")
    CategoriaEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM categorias WHERE empresa_id = :empresaId")
    List<CategoriaEntity> obtenerPorEmpresa(int empresaId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(CategoriaEntity categoria);
    
    @Update
    void actualizar(CategoriaEntity categoria);
    
    @Delete
    void eliminar(CategoriaEntity categoria);
}

