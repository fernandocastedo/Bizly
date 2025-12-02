package com.bizly.app.presentation.auth;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.domain.usecase.auth.IniciarSesionUseCase;
import com.bizly.app.presentation.base.BaseState;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para la pantalla de login
 * RF-02, RF-49
 */
public class LoginViewModel extends AndroidViewModel {
    
    private IniciarSesionUseCase iniciarSesionUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    // LiveData para el estado
    private MutableLiveData<LoginState> loginState = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Usuario> usuarioAutenticado = new MutableLiveData<>();
    
    public LoginViewModel(Application application) {
        super(application);
        this.iniciarSesionUseCase = new IniciarSesionUseCase(application);
        loginState.setValue(new LoginState(BaseState.STATE_IDLE));
        isLoading.setValue(false);
    }
    
    /**
     * Inicia sesión con email y contraseña
     * @param email Email del usuario
     * @param password Contraseña
     */
    public void iniciarSesion(String email, String password) {
        // Verificar si ya está en proceso de carga
        Boolean isCurrentlyLoading = isLoading.getValue();
        if (isCurrentlyLoading != null && isCurrentlyLoading) {
            return; // Ya está procesando, ignorar llamada
        }
        
        // Limpiar estados previos
        errorMessage.postValue("");
        usuarioAutenticado.postValue(null);
        
        // Validar campos vacíos ANTES de establecer isLoading
        if (email == null || email.trim().isEmpty()) {
            errorMessage.postValue("El email es requerido");
            return; // Retornar sin establecer isLoading
        }
        
        if (password == null || password.isEmpty()) {
            errorMessage.postValue("La contraseña es requerida");
            return; // Retornar sin establecer isLoading
        }
        
        // Solo establecer loading si pasó las validaciones
        isLoading.postValue(true);
        loginState.postValue(new LoginState(BaseState.STATE_LOADING));
        
        // Ejecutar caso de uso en background
        executor.execute(() -> {
            try {
                Usuario usuario = iniciarSesionUseCase.ejecutar(email.trim(), password);
                
                // Login exitoso
                usuarioAutenticado.postValue(usuario);
                loginState.postValue(new LoginState(LoginState.STATE_LOGIN_SUCCESS, "Login exitoso"));
                isLoading.postValue(false);
                
            } catch (AppException e) {
                // Error de autenticación
                errorMessage.postValue(e.getMessage());
                loginState.postValue(new LoginState(BaseState.STATE_ERROR, e.getMessage()));
                isLoading.postValue(false);
            } catch (Exception e) {
                // Error inesperado
                errorMessage.postValue("Error al iniciar sesión. Intente nuevamente.");
                loginState.postValue(new LoginState(BaseState.STATE_ERROR, "Error inesperado"));
                isLoading.postValue(false);
            }
        });
    }
    
    // Getters para LiveData
    public MutableLiveData<LoginState> getLoginState() {
        return loginState;
    }
    
    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public MutableLiveData<Usuario> getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}

