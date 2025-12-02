package com.bizly.app.data.repository;

import com.bizly.app.domain.model.Categoria;
import java.util.List;

/**
 * Interfaz del repositorio de categorías
 * Define las operaciones de acceso a datos para categorías
 */
public interface CategoriaRepository {
    
    /**
     * Registra una nueva categoría
     */
    Categoria registrarCategoria(Categoria categoria);
    
    /**
     * Obtiene una categoría por ID
     */
    Categoria obtenerCategoriaPorId(int id);
    
    /**
     * Obtiene todas las categorías de una empresa
     */
    List<Categoria> obtenerCategoriasPorEmpresa(int empresaId);
    
    /**
     * Actualiza una categoría
     */
    boolean actualizarCategoria(Categoria categoria);
    
    /**
     * Elimina una categoría
     */
    boolean eliminarCategoria(int id);
}

