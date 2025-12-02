package com.bizly.app.core.mapper;

import com.bizly.app.data.local.entity.ClienteEntity;
import com.bizly.app.domain.model.Cliente;

/**
 * Mapper para convertir entre ClienteEntity y Cliente (Model)
 */
public class ClienteMapper {
    
    /**
     * Convierte ClienteEntity a Cliente (Model)
     */
    public static Cliente toModel(ClienteEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Cliente cliente = new Cliente();
        cliente.setId(entity.id);
        cliente.setEmpresaId(entity.empresaId);
        cliente.setSucursalId(entity.sucursalId);
        cliente.setNombre(entity.nombre);
        cliente.setNit(entity.nit);
        cliente.setTelefono(entity.telefono);
        cliente.setEmail(entity.email);
        cliente.setDireccion(entity.direccion);
        cliente.setCreatedAt(entity.createdAt);
        
        return cliente;
    }
    
    /**
     * Convierte Cliente (Model) a ClienteEntity
     */
    public static ClienteEntity toEntity(Cliente model) {
        if (model == null) {
            return null;
        }
        
        ClienteEntity entity = new ClienteEntity();
        entity.id = model.getId();
        entity.empresaId = model.getEmpresaId();
        entity.sucursalId = model.getSucursalId();
        entity.nombre = model.getNombre();
        entity.nit = model.getNit();
        entity.telefono = model.getTelefono();
        entity.email = model.getEmail();
        entity.direccion = model.getDireccion();
        entity.createdAt = model.getCreatedAt();
        
        return entity;
    }
}

