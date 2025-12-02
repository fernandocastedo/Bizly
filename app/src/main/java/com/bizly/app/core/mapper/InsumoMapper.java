package com.bizly.app.core.mapper;

import com.bizly.app.data.local.entity.InsumoEntity;
import com.bizly.app.domain.model.Insumo;

/**
 * Mapper para convertir entre InsumoEntity y Insumo (Model)
 */
public class InsumoMapper {
    
    /**
     * Convierte InsumoEntity a Insumo (Model)
     */
    public static Insumo toModel(InsumoEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Insumo insumo = new Insumo();
        insumo.setId(entity.id);
        insumo.setEmpresaId(entity.empresaId);
        insumo.setSucursalId(entity.sucursalId);
        insumo.setCategoriaId(entity.categoriaId);
        insumo.setNombre(entity.nombre);
        insumo.setDescripcion(entity.descripcion);
        insumo.setCantidad(entity.cantidad);
        insumo.setUnidadMedida(entity.unidadMedida);
        insumo.setPrecioUnitario(entity.precioUnitario);
        insumo.setPrecioTotal(entity.precioTotal);
        insumo.setStockMinimo(entity.stockMinimo);
        insumo.setActivo(entity.activo);
        insumo.setCreatedAt(entity.createdAt);
        insumo.setUpdatedAt(entity.updatedAt);
        
        return insumo;
    }
    
    /**
     * Convierte Insumo (Model) a InsumoEntity
     */
    public static InsumoEntity toEntity(Insumo model) {
        if (model == null) {
            return null;
        }
        
        InsumoEntity entity = new InsumoEntity();
        entity.id = model.getId();
        entity.empresaId = model.getEmpresaId();
        entity.sucursalId = model.getSucursalId();
        entity.categoriaId = model.getCategoriaId();
        entity.nombre = model.getNombre();
        entity.descripcion = model.getDescripcion();
        entity.cantidad = model.getCantidad();
        entity.unidadMedida = model.getUnidadMedida();
        entity.precioUnitario = model.getPrecioUnitario();
        entity.precioTotal = model.getPrecioTotal();
        entity.stockMinimo = model.getStockMinimo();
        entity.activo = model.isActivo();
        entity.createdAt = model.getCreatedAt();
        entity.updatedAt = model.getUpdatedAt();
        
        return entity;
    }
}

