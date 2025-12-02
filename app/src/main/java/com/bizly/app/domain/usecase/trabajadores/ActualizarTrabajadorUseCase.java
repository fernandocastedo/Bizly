package com.bizly.app.domain.usecase.trabajadores;

import android.content.Context;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.TrabajadorRepository;
import com.bizly.app.data.repository.impl.TrabajadorRepositoryLocal;
import com.bizly.app.domain.model.Trabajador;

/**
 * Caso de uso para actualizar un trabajador existente
 * RF-47
 */
public class ActualizarTrabajadorUseCase {
    
    private final TrabajadorRepository trabajadorRepository;
    
    public ActualizarTrabajadorUseCase(Context context) {
        this.trabajadorRepository = new TrabajadorRepositoryLocal(context);
    }
    
    /**
     * Actualiza un trabajador existente
     * @param trabajador Trabajador con los datos actualizados
     * @return true si se actualizó correctamente
     * @throws AppException si hay errores de validación
     */
    public boolean ejecutar(Trabajador trabajador) throws AppException {
        // Validar que el trabajador tenga ID
        if (trabajador.getId() <= 0) {
            throw new AppException("El trabajador debe tener un ID válido");
        }
        
        // Validar nombre
        if (trabajador.getNombre() == null || trabajador.getNombre().trim().isEmpty()) {
            throw new AppException("El nombre del trabajador es requerido");
        }
        
        // Validar cargo
        if (trabajador.getCargo() == null || trabajador.getCargo().trim().isEmpty()) {
            throw new AppException("El cargo es requerido");
        }
        
        // Validar sueldo
        if (trabajador.getSueldoMensual() < 0) {
            throw new AppException("El sueldo mensual debe ser un valor positivo");
        }
        
        // Validar tipo de gasto
        if (trabajador.getTipoGasto() == null || 
            (!trabajador.getTipoGasto().equals("fijo") && !trabajador.getTipoGasto().equals("variable"))) {
            throw new AppException("El tipo de gasto debe ser 'fijo' o 'variable'");
        }
        
        // Validar que pertenezca a una sucursal (requerido)
        if (trabajador.getSucursalId() == null || trabajador.getSucursalId() <= 0) {
            throw new AppException("El trabajador debe pertenecer a una sucursal");
        }
        
        // Actualizar trabajador
        return trabajadorRepository.actualizarTrabajador(trabajador);
    }
}


