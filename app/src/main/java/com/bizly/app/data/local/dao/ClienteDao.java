package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.ClienteEntity;
import java.util.List;

/**
 * DAO para la tabla clientes
 * RF-26, RF-29, RF-40
 */
@Dao
public interface ClienteDao {
    
    @Query("SELECT * FROM clientes WHERE id = :id")
    ClienteEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM clientes WHERE empresa_id = :empresaId")
    List<ClienteEntity> obtenerPorEmpresa(int empresaId);
    
    @Query("SELECT * FROM clientes WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId")
    List<ClienteEntity> obtenerPorEmpresaYSucursal(int empresaId, int sucursalId);
    
    @Query("SELECT * FROM clientes WHERE empresa_id = :empresaId " +
           "AND nombre LIKE '%' || :nombre || '%'")
    List<ClienteEntity> buscarPorNombre(int empresaId, String nombre);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(ClienteEntity cliente);
    
    @Update
    void actualizar(ClienteEntity cliente);
    
    @Delete
    void eliminar(ClienteEntity cliente);
}

