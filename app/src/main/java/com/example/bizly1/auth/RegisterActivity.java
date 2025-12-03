package com.example.bizly1.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bizly1.R;
import com.example.bizly1.data.network.ApiClient;
import com.example.bizly1.data.network.ApiService;
import com.example.bizly1.data.utils.AuthManager;
import com.example.bizly1.models.LoginResponse;
import com.example.bizly1.models.RegisterRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nombreEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private TextInputLayout nombreInputLayout;
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout confirmPasswordInputLayout;
    private MaterialButton registerButton;
    private TextView backToLoginButton;
    private ProgressBar progressBar;
    
    private ApiService apiService;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Inicializar ApiClient
        ApiClient.init(this);
        apiService = ApiClient.getApiService();
        authManager = AuthManager.getInstance(this);
        
        initViews();
        setupListeners();
    }
    
    private void initViews() {
        nombreEditText = findViewById(R.id.nombreEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        nombreInputLayout = findViewById(R.id.nombreInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        confirmPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);
        registerButton = findViewById(R.id.registerButton);
        backToLoginButton = findViewById(R.id.backToLoginButton);
        progressBar = findViewById(R.id.progressBar);
        
        // Botón de atrás del header
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }
    
    private void setupListeners() {
        registerButton.setOnClickListener(v -> performRegister());
        backToLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
    
    private void performRegister() {
        String nombre = nombreEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        
        // Validar campos
        if (!validateFields(nombre, email, password, confirmPassword)) {
            return;
        }
        
        // Mostrar progress bar
        setLoading(true);
        
        // Crear request
        RegisterRequest registerRequest = new RegisterRequest(nombre, email, password);
        
        // Realizar petición
        Call<LoginResponse> call = apiService.registroEmprendedor(registerRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                setLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    String token = loginResponse.getToken();
                    
                    if (token != null && !token.isEmpty()) {
                        // Guardar token y datos del usuario
                        authManager.saveToken(token);
                        
                        if (loginResponse.getUsuario() != null) {
                            authManager.saveUserInfo(
                                loginResponse.getUsuario().getId(),
                                loginResponse.getUsuario().getNombre(),
                                loginResponse.getUsuario().getEmail(),
                                loginResponse.getUsuario().getRol(),
                                loginResponse.getUsuario().getEmpresaId()
                            );
                        }
                        
                        Toast.makeText(RegisterActivity.this, 
                            "Registro exitoso", Toast.LENGTH_SHORT).show();
                        
                        // Navegar a configuración del emprendimiento
                        Intent intent = new Intent(RegisterActivity.this, 
                            ConfiguracionEmprendimientoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, 
                            "Error: No se recibió token", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Error al registrar usuario";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(RegisterActivity.this, 
                    "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private boolean validateFields(String nombre, String email, String password, String confirmPassword) {
        boolean isValid = true;
        
        // Validar nombre
        if (TextUtils.isEmpty(nombre)) {
            nombreInputLayout.setError("El nombre es requerido");
            isValid = false;
        } else if (nombre.length() < 3) {
            nombreInputLayout.setError("El nombre debe tener al menos 3 caracteres");
            isValid = false;
        } else {
            nombreInputLayout.setError(null);
        }
        
        // Validar email
        if (TextUtils.isEmpty(email)) {
            emailInputLayout.setError("El correo es requerido");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Correo inválido");
            isValid = false;
        } else {
            emailInputLayout.setError(null);
        }
        
        // Validar contraseña
        if (TextUtils.isEmpty(password)) {
            passwordInputLayout.setError("La contraseña es requerida");
            isValid = false;
        } else if (password.length() < 6) {
            passwordInputLayout.setError("La contraseña debe tener al menos 6 caracteres");
            isValid = false;
        } else {
            passwordInputLayout.setError(null);
        }
        
        // Validar confirmación de contraseña
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordInputLayout.setError("Confirma tu contraseña");
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordInputLayout.setError("Las contraseñas no coinciden");
            isValid = false;
        } else {
            confirmPasswordInputLayout.setError(null);
        }
        
        return isValid;
    }
    
    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        registerButton.setEnabled(!loading);
        backToLoginButton.setEnabled(!loading);
        nombreEditText.setEnabled(!loading);
        emailEditText.setEnabled(!loading);
        passwordEditText.setEnabled(!loading);
        confirmPasswordEditText.setEnabled(!loading);
    }
}

