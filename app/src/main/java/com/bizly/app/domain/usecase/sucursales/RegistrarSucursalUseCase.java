package com.bizly.app.domain.usecase.sucursales;

import android.content.Context;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.SucursalRepository;
import com.bizly.app.data.repository.impl.SucursalRepositoryLocal;
import com.bizly.app.domain.model.Sucursal;

/**
 * Caso de uso para registrar una nueva sucursal
 */
public class RegistrarSucursalUseCase {
    
    private final SucursalRepository sucursalRepository;
    
    public RegistrarSucursalUseCase(Context context) {
        this.sucursalRepository = new SucursalRepositoryLocal(context);
    }
    
    /**
     * Registra una nueva sucursal
     * @param sucursal Sucursal a registrar
     * @return Sucursal registrada con ID asignado
     * @throws AppException si hay errores de validación
     */
    public Sucursal ejecutar(Sucursal sucursal) throws AppException {
        // Validar nombre
        if (sucursal.getNombre() == null || sucursal.getNombre().trim().isEmpty()) {
            throw new AppException("El nombre de la sucursal es requerido");
        }
        
        // Validar dirección
        if (sucursal.getDireccion() == null || sucursal.getDireccion().trim().isEmpty()) {
            throw new AppException("La dirección es requerida");
        }
        
        // Validar ciudad
        if (sucursal.getCiudad() == null || sucursal.getCiudad().trim().isEmpty()) {
            throw new AppException("La ciudad es requerida");
        }
        
        // Registrar sucursal
        return sucursalRepository.registrarSucursal(sucursal);
    }
}


