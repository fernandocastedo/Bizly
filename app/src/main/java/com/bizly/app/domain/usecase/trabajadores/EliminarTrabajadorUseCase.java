package com.bizly.app.domain.usecase.trabajadores;

import android.content.Context;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.TrabajadorRepository;
import com.bizly.app.data.repository.impl.TrabajadorRepositoryLocal;

/**
 * Caso de uso para eliminar un trabajador
 * RF-47
 */
public class EliminarTrabajadorUseCase {
    
    private final TrabajadorRepository trabajadorRepository;
    
    public EliminarTrabajadorUseCase(Context context) {
        this.trabajadorRepository = new TrabajadorRepositoryLocal(context);
    }
    
    /**
     * Elimina un trabajador
     * @param trabajadorId ID del trabajador a eliminar
     * @return true si se eliminó correctamente
     * @throws AppException si hay errores de validación
     */
    public boolean ejecutar(int trabajadorId) throws AppException {
        // Validar ID
        if (trabajadorId <= 0) {
            throw new AppException("El ID del trabajador debe ser válido");
        }
        
        // Verificar que el trabajador existe
        if (trabajadorRepository.obtenerTrabajadorPorId(trabajadorId) == null) {
            throw new AppException("El trabajador no existe");
        }
        
        // Eliminar trabajador
        return trabajadorRepository.eliminarTrabajador(trabajadorId);
    }
}


