package com.bizly.app.domain.usecase.trabajadores;

import android.content.Context;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.TrabajadorRepository;
import com.bizly.app.data.repository.UsuarioRepository;
import com.bizly.app.data.repository.impl.TrabajadorRepositoryLocal;
import com.bizly.app.data.repository.impl.UsuarioRepositoryLocal;
import com.bizly.app.domain.model.Trabajador;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.domain.service.HashPasswordService;
import com.bizly.app.domain.service.ValidacionEmailService;

/**
 * Caso de uso para crear un usuario vinculado a un trabajador
 * RF-48
 */
public class CrearUsuarioTrabajadorUseCase {
    
    private final UsuarioRepository usuarioRepository;
    private final TrabajadorRepository trabajadorRepository;
    
    public CrearUsuarioTrabajadorUseCase(Context context) {
        this.usuarioRepository = new UsuarioRepositoryLocal(context);
        this.trabajadorRepository = new TrabajadorRepositoryLocal(context);
    }
    
    /**
     * Crea un usuario vinculado a un trabajador existente
     * @param usuario Usuario a crear (debe tener trabajadorId)
     * @return Usuario creado con ID asignado
     * @throws AppException si hay errores de validación
     */
    public Usuario ejecutar(Usuario usuario) throws AppException {
        // Validar que tiene trabajadorId
        if (usuario.getTrabajadorId() == null) {
            throw new AppException("El usuario debe estar vinculado a un trabajador");
        }
        
        // Verificar que el trabajador existe
        Trabajador trabajador = trabajadorRepository.obtenerTrabajadorPorId(usuario.getTrabajadorId());
        if (trabajador == null) {
            throw new AppException("El trabajador no existe");
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
        
        // Establecer tipo de usuario como TRABAJADOR
        usuario.setTipoUsuario("TRABAJADOR");
        usuario.setActivo(true);
        
        // Asociar con la empresa y sucursal del trabajador
        usuario.setEmpresaId(trabajador.getEmpresaId());
        usuario.setSucursalId(trabajador.getSucursalId());
        
        // Crear usuario
        return usuarioRepository.crearUsuarioTrabajador(usuario);
    }
}

