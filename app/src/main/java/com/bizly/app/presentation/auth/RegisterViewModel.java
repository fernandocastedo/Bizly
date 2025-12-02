package com.bizly.app.presentation.auth;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.bizly.app.core.exception.AppException;
import com.bizly.app.domain.model.Empresa;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.domain.usecase.auth.RegistrarUsuarioUseCase;
import com.bizly.app.domain.usecase.emprendimiento.RegistrarEmprendimientoUseCase;
import com.bizly.app.presentation.base.BaseState;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para la pantalla de registro (dos pasos: empresa y usuario)
 * RF-01, RF-03
 */
public class RegisterViewModel extends AndroidViewModel {
    
    private final RegistrarEmprendimientoUseCase registrarEmprendimientoUseCase;
    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    
    // Datos temporales del paso 1 (empresa)
    private Empresa empresaTemporal;
    
    // LiveData para el estado
    private MutableLiveData<RegisterState> registerState = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Usuario> usuarioRegistrado = new MutableLiveData<>();
    private MutableLiveData<Integer> currentStep = new MutableLiveData<>(); // 1 = empresa, 2 = usuario
    
    public RegisterViewModel(Application application) {
        super(application);
        this.registrarEmprendimientoUseCase = new RegistrarEmprendimientoUseCase(application);
        this.registrarUsuarioUseCase = new RegistrarUsuarioUseCase(application);
        registerState.setValue(new RegisterState(BaseState.STATE_IDLE));
        isLoading.setValue(false);
        currentStep.setValue(1); // Empezar en paso 1
    }
    
    /**
     * Paso 1: Registra la empresa
     * @param nombre Nombre de la empresa
     * @param rubro Rubro de la empresa
     * @param descripcion Descripción de la empresa
     * @param margenGanancia Margen de ganancia
     * @param logoUrl URL o path del logo (puede ser null)
     */
    public void registrarEmpresa(String nombre, String rubro, String descripcion, 
                                  double margenGanancia, String logoUrl) {
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
        registerState.postValue(new RegisterState(BaseState.STATE_LOADING));
        
        // Ejecutar caso de uso en background
        executor.execute(() -> {
            try {
                // Crear objeto Empresa
                Empresa empresa = new Empresa();
                empresa.setNombre(nombre.trim());
                empresa.setRubro(rubro.trim());
                empresa.setDescripcion(descripcion != null ? descripcion.trim() : "");
                empresa.setMargenGanancia(margenGanancia);
                empresa.setLogoUrl(logoUrl);
                
                Empresa empresaCreada = registrarEmprendimientoUseCase.ejecutar(empresa);
                
                // Guardar empresa temporal para el paso 2
                this.empresaTemporal = empresaCreada;
                
                // Avanzar al paso 2
                currentStep.postValue(2);
                registerState.postValue(new RegisterState(BaseState.STATE_SUCCESS, "Empresa registrada"));
                isLoading.postValue(false);
                
            } catch (AppException e) {
                // Error de validación
                errorMessage.postValue(e.getMessage());
                registerState.postValue(new RegisterState(BaseState.STATE_ERROR, e.getMessage()));
                isLoading.postValue(false);
            } catch (Exception e) {
                // Error inesperado
                errorMessage.postValue("Error al registrar empresa. Intente nuevamente.");
                registerState.postValue(new RegisterState(BaseState.STATE_ERROR, "Error inesperado"));
                isLoading.postValue(false);
            }
        });
    }
    
    /**
     * Paso 2: Registra el usuario asociado a la empresa creada
     * @param nombre Nombre del usuario
     * @param email Email del usuario
     * @param password Contraseña
     * @param confirmPassword Confirmación de contraseña
     */
    public void registrarUsuario(String nombre, String email, String password, String confirmPassword) {
        // Validar que la empresa ya fue creada
        if (empresaTemporal == null) {
            errorMessage.postValue("Debe completar primero el registro de la empresa");
            return;
        }
        
        // Validar campos vacíos
        if (nombre == null || nombre.trim().isEmpty()) {
            errorMessage.postValue("El nombre es requerido");
            return;
        }
        
        if (email == null || email.trim().isEmpty()) {
            errorMessage.postValue("El email es requerido");
            return;
        }
        
        if (password == null || password.isEmpty()) {
            errorMessage.postValue("La contraseña es requerida");
            return;
        }
        
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            errorMessage.postValue("La confirmación de contraseña es requerida");
            return;
        }
        
        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            errorMessage.postValue("Las contraseñas no coinciden");
            return;
        }
        
        // Mostrar loading
        isLoading.postValue(true);
        registerState.postValue(new RegisterState(BaseState.STATE_LOADING));
        
        // Ejecutar caso de uso en background
        executor.execute(() -> {
            try {
                // Crear objeto Usuario
                Usuario usuario = new Usuario();
                usuario.setNombre(nombre.trim());
                usuario.setEmail(email.trim());
                usuario.setPassword(password);
                usuario.setEmpresaId(empresaTemporal.getId());
                
                Usuario usuarioCreado = registrarUsuarioUseCase.ejecutar(usuario);
                
                // Registro completo exitoso
                usuarioRegistrado.postValue(usuarioCreado);
                registerState.postValue(new RegisterState(RegisterState.STATE_REGISTER_SUCCESS, "Registro exitoso"));
                isLoading.postValue(false);
                
            } catch (AppException e) {
                // Error de validación
                errorMessage.postValue(e.getMessage());
                registerState.postValue(new RegisterState(BaseState.STATE_ERROR, e.getMessage()));
                isLoading.postValue(false);
            } catch (Exception e) {
                // Error inesperado
                errorMessage.postValue("Error al registrar usuario. Intente nuevamente.");
                registerState.postValue(new RegisterState(BaseState.STATE_ERROR, "Error inesperado"));
                isLoading.postValue(false);
            }
        });
    }
    
    // Getters para LiveData
    public MutableLiveData<RegisterState> getRegisterState() {
        return registerState;
    }
    
    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public MutableLiveData<Usuario> getUsuarioRegistrado() {
        return usuarioRegistrado;
    }
    
    public MutableLiveData<Integer> getCurrentStep() {
        return currentStep;
    }
    
    public Empresa getEmpresaTemporal() {
        return empresaTemporal;
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
