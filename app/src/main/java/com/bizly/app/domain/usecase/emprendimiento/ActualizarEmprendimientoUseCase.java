package com.bizly.app.domain.usecase.emprendimiento;

import android.content.Context;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.EmpresaRepository;
import com.bizly.app.data.repository.impl.EmpresaRepositoryLocal;
import com.bizly.app.domain.model.Empresa;

/**
 * Caso de uso para actualizar datos del emprendimiento
 * RF-05
 */
public class ActualizarEmprendimientoUseCase {
    
    private final EmpresaRepository empresaRepository;
    
    public ActualizarEmprendimientoUseCase(Context context) {
        this.empresaRepository = new EmpresaRepositoryLocal(context);
    }
    
    /**
     * Actualiza los datos de un emprendimiento
     * @param empresa Empresa con los datos actualizados
     * @return true si se actualizó correctamente
     * @throws AppException si hay errores de validación o el emprendimiento no existe
     */
    public boolean ejecutar(Empresa empresa) throws AppException {
        // Validar que la empresa existe
        Empresa empresaExistente = empresaRepository.obtenerEmpresaPorId(empresa.getId());
        if (empresaExistente == null) {
            throw new AppException("El emprendimiento no existe");
        }
        
        // Validar nombre
        if (empresa.getNombre() == null || empresa.getNombre().trim().isEmpty()) {
            throw new AppException("El nombre del emprendimiento es requerido");
        }
        
        // Validar rubro
        if (empresa.getRubro() == null || empresa.getRubro().trim().isEmpty()) {
            throw new AppException("El rubro es requerido");
        }
        
        // Validar margen de ganancia
        if (empresa.getMargenGanancia() < 0) {
            throw new AppException("El margen de ganancia debe ser un valor positivo");
        }
        
        // Actualizar empresa
        boolean exito = empresaRepository.actualizarEmpresa(empresa);
        if (!exito) {
            throw new AppException("No se pudo actualizar el emprendimiento");
        }
        
        return true;
    }
}

