package com.bizly.app.domain.usecase.emprendimiento;

import android.content.Context;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.EmpresaRepository;
import com.bizly.app.data.repository.SucursalRepository;
import com.bizly.app.data.repository.impl.EmpresaRepositoryLocal;
import com.bizly.app.data.repository.impl.SucursalRepositoryLocal;
import com.bizly.app.domain.model.Empresa;
import com.bizly.app.domain.model.Sucursal;
import java.util.Date;

/**
 * Caso de uso para registrar un nuevo emprendimiento
 * RF-03
 * Crea automáticamente la primera sucursal con la dirección de la empresa
 */
public class RegistrarEmprendimientoUseCase {
    
    private final EmpresaRepository empresaRepository;
    private final SucursalRepository sucursalRepository;
    
    public RegistrarEmprendimientoUseCase(Context context) {
        this.empresaRepository = new EmpresaRepositoryLocal(context);
        this.sucursalRepository = new SucursalRepositoryLocal(context);
    }
    
    /**
     * Registra un nuevo emprendimiento y crea automáticamente la primera sucursal
     * @param empresa Empresa/Emprendimiento a registrar
     * @param direccion Dirección de la empresa (para la sucursal automática)
     * @param ciudad Ciudad de la empresa (para la sucursal automática)
     * @param departamento Departamento de la empresa (opcional, para la sucursal automática)
     * @param telefono Teléfono de la empresa (opcional, para la sucursal automática)
     * @return Empresa registrada con ID asignado
     * @throws AppException si hay errores de validación
     */
    public Empresa ejecutar(Empresa empresa, String direccion, String ciudad, 
                            String departamento, String telefono) throws AppException {
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
        
        // Validar dirección (requerida para crear la sucursal automática)
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new AppException("La dirección es requerida");
        }
        
        // Validar ciudad (requerida para crear la sucursal automática)
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new AppException("La ciudad es requerida");
        }
        
        // Registrar empresa
        Empresa empresaCreada = empresaRepository.registrarEmpresa(empresa);
        
        // Crear automáticamente la primera sucursal con los datos de la empresa
        Sucursal sucursalInicial = new Sucursal();
        sucursalInicial.setEmpresaId(empresaCreada.getId());
        sucursalInicial.setNombre(empresaCreada.getNombre() + " - Sucursal Principal"); // Nombre por defecto
        sucursalInicial.setDireccion(direccion.trim());
        sucursalInicial.setCiudad(ciudad.trim());
        sucursalInicial.setDepartamento(departamento != null ? departamento.trim() : null);
        sucursalInicial.setTelefono(telefono != null ? telefono.trim() : null);
        sucursalInicial.setLatitud(0.0); // Por defecto
        sucursalInicial.setLongitud(0.0); // Por defecto
        sucursalInicial.setCreatedAt(new Date());
        sucursalInicial.setUpdatedAt(new Date());
        
        // Registrar la sucursal
        sucursalRepository.registrarSucursal(sucursalInicial);
        
        return empresaCreada;
    }
    
    /**
     * Método sobrecargado para compatibilidad (sin dirección - no crea sucursal automática)
     * @deprecated Use el método con dirección para crear automáticamente la sucursal
     */
    @Deprecated
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
        
        // Registrar empresa (sin crear sucursal automática)
        return empresaRepository.registrarEmpresa(empresa);
    }
}

