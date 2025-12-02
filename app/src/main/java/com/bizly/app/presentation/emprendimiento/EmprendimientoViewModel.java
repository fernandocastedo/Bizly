package com.bizly.app.presentation.emprendimiento;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.EmpresaRepository;
import com.bizly.app.data.repository.UsuarioRepository;
import com.bizly.app.data.repository.impl.EmpresaRepositoryLocal;
import com.bizly.app.data.repository.impl.UsuarioRepositoryLocal;
import com.bizly.app.domain.model.Empresa;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.domain.usecase.emprendimiento.ActualizarEmprendimientoUseCase;
import com.bizly.app.presentation.base.BaseState;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para la pantalla de emprendimiento
 * RF-03, RF-04, RF-05, RF-06
 */
public class EmprendimientoViewModel extends AndroidViewModel {
    
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ActualizarEmprendimientoUseCase actualizarEmprendimientoUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    // LiveData para el estado
    private MutableLiveData<EmprendimientoState> emprendimientoState = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Empresa> empresa = new MutableLiveData<>();
    private MutableLiveData<Usuario> usuario = new MutableLiveData<>();
    
    // Usuario actual
    private int usuarioId;
    
    public EmprendimientoViewModel(Application application) {
        super(application);
        this.empresaRepository = new EmpresaRepositoryLocal(application);
        this.usuarioRepository = new UsuarioRepositoryLocal(application);
        this.actualizarEmprendimientoUseCase = new ActualizarEmprendimientoUseCase(application);
        emprendimientoState.setValue(new EmprendimientoState(BaseState.STATE_IDLE));
        isLoading.setValue(false);
    }
    
    /**
     * Establece el ID del usuario actual y carga los datos
     * @param usuarioId ID del usuario autenticado
     */
    public void cargarDatos(int usuarioId) {
        this.usuarioId = usuarioId;
        cargarDatos();
    }
    
    /**
     * Carga los datos del emprendimiento
     */
    public void cargarDatos() {
        if (usuarioId <= 0) {
            errorMessage.postValue("Usuario no identificado");
            return;
        }
        
        // Mostrar loading
        isLoading.postValue(true);
        emprendimientoState.postValue(new EmprendimientoState(BaseState.STATE_LOADING));
        
        // Ejecutar en background
        executor.execute(() -> {
            try {
                // Cargar usuario
                Usuario usuarioActual = usuarioRepository.obtenerUsuarioPorId(usuarioId);
                if (usuarioActual == null) {
                    errorMessage.postValue("Usuario no encontrado");
                    emprendimientoState.postValue(new EmprendimientoState(BaseState.STATE_ERROR, "Usuario no encontrado"));
                    isLoading.postValue(false);
                    return;
                }
                
                usuario.postValue(usuarioActual);
                
                // Cargar empresa
                if (usuarioActual.getEmpresaId() > 0) {
                    Empresa empresaActual = empresaRepository.obtenerEmpresaPorId(usuarioActual.getEmpresaId());
                    if (empresaActual != null) {
                        empresa.postValue(empresaActual);
                    }
                }
                
                // Datos cargados exitosamente
                emprendimientoState.postValue(new EmprendimientoState(BaseState.STATE_SUCCESS, "Datos cargados"));
                isLoading.postValue(false);
                
            } catch (Exception e) {
                // Error inesperado
                errorMessage.postValue("Error al cargar datos. Intente nuevamente.");
                emprendimientoState.postValue(new EmprendimientoState(BaseState.STATE_ERROR, "Error inesperado"));
                isLoading.postValue(false);
            }
        });
    }
    
    /**
     * Actualiza los datos del emprendimiento
     * @param nombre Nombre de la empresa
     * @param rubro Rubro de la empresa
     * @param descripcion Descripción de la empresa
     * @param margenGanancia Margen de ganancia
     * @param logoUrl URL o path del logo
     */
    public void actualizarEmprendimiento(String nombre, String rubro, String descripcion, 
                                         double margenGanancia, String logoUrl) {
        Empresa empresaActual = empresa.getValue();
        if (empresaActual == null) {
            errorMessage.postValue("No se pudo cargar la información del emprendimiento");
            return;
        }
        
        // Validar campos
        if (nombre == null || nombre.trim().isEmpty()) {
            errorMessage.postValue("El nombre de la empresa es requerido");
            return;
        }
        
        if (rubro == null || rubro.trim().isEmpty()) {
            errorMessage.postValue("El rubro es requerido");
            return;
        }
        
        if (margenGanancia < 0) {
            errorMessage.postValue("El margen de ganancia debe ser un valor positivo");
            return;
        }
        
        // Mostrar loading
        isLoading.postValue(true);
        emprendimientoState.postValue(new EmprendimientoState(BaseState.STATE_LOADING));
        
        // Ejecutar caso de uso en background
        executor.execute(() -> {
            try {
                // Actualizar datos de la empresa
                empresaActual.setNombre(nombre.trim());
                empresaActual.setRubro(rubro.trim());
                empresaActual.setDescripcion(descripcion != null ? descripcion.trim() : "");
                empresaActual.setMargenGanancia(margenGanancia);
                if (logoUrl != null && !logoUrl.isEmpty()) {
                    empresaActual.setLogoUrl(logoUrl);
                }
                
                boolean exito = actualizarEmprendimientoUseCase.ejecutar(empresaActual);
                
                if (exito) {
                    // Actualizar empresa en LiveData
                    empresa.postValue(empresaActual);
                    emprendimientoState.postValue(new EmprendimientoState(EmprendimientoState.STATE_UPDATE_SUCCESS, "Emprendimiento actualizado exitosamente"));
                } else {
                    errorMessage.postValue("No se pudo actualizar el emprendimiento");
                    emprendimientoState.postValue(new EmprendimientoState(BaseState.STATE_ERROR, "Error al actualizar"));
                }
                
                isLoading.postValue(false);
                
            } catch (AppException e) {
                // Error de validación
                errorMessage.postValue(e.getMessage());
                emprendimientoState.postValue(new EmprendimientoState(BaseState.STATE_ERROR, e.getMessage()));
                isLoading.postValue(false);
            } catch (Exception e) {
                // Error inesperado
                errorMessage.postValue("Error al actualizar emprendimiento. Intente nuevamente.");
                emprendimientoState.postValue(new EmprendimientoState(BaseState.STATE_ERROR, "Error inesperado"));
                isLoading.postValue(false);
            }
        });
    }
    
    // Getters para LiveData
    public MutableLiveData<EmprendimientoState> getEmprendimientoState() {
        return emprendimientoState;
    }
    
    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public MutableLiveData<Empresa> getEmpresa() {
        return empresa;
    }
    
    public MutableLiveData<Usuario> getUsuario() {
        return usuario;
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}

