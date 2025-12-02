package com.bizly.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bizly.app.core.database.DatabaseSeeder;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.presentation.auth.LoginState;
import com.bizly.app.presentation.auth.LoginViewModel;
import com.bizly.app.presentation.base.BaseState;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Activity principal de la aplicación Bizly - Pantalla de Login
 * RF-02, RF-49
 */
public class MainActivity extends AppCompatActivity {
    
    private LoginViewModel viewModel;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private MaterialButton loginButton;
    private MaterialButton registerButton;
    private CircularProgressIndicator progressIndicator;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Poblar base de datos con datos de prueba (solo en desarrollo)
        DatabaseSeeder seeder = new DatabaseSeeder(this);
        seeder.seedDatabase();
        
        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        
        // Inicializar vistas
        initViews();
        
        // Configurar listeners
        setupListeners();
        
        // Observar cambios en el ViewModel
        observeViewModel();
    }
    
    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        
        // Progress indicator (lo agregaremos al layout si no existe)
        progressIndicator = findViewById(R.id.progressIndicator);
    }
    
    private void setupListeners() {
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText() != null ? emailEditText.getText().toString() : "";
            String password = passwordEditText.getText() != null ? passwordEditText.getText().toString() : "";
            
            // Limpiar errores previos
            clearErrors();
            
            // Validar campos
            if (TextUtils.isEmpty(email)) {
                emailInputLayout.setError("El email es requerido");
                return;
            }
            
            if (TextUtils.isEmpty(password)) {
                passwordInputLayout.setError("La contraseña es requerida");
                return;
            }
            
            // Iniciar sesión
            viewModel.iniciarSesion(email, password);
        });
        
        registerButton.setOnClickListener(v -> {
            // Navegar a RegisterActivity
            Intent intent = new Intent(this, com.bizly.app.presentation.auth.RegisterActivity.class);
            startActivity(intent);
        });
    }
    
    private void observeViewModel() {
        // Observar estado de carga
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                loginButton.setEnabled(!isLoading);
                registerButton.setEnabled(!isLoading);
                if (progressIndicator != null) {
                    progressIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                }
            }
        });
        
        // Observar errores
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                
                // Mostrar errores en los campos si es apropiado
                if (errorMessage.contains("email") || errorMessage.contains("Email")) {
                    emailInputLayout.setError(errorMessage);
                } else if (errorMessage.contains("contraseña") || errorMessage.contains("Contraseña") || 
                           errorMessage.contains("password") || errorMessage.contains("Password")) {
                    passwordInputLayout.setError(errorMessage);
                }
            }
        });
        
        // Observar estado de login
        viewModel.getLoginState().observe(this, state -> {
            if (state != null) {
                if (state.isLoginSuccess()) {
                    // Login exitoso
                    handleLoginSuccess();
                } else if (state.isError()) {
                    // Error ya manejado por errorMessage
                    clearErrors();
                }
            }
        });
        
        // Observar usuario autenticado
        viewModel.getUsuarioAutenticado().observe(this, usuario -> {
            if (usuario != null) {
                handleLoginSuccess(usuario);
            }
        });
    }
    
    private void handleLoginSuccess() {
        Usuario usuario = viewModel.getUsuarioAutenticado().getValue();
        if (usuario != null) {
            handleLoginSuccess(usuario);
        }
    }
    
    private void handleLoginSuccess(Usuario usuario) {
        Toast.makeText(this, "¡Bienvenido " + usuario.getNombre() + "!", Toast.LENGTH_SHORT).show();
        // Navegar a DashboardActivity
        Intent intent = new Intent(this, com.bizly.app.presentation.dashboard.DashboardActivity.class);
        intent.putExtra("usuario_id", usuario.getId());
        startActivity(intent);
        finish();
    }
    
    private void clearErrors() {
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);
    }
}

