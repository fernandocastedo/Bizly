package com.bizly.app.data.repository;

import com.bizly.app.domain.model.Usuario;

/**
 * Interfaz del repositorio de usuarios
 * Define las operaciones de acceso a datos para usuarios
 * RF-01, RF-02, RF-48, RF-49, RF-52
 */
public interface UsuarioRepository {
    
    /**
     * Registra un nuevo usuario emprendedor (RF-01)
     */
    Usuario registrarUsuario(Usuario usuario);
    
    /**
     * Inicia sesión con email y contraseña (RF-02, RF-49)
     */
    Usuario iniciarSesion(String email, String password);
    
    /**
     * Obtiene un usuario por ID
     */
    Usuario obtenerUsuarioPorId(int id);
    
    /**
     * Obtiene un usuario por email
     */
    Usuario obtenerUsuarioPorEmail(String email);
    
    /**
     * Crea un usuario vinculado a un trabajador (RF-48)
     */
    Usuario crearUsuarioTrabajador(Usuario usuario);
    
    /**
     * Desactiva un usuario (RF-52)
     */
    boolean desactivarUsuario(int usuarioId);
    
    /**
     * Actualiza un usuario
     */
    boolean actualizarUsuario(Usuario usuario);
}

