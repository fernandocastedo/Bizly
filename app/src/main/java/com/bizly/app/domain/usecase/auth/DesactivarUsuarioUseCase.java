package com.bizly.app.domain.usecase.auth;

import android.content.Context;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.UsuarioRepository;
import com.bizly.app.data.repository.impl.UsuarioRepositoryLocal;

/**
 * Caso de uso para desactivar un usuario
 * RF-52
 */
public class DesactivarUsuarioUseCase {
    
    private final UsuarioRepository usuarioRepository;
    
    public DesactivarUsuarioUseCase(Context context) {
        this.usuarioRepository = new UsuarioRepositoryLocal(context);
    }
    
    /**
     * Desactiva un usuario
     * @param usuarioId ID del usuario a desactivar
     * @throws AppException si el usuario no existe o no se puede desactivar
     */
    public void ejecutar(int usuarioId) throws AppException {
        // Verificar que el usuario existe
        if (usuarioRepository.obtenerUsuarioPorId(usuarioId) == null) {
            throw new AppException("El usuario no existe");
        }
        
        // Desactivar usuario
        boolean exito = usuarioRepository.desactivarUsuario(usuarioId);
        if (!exito) {
            throw new AppException("No se pudo desactivar el usuario");
        }
    }
}

