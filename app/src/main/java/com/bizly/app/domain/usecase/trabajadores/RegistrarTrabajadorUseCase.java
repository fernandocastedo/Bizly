package com.bizly.app.domain.usecase.trabajadores;

import android.content.Context;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.TrabajadorRepository;
import com.bizly.app.data.repository.impl.TrabajadorRepositoryLocal;
import com.bizly.app.domain.model.Trabajador;

/**
 * Caso de uso para registrar un nuevo trabajador
 * RF-46
 */
public class RegistrarTrabajadorUseCase {
    
    private final TrabajadorRepository trabajadorRepository;
    
    public RegistrarTrabajadorUseCase(Context context) {
        this.trabajadorRepository = new TrabajadorRepositoryLocal(context);
    }
    
    /**
     * Registra un nuevo trabajador
     * @param trabajador Trabajador a registrar
     * @return Trabajador registrado con ID asignado
     * @throws AppException si hay errores de validación
     */
    public Trabajador ejecutar(Trabajador trabajador) throws AppException {
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
        
        // Registrar trabajador
        return trabajadorRepository.registrarTrabajador(trabajador);
    }
}

