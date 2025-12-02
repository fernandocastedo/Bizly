package com.bizly.app.core.mapper;

import com.bizly.app.data.local.entity.VentaEntity;
import com.bizly.app.domain.model.Venta;

/**
 * Mapper para convertir entre VentaEntity y Venta (Model)
 */
public class VentaMapper {
    
    /**
     * Convierte VentaEntity a Venta (Model)
     */
    public static Venta toModel(VentaEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Venta venta = new Venta();
        venta.setId(entity.id);
        venta.setEmpresaId(entity.empresaId);
        venta.setSucursalId(entity.sucursalId);
        venta.setUsuarioId(entity.usuarioId);
        venta.setClienteId(entity.clienteId);
        venta.setFecha(entity.fecha);
        venta.setMetodoPago(entity.metodoPago);
        venta.setTotal(entity.total);
        venta.setEnvio(entity.esEnvio);
        venta.setEstadoPago(entity.estadoPago);
        venta.setEstadoPedido(entity.estadoPedido);
        venta.setCreatedAt(entity.createdAt);
        
        return venta;
    }
    
    /**
     * Convierte Venta (Model) a VentaEntity
     */
    public static VentaEntity toEntity(Venta model) {
        if (model == null) {
            return null;
        }
        
        VentaEntity entity = new VentaEntity();
        entity.id = model.getId();
        entity.empresaId = model.getEmpresaId();
        entity.sucursalId = model.getSucursalId();
        entity.usuarioId = model.getUsuarioId();
        entity.clienteId = model.getClienteId();
        entity.fecha = model.getFecha();
        entity.metodoPago = model.getMetodoPago();
        entity.total = model.getTotal();
        entity.esEnvio = model.isEnvio();
        entity.estadoPago = model.getEstadoPago();
        entity.estadoPedido = model.getEstadoPedido();
        entity.createdAt = model.getCreatedAt();
        
        return entity;
    }
}

