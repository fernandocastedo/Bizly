package com.bizly.app.core.mapper;

import com.bizly.app.data.local.entity.SucursalEntity;
import com.bizly.app.domain.model.Sucursal;

/**
 * Mapper para convertir entre SucursalEntity y Sucursal (Model)
 */
public class SucursalMapper {
    
    /**
     * Convierte SucursalEntity a Sucursal (Model)
     */
    public static Sucursal toModel(SucursalEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Sucursal sucursal = new Sucursal();
        sucursal.setId(entity.id);
        sucursal.setEmpresaId(entity.empresaId);
        sucursal.setNombre(entity.nombre);
        sucursal.setDireccion(entity.direccion);
        sucursal.setCiudad(entity.ciudad);
        sucursal.setLatitud(entity.latitud);
        sucursal.setLongitud(entity.longitud);
        sucursal.setDepartamento(entity.departamento);
        sucursal.setTelefono(entity.telefono);
        sucursal.setCreatedAt(entity.createdAt);
        sucursal.setUpdatedAt(entity.updatedAt);
        
        return sucursal;
    }
    
    /**
     * Convierte Sucursal (Model) a SucursalEntity
     */
    public static SucursalEntity toEntity(Sucursal model) {
        if (model == null) {
            return null;
        }
        
        SucursalEntity entity = new SucursalEntity();
        entity.id = model.getId();
        entity.empresaId = model.getEmpresaId();
        entity.nombre = model.getNombre();
        entity.direccion = model.getDireccion();
        entity.ciudad = model.getCiudad();
        entity.latitud = model.getLatitud();
        entity.longitud = model.getLongitud();
        entity.departamento = model.getDepartamento();
        entity.telefono = model.getTelefono();
        entity.createdAt = model.getCreatedAt();
        entity.updatedAt = model.getUpdatedAt();
        
        return entity;
    }
}

