package com.bizly.app.data.repository;

import com.bizly.app.domain.model.Empresa;
import java.util.List;

/**
 * Interfaz del repositorio de empresas
 * Define las operaciones de acceso a datos para empresas
 * RF-03, RF-04, RF-05, RF-06, RF-07
 */
public interface EmpresaRepository {
    
    /**
     * Registra una nueva empresa/emprendimiento (RF-03)
     */
    Empresa registrarEmpresa(Empresa empresa);
    
    /**
     * Obtiene una empresa por ID
     */
    Empresa obtenerEmpresaPorId(int id);
    
    /**
     * Obtiene todas las empresas
     */
    List<Empresa> obtenerTodasLasEmpresas();
    
    /**
     * Actualiza una empresa (RF-05)
     */
    boolean actualizarEmpresa(Empresa empresa);
    
    /**
     * Elimina una empresa
     */
    boolean eliminarEmpresa(int id);
}

