package com.bizly.app.core.mapper;

import com.bizly.app.data.local.entity.UsuarioEntity;
import com.bizly.app.domain.model.Usuario;

/**
 * Mapper para convertir entre UsuarioEntity y Usuario (Model)
 */
public class UsuarioMapper {
    
    /**
     * Convierte UsuarioEntity a Usuario (Model)
     */
    public static Usuario toModel(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Usuario usuario = new Usuario();
        usuario.setId(entity.id);
        usuario.setEmpresaId(entity.empresaId);
        usuario.setSucursalId(entity.sucursalId);
        usuario.setTrabajadorId(entity.trabajadorId);
        usuario.setNombre(entity.nombre);
        usuario.setEmail(entity.email);
        usuario.setPassword(entity.password);
        usuario.setTipoUsuario(entity.tipoUsuario);
        usuario.setActivo(entity.activo);
        usuario.setCreatedAt(entity.createdAt);
        usuario.setUpdatedAt(entity.updatedAt);
        
        return usuario;
    }
    
    /**
     * Convierte Usuario (Model) a UsuarioEntity
     */
    public static UsuarioEntity toEntity(Usuario model) {
        if (model == null) {
            return null;
        }
        
        UsuarioEntity entity = new UsuarioEntity();
        entity.id = model.getId();
        entity.empresaId = model.getEmpresaId();
        entity.sucursalId = model.getSucursalId();
        entity.trabajadorId = model.getTrabajadorId();
        entity.nombre = model.getNombre();
        entity.email = model.getEmail();
        entity.password = model.getPassword();
        entity.tipoUsuario = model.getTipoUsuario();
        entity.activo = model.isActivo();
        entity.createdAt = model.getCreatedAt();
        entity.updatedAt = model.getUpdatedAt();
        
        return entity;
    }
}

