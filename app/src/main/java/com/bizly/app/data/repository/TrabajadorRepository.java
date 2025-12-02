package com.bizly.app.data.repository;

import com.bizly.app.domain.model.Trabajador;
import java.util.List;

/**
 * Interfaz del repositorio de trabajadores
 * Define las operaciones de acceso a datos para trabajadores
 * RF-46, RF-47, RF-51
 */
public interface TrabajadorRepository {
    
    /**
     * Registra un nuevo trabajador (RF-46)
     */
    Trabajador registrarTrabajador(Trabajador trabajador);
    
    /**
     * Obtiene un trabajador por ID
     */
    Trabajador obtenerTrabajadorPorId(int id);
    
    /**
     * Obtiene todos los trabajadores de una empresa (RF-46, RF-51)
     */
    List<Trabajador> obtenerTrabajadoresPorEmpresa(int empresaId);
    
    /**
     * Obtiene trabajadores por empresa y sucursal
     */
    List<Trabajador> obtenerTrabajadoresPorEmpresaYSucursal(int empresaId, int sucursalId);
    
    /**
     * Actualiza un trabajador (RF-47)
     */
    boolean actualizarTrabajador(Trabajador trabajador);
    
    /**
     * Elimina un trabajador (RF-47)
     */
    boolean eliminarTrabajador(int id);
}

