package com.bizly.app.core.mapper;

import com.bizly.app.data.local.entity.CategoriaEntity;
import com.bizly.app.domain.model.Categoria;

/**
 * Mapper para convertir entre CategoriaEntity y Categoria (Model)
 */
public class CategoriaMapper {
    
    /**
     * Convierte CategoriaEntity a Categoria (Model)
     */
    public static Categoria toModel(CategoriaEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Categoria categoria = new Categoria();
        categoria.setId(entity.id);
        categoria.setEmpresaId(entity.empresaId);
        categoria.setNombre(entity.nombre);
        categoria.setDescripcion(entity.descripcion);
        categoria.setCreatedAt(entity.createdAt);
        
        return categoria;
    }
    
    /**
     * Convierte Categoria (Model) a CategoriaEntity
     */
    public static CategoriaEntity toEntity(Categoria model) {
        if (model == null) {
            return null;
        }
        
        CategoriaEntity entity = new CategoriaEntity();
        entity.id = model.getId();
        entity.empresaId = model.getEmpresaId();
        entity.nombre = model.getNombre();
        entity.descripcion = model.getDescripcion();
        entity.createdAt = model.getCreatedAt();
        
        return entity;
    }
}

