package com.bizly.app.presentation.sucursales;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.bizly.app.data.repository.SucursalRepository;
import com.bizly.app.data.repository.UsuarioRepository;
import com.bizly.app.data.repository.impl.SucursalRepositoryLocal;
import com.bizly.app.data.repository.impl.UsuarioRepositoryLocal;
import com.bizly.app.domain.model.Sucursal;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.presentation.base.BaseState;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para la pantalla de sucursales
 */
public class SucursalesViewModel extends AndroidViewModel {
    
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    // LiveData para el estado
    private MutableLiveData<BaseState> state = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<Sucursal>> sucursales = new MutableLiveData<>();
    
    // Usuario actual
    private int usuarioId;
    private int empresaId;
    
    public SucursalesViewModel(Application application) {
        super(application);
        this.sucursalRepository = new SucursalRepositoryLocal(application);
        this.usuarioRepository = new UsuarioRepositoryLocal(application);
        state.setValue(new BaseState(BaseState.STATE_IDLE));
        isLoading.setValue(false);
        sucursales.setValue(new ArrayList<>());
    }
    
    /**
     * Establece el ID del usuario actual y carga las sucursales
     * @param usuarioId ID del usuario autenticado
     */
    public void cargarSucursales(int usuarioId) {
        this.usuarioId = usuarioId;
        cargarSucursales();
    }
    
    /**
     * Carga las sucursales de la empresa
     */
    public void cargarSucursales() {
        if (usuarioId <= 0) {
            errorMessage.postValue("Usuario no identificado");
            return;
        }
        
        // Mostrar loading
        isLoading.postValue(true);
        state.postValue(new BaseState(BaseState.STATE_LOADING));
        
        // Ejecutar en background
        executor.execute(() -> {
            try {
                // Cargar usuario para obtener empresaId
                Usuario usuario = usuarioRepository.obtenerUsuarioPorId(usuarioId);
                if (usuario == null) {
                    errorMessage.postValue("Usuario no encontrado");
                    state.postValue(new BaseState(BaseState.STATE_ERROR, "Usuario no encontrado"));
                    isLoading.postValue(false);
                    return;
                }
                
                empresaId = usuario.getEmpresaId();
                
                // Cargar sucursales
                List<Sucursal> sucursalesList = sucursalRepository.obtenerSucursalesPorEmpresa(empresaId);
                sucursales.postValue(sucursalesList != null ? sucursalesList : new ArrayList<>());
                
                // Datos cargados exitosamente
                state.postValue(new BaseState(BaseState.STATE_SUCCESS, "Sucursales cargadas"));
                isLoading.postValue(false);
                
            } catch (Exception e) {
                // Error inesperado
                errorMessage.postValue("Error al cargar sucursales. Intente nuevamente.");
                state.postValue(new BaseState(BaseState.STATE_ERROR, "Error inesperado"));
                isLoading.postValue(false);
            }
        });
    }
    
    /**
     * Recarga las sucursales
     */
    public void recargarSucursales() {
        cargarSucursales();
    }
    
    public int getEmpresaId() {
        return empresaId;
    }
    
    // Getters para LiveData
    public MutableLiveData<BaseState> getState() {
        return state;
    }
    
    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public MutableLiveData<List<Sucursal>> getSucursales() {
        return sucursales;
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}


