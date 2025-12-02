package com.bizly.app.presentation.trabajadores;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.SucursalRepository;
import com.bizly.app.data.repository.TrabajadorRepository;
import com.bizly.app.data.repository.impl.SucursalRepositoryLocal;
import com.bizly.app.data.repository.impl.TrabajadorRepositoryLocal;
import com.bizly.app.domain.model.Sucursal;
import com.bizly.app.domain.model.Trabajador;
import com.bizly.app.domain.usecase.trabajadores.ActualizarTrabajadorUseCase;
import com.bizly.app.presentation.base.BaseState;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para editar un trabajador existente
 * RF-47
 */
public class EditarTrabajadorViewModel extends AndroidViewModel {
    
    private final ActualizarTrabajadorUseCase actualizarTrabajadorUseCase;
    private final TrabajadorRepository trabajadorRepository;
    private final SucursalRepository sucursalRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    // LiveData para el estado
    private MutableLiveData<BaseState> state = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Trabajador> trabajador = new MutableLiveData<>();
    private MutableLiveData<List<Sucursal>> sucursales = new MutableLiveData<>();
    private MutableLiveData<Boolean> trabajadorActualizado = new MutableLiveData<>();
    
    private int empresaId;
    private int trabajadorId;
    
    public EditarTrabajadorViewModel(Application application) {
        super(application);
        this.actualizarTrabajadorUseCase = new ActualizarTrabajadorUseCase(application);
        this.trabajadorRepository = new TrabajadorRepositoryLocal(application);
        this.sucursalRepository = new SucursalRepositoryLocal(application);
        state.setValue(new BaseState(BaseState.STATE_IDLE));
        isLoading.setValue(false);
        sucursales.setValue(new ArrayList<>());
    }
    
    /**
     * Carga el trabajador y las sucursales
     * @param trabajadorId ID del trabajador a editar
     * @param empresaId ID de la empresa
     */
    public void cargarDatos(int trabajadorId, int empresaId) {
        this.trabajadorId = trabajadorId;
        this.empresaId = empresaId;
        
        isLoading.postValue(true);
        state.postValue(new BaseState(BaseState.STATE_LOADING));
        
        executor.execute(() -> {
            try {
                // Cargar trabajador
                Trabajador trabajadorActual = trabajadorRepository.obtenerTrabajadorPorId(trabajadorId);
                if (trabajadorActual == null) {
                    errorMessage.postValue("Trabajador no encontrado");
                    state.postValue(new BaseState(BaseState.STATE_ERROR, "Trabajador no encontrado"));
                    isLoading.postValue(false);
                    return;
                }
                
                trabajador.postValue(trabajadorActual);
                
                // Cargar sucursales
                List<Sucursal> sucursalesList = sucursalRepository.obtenerSucursalesPorEmpresa(empresaId);
                sucursales.postValue(sucursalesList != null ? sucursalesList : new ArrayList<>());
                
                state.postValue(new BaseState(BaseState.STATE_SUCCESS, "Datos cargados"));
                isLoading.postValue(false);
                
            } catch (Exception e) {
                errorMessage.postValue("Error al cargar datos");
                state.postValue(new BaseState(BaseState.STATE_ERROR, "Error inesperado"));
                isLoading.postValue(false);
            }
        });
    }
    
    /**
     * Actualiza el trabajador
     */
    public void actualizarTrabajador(String nombre, String cargo, double sueldoMensual,
                                    String tipoGasto, Integer sucursalId) {
        // Validar campos
        if (nombre == null || nombre.trim().isEmpty()) {
            errorMessage.postValue("El nombre del trabajador es requerido");
            return;
        }
        
        if (cargo == null || cargo.trim().isEmpty()) {
            errorMessage.postValue("El cargo es requerido");
            return;
        }
        
        if (sueldoMensual < 0) {
            errorMessage.postValue("El sueldo mensual debe ser un valor positivo");
            return;
        }
        
        if (tipoGasto == null || (!tipoGasto.equals("fijo") && !tipoGasto.equals("variable"))) {
            errorMessage.postValue("El tipo de gasto debe ser 'fijo' o 'variable'");
            return;
        }
        
        if (sucursalId == null || sucursalId <= 0) {
            errorMessage.postValue("El trabajador debe pertenecer a una sucursal");
            return;
        }
        
        // Mostrar loading
        isLoading.postValue(true);
        state.postValue(new BaseState(BaseState.STATE_LOADING));
        
        // Ejecutar caso de uso en background
        executor.execute(() -> {
            try {
                // Obtener trabajador actual
                Trabajador trabajadorActual = trabajadorRepository.obtenerTrabajadorPorId(trabajadorId);
                if (trabajadorActual == null) {
                    errorMessage.postValue("Trabajador no encontrado");
                    state.postValue(new BaseState(BaseState.STATE_ERROR, "Trabajador no encontrado"));
                    isLoading.postValue(false);
                    return;
                }
                
                // Actualizar datos
                trabajadorActual.setNombre(nombre.trim());
                trabajadorActual.setCargo(cargo.trim());
                trabajadorActual.setSueldoMensual(sueldoMensual);
                trabajadorActual.setTipoGasto(tipoGasto);
                trabajadorActual.setSucursalId(sucursalId);
                trabajadorActual.setUpdatedAt(new Date());
                
                boolean actualizado = actualizarTrabajadorUseCase.ejecutar(trabajadorActual);
                
                if (actualizado) {
                    trabajadorActualizado.postValue(true);
                    state.postValue(new BaseState(BaseState.STATE_SUCCESS, "Trabajador actualizado exitosamente"));
                } else {
                    errorMessage.postValue("Error al actualizar trabajador");
                    state.postValue(new BaseState(BaseState.STATE_ERROR, "Error al actualizar"));
                }
                isLoading.postValue(false);
                
            } catch (AppException e) {
                errorMessage.postValue(e.getMessage());
                state.postValue(new BaseState(BaseState.STATE_ERROR, e.getMessage()));
                isLoading.postValue(false);
            } catch (Exception e) {
                errorMessage.postValue("Error al actualizar trabajador. Intente nuevamente.");
                state.postValue(new BaseState(BaseState.STATE_ERROR, "Error inesperado"));
                isLoading.postValue(false);
            }
        });
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
    
    public MutableLiveData<Trabajador> getTrabajador() {
        return trabajador;
    }
    
    public MutableLiveData<List<Sucursal>> getSucursales() {
        return sucursales;
    }
    
    public MutableLiveData<Boolean> getTrabajadorActualizado() {
        return trabajadorActualizado;
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}


