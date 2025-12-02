package com.bizly.app.presentation.trabajadores;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.data.repository.SucursalRepository;
import com.bizly.app.data.repository.impl.SucursalRepositoryLocal;
import com.bizly.app.domain.model.Sucursal;
import com.bizly.app.domain.model.Trabajador;
import com.bizly.app.domain.usecase.trabajadores.RegistrarTrabajadorUseCase;
import com.bizly.app.presentation.base.BaseState;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para crear un nuevo trabajador
 * RF-46
 */
public class CrearTrabajadorViewModel extends AndroidViewModel {
    
    private final RegistrarTrabajadorUseCase registrarTrabajadorUseCase;
    private final SucursalRepository sucursalRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    // LiveData para el estado
    private MutableLiveData<BaseState> state = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Trabajador> trabajadorRegistrado = new MutableLiveData<>();
    private MutableLiveData<List<Sucursal>> sucursales = new MutableLiveData<>();
    
    private int empresaId;
    
    public CrearTrabajadorViewModel(Application application) {
        super(application);
        this.registrarTrabajadorUseCase = new RegistrarTrabajadorUseCase(application);
        this.sucursalRepository = new SucursalRepositoryLocal(application);
        state.setValue(new BaseState(BaseState.STATE_IDLE));
        isLoading.setValue(false);
        sucursales.setValue(new ArrayList<>());
    }
    
    /**
     * Establece el ID de la empresa y carga las sucursales
     * @param empresaId ID de la empresa
     */
    public void cargarSucursales(int empresaId) {
        this.empresaId = empresaId;
        cargarSucursales();
    }
    
    /**
     * Carga las sucursales de la empresa
     */
    public void cargarSucursales() {
        executor.execute(() -> {
            try {
                List<Sucursal> sucursalesList = sucursalRepository.obtenerSucursalesPorEmpresa(empresaId);
                sucursales.postValue(sucursalesList != null ? sucursalesList : new ArrayList<>());
            } catch (Exception e) {
                errorMessage.postValue("Error al cargar sucursales");
            }
        });
    }
    
    /**
     * Registra un nuevo trabajador
     */
    public void registrarTrabajador(String nombre, String cargo, double sueldoMensual,
                                    String tipoGasto, Integer sucursalId) {
        // Verificar si ya está en proceso de carga
        Boolean isCurrentlyLoading = isLoading.getValue();
        if (isCurrentlyLoading != null && isCurrentlyLoading) {
            return; // Ya está procesando, ignorar llamada
        }
        
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
        
        // Validar que pertenezca a una sucursal (requerido)
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
                // Crear objeto Trabajador
                Trabajador trabajador = new Trabajador();
                trabajador.setEmpresaId(empresaId);
                trabajador.setSucursalId(sucursalId);
                trabajador.setNombre(nombre.trim());
                trabajador.setCargo(cargo.trim());
                trabajador.setSueldoMensual(sueldoMensual);
                trabajador.setTipoGasto(tipoGasto);
                trabajador.setCreatedAt(new Date());
                trabajador.setUpdatedAt(new Date());
                
                Trabajador trabajadorCreado = registrarTrabajadorUseCase.ejecutar(trabajador);
                
                // Registro exitoso
                trabajadorRegistrado.postValue(trabajadorCreado);
                state.postValue(new BaseState(BaseState.STATE_SUCCESS, "Trabajador registrado exitosamente"));
                isLoading.postValue(false);
                
            } catch (AppException e) {
                // Error de validación
                errorMessage.postValue(e.getMessage());
                state.postValue(new BaseState(BaseState.STATE_ERROR, e.getMessage()));
                isLoading.postValue(false);
            } catch (Exception e) {
                // Error inesperado
                errorMessage.postValue("Error al registrar trabajador. Intente nuevamente.");
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
    
    public MutableLiveData<Trabajador> getTrabajadorRegistrado() {
        return trabajadorRegistrado;
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

