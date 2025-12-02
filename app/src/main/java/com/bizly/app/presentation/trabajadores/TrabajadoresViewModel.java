package com.bizly.app.presentation.trabajadores;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.bizly.app.data.repository.SucursalRepository;
import com.bizly.app.data.repository.TrabajadorRepository;
import com.bizly.app.data.repository.UsuarioRepository;
import com.bizly.app.data.repository.impl.SucursalRepositoryLocal;
import com.bizly.app.data.repository.impl.TrabajadorRepositoryLocal;
import com.bizly.app.data.repository.impl.UsuarioRepositoryLocal;
import com.bizly.app.domain.model.Sucursal;
import com.bizly.app.domain.model.Trabajador;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.presentation.base.BaseState;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para la pantalla de trabajadores
 * RF-46, RF-47, RF-51
 */
public class TrabajadoresViewModel extends AndroidViewModel {
    
    private final TrabajadorRepository trabajadorRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    // LiveData para el estado
    private MutableLiveData<BaseState> state = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<List<Trabajador>> trabajadores = new MutableLiveData<>();
    private MutableLiveData<List<Sucursal>> sucursales = new MutableLiveData<>();
    
    // Usuario actual y filtros
    private int usuarioId;
    private int empresaId;
    private Integer sucursalIdFiltro = null; // null = todas las sucursales
    
    public TrabajadoresViewModel(Application application) {
        super(application);
        this.trabajadorRepository = new TrabajadorRepositoryLocal(application);
        this.sucursalRepository = new SucursalRepositoryLocal(application);
        this.usuarioRepository = new UsuarioRepositoryLocal(application);
        state.setValue(new BaseState(BaseState.STATE_IDLE));
        isLoading.setValue(false);
        trabajadores.setValue(new ArrayList<>());
        sucursales.setValue(new ArrayList<>());
    }
    
    /**
     * Establece el ID del usuario actual y carga los trabajadores
     * @param usuarioId ID del usuario autenticado
     */
    public void cargarTrabajadores(int usuarioId) {
        this.usuarioId = usuarioId;
        cargarDatos();
    }
    
    /**
     * Carga los trabajadores y sucursales
     */
    public void cargarDatos() {
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
                
                // Cargar trabajadores
                cargarTrabajadoresFiltrados();
                
            } catch (Exception e) {
                // Error inesperado
                errorMessage.postValue("Error al cargar datos. Intente nuevamente.");
                state.postValue(new BaseState(BaseState.STATE_ERROR, "Error inesperado"));
                isLoading.postValue(false);
            }
        });
    }
    
    /**
     * Carga trabajadores según el filtro de sucursal
     */
    private void cargarTrabajadoresFiltrados() {
        List<Trabajador> trabajadoresList;
        
        if (sucursalIdFiltro != null && sucursalIdFiltro > 0) {
            // Filtrar por sucursal
            trabajadoresList = trabajadorRepository.obtenerTrabajadoresPorEmpresaYSucursal(empresaId, sucursalIdFiltro);
        } else {
            // Todas las sucursales
            trabajadoresList = trabajadorRepository.obtenerTrabajadoresPorEmpresa(empresaId);
        }
        
        trabajadores.postValue(trabajadoresList != null ? trabajadoresList : new ArrayList<>());
        
        // Datos cargados exitosamente
        state.postValue(new BaseState(BaseState.STATE_SUCCESS, "Trabajadores cargados"));
        isLoading.postValue(false);
    }
    
    /**
     * Filtra trabajadores por sucursal
     * @param sucursalId ID de la sucursal (null para todas)
     */
    public void filtrarPorSucursal(Integer sucursalId) {
        this.sucursalIdFiltro = sucursalId;
        isLoading.postValue(true);
        executor.execute(() -> {
            cargarTrabajadoresFiltrados();
        });
    }
    
    /**
     * Recarga los trabajadores
     */
    public void recargarTrabajadores() {
        cargarDatos();
    }
    
    /**
     * Elimina un trabajador
     * @param trabajadorId ID del trabajador a eliminar
     */
    public void eliminarTrabajador(int trabajadorId) {
        isLoading.postValue(true);
        
        executor.execute(() -> {
            try {
                com.bizly.app.domain.usecase.trabajadores.EliminarTrabajadorUseCase eliminarUseCase = 
                    new com.bizly.app.domain.usecase.trabajadores.EliminarTrabajadorUseCase(getApplication());
                
                boolean eliminado = eliminarUseCase.ejecutar(trabajadorId);
                
                if (eliminado) {
                    // Recargar trabajadores después de eliminar
                    cargarTrabajadoresFiltrados();
                    state.postValue(new BaseState(BaseState.STATE_SUCCESS, "Trabajador eliminado"));
                } else {
                    errorMessage.postValue("Error al eliminar trabajador");
                    state.postValue(new BaseState(BaseState.STATE_ERROR, "Error al eliminar"));
                }
                isLoading.postValue(false);
                
            } catch (com.bizly.app.core.exception.AppException e) {
                errorMessage.postValue(e.getMessage());
                state.postValue(new BaseState(BaseState.STATE_ERROR, e.getMessage()));
                isLoading.postValue(false);
            } catch (Exception e) {
                errorMessage.postValue("Error inesperado al eliminar trabajador");
                state.postValue(new BaseState(BaseState.STATE_ERROR, "Error inesperado"));
                isLoading.postValue(false);
            }
        });
    }
    
    public int getEmpresaId() {
        return empresaId;
    }
    
    public Integer getSucursalIdFiltro() {
        return sucursalIdFiltro;
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
    
    public MutableLiveData<List<Trabajador>> getTrabajadores() {
        return trabajadores;
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

