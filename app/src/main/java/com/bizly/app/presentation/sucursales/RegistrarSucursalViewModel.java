package com.bizly.app.presentation.sucursales;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.domain.model.Sucursal;
import com.bizly.app.domain.usecase.sucursales.RegistrarSucursalUseCase;
import com.bizly.app.presentation.base.BaseState;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para registrar una nueva sucursal
 */
public class RegistrarSucursalViewModel extends AndroidViewModel {
    
    private final RegistrarSucursalUseCase registrarSucursalUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    // LiveData para el estado
    private MutableLiveData<BaseState> state = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Sucursal> sucursalRegistrada = new MutableLiveData<>();
    
    public RegistrarSucursalViewModel(Application application) {
        super(application);
        this.registrarSucursalUseCase = new RegistrarSucursalUseCase(application);
        state.setValue(new BaseState(BaseState.STATE_IDLE));
        isLoading.setValue(false);
    }
    
    /**
     * Registra una nueva sucursal
     */
    public void registrarSucursal(int empresaId, String nombre, String direccion, String ciudad,
                                   String departamento, String telefono, double latitud, double longitud) {
        // Validar campos
        if (nombre == null || nombre.trim().isEmpty()) {
            errorMessage.postValue("El nombre de la sucursal es requerido");
            return;
        }
        
        if (direccion == null || direccion.trim().isEmpty()) {
            errorMessage.postValue("La dirección es requerida");
            return;
        }
        
        if (ciudad == null || ciudad.trim().isEmpty()) {
            errorMessage.postValue("La ciudad es requerida");
            return;
        }
        
        // Mostrar loading
        isLoading.postValue(true);
        state.postValue(new BaseState(BaseState.STATE_LOADING));
        
        // Ejecutar caso de uso en background
        executor.execute(() -> {
            try {
                // Crear objeto Sucursal
                Sucursal sucursal = new Sucursal();
                sucursal.setEmpresaId(empresaId);
                sucursal.setNombre(nombre.trim());
                sucursal.setDireccion(direccion.trim());
                sucursal.setCiudad(ciudad.trim());
                sucursal.setDepartamento(departamento != null ? departamento.trim() : null);
                sucursal.setTelefono(telefono != null ? telefono.trim() : null);
                sucursal.setLatitud(latitud);
                sucursal.setLongitud(longitud);
                sucursal.setCreatedAt(new Date());
                sucursal.setUpdatedAt(new Date());
                
                Sucursal sucursalCreada = registrarSucursalUseCase.ejecutar(sucursal);
                
                // Registro exitoso
                sucursalRegistrada.postValue(sucursalCreada);
                state.postValue(new BaseState(BaseState.STATE_SUCCESS, "Sucursal registrada exitosamente"));
                isLoading.postValue(false);
                
            } catch (AppException e) {
                // Error de validación
                errorMessage.postValue(e.getMessage());
                state.postValue(new BaseState(BaseState.STATE_ERROR, e.getMessage()));
                isLoading.postValue(false);
            } catch (Exception e) {
                // Error inesperado
                errorMessage.postValue("Error al registrar sucursal. Intente nuevamente.");
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
    
    public MutableLiveData<Sucursal> getSucursalRegistrada() {
        return sucursalRegistrada;
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}


