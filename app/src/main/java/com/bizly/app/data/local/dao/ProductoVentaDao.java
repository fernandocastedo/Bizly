package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.ProductoVentaEntity;
import java.util.List;

/**
 * DAO para la tabla productos_venta
 * RF-15, RF-19, RF-20
 */
@Dao
public interface ProductoVentaDao {
    
    @Query("SELECT * FROM productos_venta WHERE id = :id")
    ProductoVentaEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM productos_venta WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId AND activo = 1")
    List<ProductoVentaEntity> obtenerTodos(int empresaId, int sucursalId);
    
    @Query("SELECT * FROM productos_venta WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "AND nombre LIKE '%' || :nombre || '%' AND activo = 1")
    List<ProductoVentaEntity> buscarPorNombre(int empresaId, int sucursalId, String nombre);
    
    @Query("SELECT * FROM productos_venta WHERE empresa_id = :empresaId AND sucursal_id = :sucursalId " +
           "AND categoria_id = :categoriaId AND activo = 1")
    List<ProductoVentaEntity> obtenerPorCategoria(int empresaId, int sucursalId, int categoriaId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(ProductoVentaEntity producto);
    
    @Update
    void actualizar(ProductoVentaEntity producto);
    
    @Query("UPDATE productos_venta SET activo = 0 WHERE id = :id")
    void desactivar(int id);
    
    @Delete
    void eliminar(ProductoVentaEntity producto);
}

