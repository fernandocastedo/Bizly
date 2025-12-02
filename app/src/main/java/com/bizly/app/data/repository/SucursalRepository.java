package com.bizly.app.data.repository;

import com.bizly.app.domain.model.Sucursal;
import java.util.List;

/**
 * Interfaz del repositorio de sucursales
 * Define las operaciones de acceso a datos para sucursales
 */
public interface SucursalRepository {
    
    /**
     * Registra una nueva sucursal
     */
    Sucursal registrarSucursal(Sucursal sucursal);
    
    /**
     * Obtiene una sucursal por ID
     */
    Sucursal obtenerSucursalPorId(int id);
    
    /**
     * Obtiene todas las sucursales de una empresa
     */
    List<Sucursal> obtenerSucursalesPorEmpresa(int empresaId);
    
    /**
     * Actualiza una sucursal
     */
    boolean actualizarSucursal(Sucursal sucursal);
    
    /**
     * Elimina una sucursal
     */
    boolean eliminarSucursal(int id);
}

