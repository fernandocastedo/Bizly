package com.bizly.app.domain.usecase.auth;

import android.content.Context;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.UsuarioRepository;
import com.bizly.app.data.repository.impl.UsuarioRepositoryLocal;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.domain.service.HashPasswordService;
import com.bizly.app.domain.service.ValidacionEmailService;

/**
 * Caso de uso para registrar un nuevo usuario emprendedor
 * RF-01
 * 
 * Nota: La empresa debe estar creada previamente. Este UseCase solo registra el usuario.
 */
public class RegistrarUsuarioUseCase {
    
    private final UsuarioRepository usuarioRepository;
    
    public RegistrarUsuarioUseCase(Context context) {
        this.usuarioRepository = new UsuarioRepositoryLocal(context);
    }
    
    /**
     * Registra un nuevo usuario emprendedor asociado a una empresa existente
     * @param usuario Usuario a registrar (debe tener empresaId ya asignado)
     * @return Usuario registrado con ID asignado
     * @throws AppException si hay errores de validación
     */
    public Usuario ejecutar(Usuario usuario) throws AppException {
        // Validar que tiene empresaId
        if (usuario.getEmpresaId() <= 0) {
            throw new AppException("El usuario debe estar asociado a una empresa");
        }
        
        // Validar email
        if (!ValidacionEmailService.esEmailValido(usuario.getEmail())) {
            throw new AppException("El formato del email no es válido");
        }
        
        // Verificar que el email no esté en uso
        Usuario usuarioExistente = usuarioRepository.obtenerUsuarioPorEmail(usuario.getEmail());
        if (usuarioExistente != null) {
            throw new AppException("El email ya está registrado");
        }
        
        // Validar contraseña
        if (usuario.getPassword() == null || usuario.getPassword().length() < 6) {
            throw new AppException("La contraseña debe tener al menos 6 caracteres");
        }
        
        // Hash de la contraseña
        String passwordHash = HashPasswordService.hashPassword(usuario.getPassword());
        usuario.setPassword(passwordHash);
        
        // Establecer tipo de usuario como EMPRENDEDOR
        usuario.setTipoUsuario("EMPRENDEDOR");
        usuario.setActivo(true);
        
        // Registrar usuario
        return usuarioRepository.registrarUsuario(usuario);
    }
}

