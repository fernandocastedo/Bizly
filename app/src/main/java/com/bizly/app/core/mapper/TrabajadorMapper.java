package com.bizly.app.core.mapper;

import com.bizly.app.data.local.entity.TrabajadorEntity;
import com.bizly.app.domain.model.Trabajador;

/**
 * Mapper para convertir entre TrabajadorEntity y Trabajador (Model)
 * RF-46, RF-47
 */
public class TrabajadorMapper {
    
    /**
     * Convierte TrabajadorEntity a Trabajador (Model)
     */
    public static Trabajador toModel(TrabajadorEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Trabajador trabajador = new Trabajador();
        trabajador.setId(entity.id);
        trabajador.setEmpresaId(entity.empresaId);
        trabajador.setSucursalId(entity.sucursalId);
        trabajador.setNombre(entity.nombre);
        trabajador.setCargo(entity.cargo);
        trabajador.setSueldoMensual(entity.sueldoMensual);
        trabajador.setTipoGasto(entity.tipoGasto);
        trabajador.setCreatedAt(entity.createdAt);
        trabajador.setUpdatedAt(entity.updatedAt);
        
        return trabajador;
    }
    
    /**
     * Convierte Trabajador (Model) a TrabajadorEntity
     */
    public static TrabajadorEntity toEntity(Trabajador model) {
        if (model == null) {
            return null;
        }
        
        TrabajadorEntity entity = new TrabajadorEntity();
        entity.id = model.getId();
        entity.empresaId = model.getEmpresaId();
        entity.sucursalId = model.getSucursalId();
        entity.nombre = model.getNombre();
        entity.cargo = model.getCargo();
        entity.sueldoMensual = model.getSueldoMensual();
        entity.tipoGasto = model.getTipoGasto();
        entity.createdAt = model.getCreatedAt();
        entity.updatedAt = model.getUpdatedAt();
        
        return entity;
    }
}

