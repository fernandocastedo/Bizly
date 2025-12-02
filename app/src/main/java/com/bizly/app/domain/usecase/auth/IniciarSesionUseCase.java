package com.bizly.app.domain.usecase.auth;

import android.content.Context;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.UsuarioRepository;
import com.bizly.app.data.repository.impl.UsuarioRepositoryLocal;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.domain.service.HashPasswordService;
import com.bizly.app.domain.service.ValidacionEmailService;

/**
 * Caso de uso para iniciar sesión
 * RF-02, RF-49
 */
public class IniciarSesionUseCase {
    
    private final UsuarioRepository usuarioRepository;
    
    public IniciarSesionUseCase(Context context) {
        this.usuarioRepository = new UsuarioRepositoryLocal(context);
    }
    
    /**
     * Inicia sesión con email y contraseña
     * @param email Email del usuario
     * @param password Contraseña en texto plano
     * @return Usuario autenticado
     * @throws AppException si las credenciales son inválidas
     */
    public Usuario ejecutar(String email, String password) throws AppException {
        // Validar formato de email
        if (!ValidacionEmailService.esEmailValido(email)) {
            throw new AppException("El formato del email no es válido");
        }
        
        // Validar que la contraseña no esté vacía
        if (password == null || password.isEmpty()) {
            throw new AppException("La contraseña no puede estar vacía");
        }
        
        // Obtener usuario por email
        Usuario usuario = usuarioRepository.obtenerUsuarioPorEmail(email);
        if (usuario == null) {
            throw new AppException("Email o contraseña incorrectos");
        }
        
        // Verificar si el usuario está activo
        if (!usuario.isActivo()) {
            throw new AppException("El usuario está desactivado. Contacte al administrador");
        }
        
        // Verificar contraseña
        // Nota: Si el password almacenado no tiene formato "salt:hash", 
        // asumimos que es texto plano (para migración)
        boolean passwordValido = false;
        if (usuario.getPassword().contains(":")) {
            // Formato con hash
            passwordValido = HashPasswordService.verificarPassword(password, usuario.getPassword());
        } else {
            // Formato antiguo (texto plano) - para compatibilidad durante desarrollo
            passwordValido = usuario.getPassword().equals(password);
        }
        
        if (!passwordValido) {
            throw new AppException("Email o contraseña incorrectos");
        }
        
        return usuario;
    }
}

