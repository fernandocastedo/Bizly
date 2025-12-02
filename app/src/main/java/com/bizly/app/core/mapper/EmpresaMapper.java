package com.bizly.app.core.mapper;

import com.bizly.app.data.local.entity.EmpresaEntity;
import com.bizly.app.domain.model.Empresa;

/**
 * Mapper para convertir entre EmpresaEntity y Empresa (Model)
 */
public class EmpresaMapper {
    
    /**
     * Convierte EmpresaEntity a Empresa (Model)
     */
    public static Empresa toModel(EmpresaEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Empresa empresa = new Empresa();
        empresa.setId(entity.id);
        empresa.setNombre(entity.nombre);
        empresa.setRubro(entity.rubro);
        empresa.setDescripcion(entity.descripcion);
        empresa.setMargenGanancia(entity.margenGanancia);
        empresa.setLogoUrl(entity.logoUrl);
        empresa.setCreatedAt(entity.createdAt);
        empresa.setUpdatedAt(entity.updatedAt);
        
        return empresa;
    }
    
    /**
     * Convierte Empresa (Model) a EmpresaEntity
     */
    public static EmpresaEntity toEntity(Empresa model) {
        if (model == null) {
            return null;
        }
        
        EmpresaEntity entity = new EmpresaEntity();
        entity.id = model.getId();
        entity.nombre = model.getNombre();
        entity.rubro = model.getRubro();
        entity.descripcion = model.getDescripcion();
        entity.margenGanancia = model.getMargenGanancia();
        entity.logoUrl = model.getLogoUrl();
        entity.createdAt = model.getCreatedAt();
        entity.updatedAt = model.getUpdatedAt();
        
        return entity;
    }
}

