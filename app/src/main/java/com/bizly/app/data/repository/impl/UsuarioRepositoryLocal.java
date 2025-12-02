package com.bizly.app.data.repository.impl;

import android.content.Context;
import com.bizly.app.core.database.DatabaseHelper;
import com.bizly.app.core.mapper.UsuarioMapper;
import com.bizly.app.data.local.dao.UsuarioDao;
import com.bizly.app.data.local.entity.UsuarioEntity;
import com.bizly.app.data.repository.UsuarioRepository;
import com.bizly.app.domain.model.Usuario;
import java.util.Date;

/**
 * Implementación local del repositorio de usuarios usando Room
 * RF-01, RF-02, RF-48, RF-49, RF-52
 */
public class UsuarioRepositoryLocal implements UsuarioRepository {
    
    private final UsuarioDao usuarioDao;
    
    public UsuarioRepositoryLocal(Context context) {
        this.usuarioDao = DatabaseHelper.getDatabase(context).usuarioDao();
    }
    
    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        // Establecer fechas si no están establecidas
        if (usuario.getCreatedAt() == null) {
            usuario.setCreatedAt(new Date());
        }
        if (usuario.getUpdatedAt() == null) {
            usuario.setUpdatedAt(new Date());
        }
        
        UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
        long id = usuarioDao.insertar(entity);
        usuario.setId((int) id);
        return usuario;
    }
    
    @Override
    public Usuario iniciarSesion(String email, String password) {
        // Este método ya no se usa directamente
        // Se debe usar IniciarSesionUseCase que maneja el hash correctamente
        // Se mantiene por compatibilidad pero debería ser deprecado
        UsuarioEntity entity = usuarioDao.obtenerPorEmail(email);
        
        if (entity == null) {
            return null; // Usuario no encontrado
        }
        
        // Verificar contraseña (compatibilidad: si tiene formato hash, usar verificación)
        boolean passwordValido = false;
        if (entity.password.contains(":")) {
            // Formato con hash
            passwordValido = com.bizly.app.domain.service.HashPasswordService.verificarPassword(password, entity.password);
        } else {
            // Formato antiguo (texto plano) - para compatibilidad durante desarrollo
            passwordValido = entity.password.equals(password);
        }
        
        if (!passwordValido) {
            return null; // Contraseña incorrecta
        }
        
        // Verificar si el usuario está activo
        if (!entity.activo) {
            return null; // Usuario desactivado
        }
        
        return UsuarioMapper.toModel(entity);
    }
    
    @Override
    public Usuario obtenerUsuarioPorId(int id) {
        UsuarioEntity entity = usuarioDao.obtenerPorId(id);
        return UsuarioMapper.toModel(entity);
    }
    
    @Override
    public Usuario obtenerUsuarioPorEmail(String email) {
        UsuarioEntity entity = usuarioDao.obtenerPorEmail(email);
        return UsuarioMapper.toModel(entity);
    }
    
    @Override
    public Usuario crearUsuarioTrabajador(Usuario usuario) {
        // Establecer tipo de usuario como TRABAJADOR
        usuario.setTipoUsuario("TRABAJADOR");
        
        // Establecer fechas si no están establecidas
        if (usuario.getCreatedAt() == null) {
            usuario.setCreatedAt(new Date());
        }
        if (usuario.getUpdatedAt() == null) {
            usuario.setUpdatedAt(new Date());
        }
        
        UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
        long id = usuarioDao.insertar(entity);
        usuario.setId((int) id);
        return usuario;
    }
    
    @Override
    public boolean desactivarUsuario(int usuarioId) {
        try {
            usuarioDao.actualizarEstado(usuarioId, false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        try {
            usuario.setUpdatedAt(new Date());
            UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
            usuarioDao.actualizar(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

