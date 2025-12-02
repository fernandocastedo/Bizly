package com.bizly.app.domain.usecase.emprendimiento;

import android.content.Context;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.EmpresaRepository;
import com.bizly.app.data.repository.impl.EmpresaRepositoryLocal;
import com.bizly.app.domain.model.Empresa;

/**
 * Caso de uso para registrar un nuevo emprendimiento
 * RF-03
 */
public class RegistrarEmprendimientoUseCase {
    
    private final EmpresaRepository empresaRepository;
    
    public RegistrarEmprendimientoUseCase(Context context) {
        this.empresaRepository = new EmpresaRepositoryLocal(context);
    }
    
    /**
     * Registra un nuevo emprendimiento
     * @param empresa Empresa/Emprendimiento a registrar
     * @return Empresa registrada con ID asignado
     * @throws AppException si hay errores de validación
     */
    public Empresa ejecutar(Empresa empresa) throws AppException {
        // Validar nombre
        if (empresa.getNombre() == null || empresa.getNombre().trim().isEmpty()) {
            throw new AppException("El nombre del emprendimiento es requerido");
        }
        
        // Validar rubro
        if (empresa.getRubro() == null || empresa.getRubro().trim().isEmpty()) {
            throw new AppException("El rubro es requerido");
        }
        
        // Validar margen de ganancia (debe ser positivo)
        if (empresa.getMargenGanancia() < 0) {
            throw new AppException("El margen de ganancia debe ser un valor positivo");
        }
        
        // Registrar empresa
        return empresaRepository.registrarEmpresa(empresa);
    }
}

