package com.bizly.app.presentation.dashboard;

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
import com.bizly.app.presentation.base.BaseState;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para la pantalla de dashboard
 * RF-07
 */
public class DashboardViewModel extends AndroidViewModel {
    
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    // LiveData para el estado
    private MutableLiveData<DashboardState> dashboardState = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Empresa> empresa = new MutableLiveData<>();
    private MutableLiveData<Usuario> usuario = new MutableLiveData<>();
    
    // Usuario actual (se establece desde la Activity)
    private int usuarioId;
    
    public DashboardViewModel(Application application) {
        super(application);
        this.empresaRepository = new EmpresaRepositoryLocal(application);
        this.usuarioRepository = new UsuarioRepositoryLocal(application);
        dashboardState.setValue(new DashboardState(BaseState.STATE_IDLE));
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
     * Carga los datos del emprendimiento y usuario
     */
    public void cargarDatos() {
        if (usuarioId <= 0) {
            errorMessage.postValue("Usuario no identificado");
            return;
        }
        
        // Mostrar loading
        isLoading.postValue(true);
        dashboardState.postValue(new DashboardState(BaseState.STATE_LOADING));
        
        // Ejecutar en background
        executor.execute(() -> {
            try {
                // Cargar usuario
                Usuario usuarioActual = usuarioRepository.obtenerUsuarioPorId(usuarioId);
                if (usuarioActual == null) {
                    errorMessage.postValue("Usuario no encontrado");
                    dashboardState.postValue(new DashboardState(BaseState.STATE_ERROR, "Usuario no encontrado"));
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
                dashboardState.postValue(new DashboardState(DashboardState.STATE_DATA_LOADED, "Datos cargados"));
                isLoading.postValue(false);
                
            } catch (Exception e) {
                // Error inesperado
                errorMessage.postValue("Error al cargar datos. Intente nuevamente.");
                dashboardState.postValue(new DashboardState(BaseState.STATE_ERROR, "Error inesperado"));
                isLoading.postValue(false);
            }
        });
    }
    
    /**
     * Recarga los datos del dashboard
     */
    public void recargarDatos() {
        cargarDatos();
    }
    
    // Getters para LiveData
    public MutableLiveData<DashboardState> getDashboardState() {
        return dashboardState;
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

