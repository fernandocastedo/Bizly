package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.InsumoEntity;
import java.util.List;

/**
 * DAO para la tabla insumos
 * RF-08, RF-11, RF-12, RF-13, RF-14
 */
@Dao
public interface InsumoDao {
    
    @Query("SELECT * FROM insumos WHERE id = :id")
    InsumoEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM insumos WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId AND activo = 1")
    List<InsumoEntity> obtenerTodos(int empresaId, int sucursalId);
    
    @Query("SELECT * FROM insumos WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "AND nombre LIKE '%' || :nombre || '%' AND activo = 1")
    List<InsumoEntity> buscarPorNombre(int empresaId, int sucursalId, String nombre);
    
    @Query("SELECT * FROM insumos WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "AND categoria_id = :categoriaId AND activo = 1")
    List<InsumoEntity> obtenerPorCategoria(int empresaId, int sucursalId, int categoriaId);
    
    @Query("SELECT * FROM insumos WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "AND cantidad <= stock_minimo AND activo = 1")
    List<InsumoEntity> obtenerStockBajo(int empresaId, int sucursalId);
    
    @Query("SELECT * FROM insumos WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "AND nombre = :nombre AND activo = 1 LIMIT 1")
    InsumoEntity buscarPorNombreExacto(int empresaId, int sucursalId, String nombre);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(InsumoEntity insumo);
    
    @Update
    void actualizar(InsumoEntity insumo);
    
    @Query("UPDATE insumos SET cantidad = :nuevaCantidad, updated_at = :updatedAt WHERE id = :id")
    void actualizarCantidad(int id, double nuevaCantidad, java.util.Date updatedAt);
    
    @Query("UPDATE insumos SET activo = 0 WHERE id = :id")
    void desactivar(int id);
    
    @Delete
    void eliminar(InsumoEntity insumo);
}

