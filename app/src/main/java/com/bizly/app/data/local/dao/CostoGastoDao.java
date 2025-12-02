package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.CostoGastoEntity;
import java.util.Date;
import java.util.List;

/**
 * DAO para la tabla costos_gastos
 * RF-32, RF-33, RF-34, RF-36, RF-37
 */
@Dao
public interface CostoGastoDao {
    
    @Query("SELECT * FROM costos_gastos WHERE id = :id")
    CostoGastoEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM costos_gastos WHERE empresa_id = :empresaId " +
           "ORDER BY fecha DESC")
    List<CostoGastoEntity> obtenerTodos(int empresaId);
    
    @Query("SELECT * FROM costos_gastos WHERE empresa_id = :empresaId " +
           "AND sucursal_id = :sucursalId ORDER BY fecha DESC")
    List<CostoGastoEntity> obtenerPorEmpresaYSucursal(int empresaId, Integer sucursalId);
    
    @Query("SELECT * FROM costos_gastos WHERE empresa_id = :empresaId " +
           "AND fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY fecha DESC")
    List<CostoGastoEntity> obtenerPorRangoFechas(int empresaId, Date fechaInicio, Date fechaFin);
    
    @Query("SELECT * FROM costos_gastos WHERE empresa_id = :empresaId " +
           "AND categoria_financiera = :categoriaFinanciera ORDER BY fecha DESC")
    List<CostoGastoEntity> obtenerPorCategoriaFinanciera(int empresaId, String categoriaFinanciera);
    
    @Query("SELECT * FROM costos_gastos WHERE empresa_id = :empresaId " +
           "AND clasificacion = :clasificacion ORDER BY fecha DESC")
    List<CostoGastoEntity> obtenerPorClasificacion(int empresaId, String clasificacion);
    
    @Query("SELECT SUM(monto) FROM costos_gastos WHERE empresa_id = :empresaId " +
           "AND fecha BETWEEN :fechaInicio AND :fechaFin")
    Double calcularTotal(int empresaId, Date fechaInicio, Date fechaFin);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(CostoGastoEntity costoGasto);
    
    @Update
    void actualizar(CostoGastoEntity costoGasto);
    
    @Delete
    void eliminar(CostoGastoEntity costoGasto);
    
    @Query("DELETE FROM costos_gastos WHERE id = :id")
    void eliminarPorId(int id);
}

