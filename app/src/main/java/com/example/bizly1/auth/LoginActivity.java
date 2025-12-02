package com.example.bizly1.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bizly1.MainActivity;
import com.example.bizly1.R;
import com.example.bizly1.data.network.ApiClient;
import com.example.bizly1.data.network.ApiService;
import com.example.bizly1.data.utils.AuthManager;
import com.example.bizly1.models.LoginRequest;
import com.example.bizly1.models.LoginResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private MaterialButton loginButton;
    private MaterialButton registerButton;
    private ProgressBar progressBar;
    
    private ApiService apiService;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Inicializar ApiClient
        ApiClient.init(this);
        apiService = ApiClient.getApiService();
        authManager = AuthManager.getInstance(this);
        
        // Verificar si ya está logueado
        if (authManager.isLoggedIn()) {
            navigateToMain();
            return;
        }
        
        initViews();
        setupListeners();
    }
    
    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void setupListeners() {
        loginButton.setOnClickListener(v -> performLogin());
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    
    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        
        // Validar campos
        if (!validateFields(email, password)) {
            return;
        }
        
        // Mostrar progress bar
        setLoading(true);
        
        // Crear request
        LoginRequest loginRequest = new LoginRequest(email, password);
        
        // Realizar petición
        Call<LoginResponse> call = apiService.login(loginRequest);
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
                        
                        Toast.makeText(LoginActivity.this, 
                            "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        
                        // Navegar al MainActivity
                        navigateToMain();
                    } else {
                        Toast.makeText(LoginActivity.this, 
                            "Error: No se recibió token", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Error al iniciar sesión";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(LoginActivity.this, 
                    "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private boolean validateFields(String email, String password) {
        boolean isValid = true;
        
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
        
        return isValid;
    }
    
    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!loading);
        registerButton.setEnabled(!loading);
        emailEditText.setEnabled(!loading);
        passwordEditText.setEnabled(!loading);
    }
    
    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

