package com.bizly.app.presentation.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bizly.app.MainActivity;
import com.bizly.app.R;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.presentation.base.BaseState;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Activity para registro de nuevos usuarios emprendedores (dos pasos)
 * Paso 1: Datos de la empresa
 * Paso 2: Datos del usuario
 * RF-01, RF-03
 */
public class RegisterActivity extends AppCompatActivity {
    
    private RegisterViewModel viewModel;
    
    // Vistas comunes
    private TextView stepIndicatorTextView;
    private TextView subtitleTextView;
    private MaterialButton backToLoginButton;
    
    // Paso 1: Empresa
    private View step1Layout;
    private TextInputEditText empresaNombreEditText;
    private TextInputEditText empresaRubroEditText;
    private TextInputEditText empresaDescripcionEditText;
    private TextInputEditText empresaMargenEditText;
    private TextInputLayout empresaNombreInputLayout;
    private TextInputLayout empresaRubroInputLayout;
    private TextInputLayout empresaDescripcionInputLayout;
    private TextInputLayout empresaMargenInputLayout;
    private ImageView logoImageView;
    private MaterialButton selectLogoButton;
    private MaterialButton continueToStep2Button;
    private CircularProgressIndicator progressIndicator1;
    private String logoPath = null;
    private Uri selectedImageUri = null;
    
    // ActivityResultLauncher para seleccionar imagen
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    
    // Paso 2: Usuario
    private View step2Layout;
    private TextInputEditText usuarioNombreEditText;
    private TextInputEditText usuarioEmailEditText;
    private TextInputEditText usuarioPasswordEditText;
    private TextInputEditText usuarioConfirmPasswordEditText;
    private TextInputLayout usuarioNombreInputLayout;
    private TextInputLayout usuarioEmailInputLayout;
    private TextInputLayout usuarioPasswordInputLayout;
    private TextInputLayout usuarioConfirmPasswordInputLayout;
    private MaterialButton registerButton;
    private CircularProgressIndicator progressIndicator2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        
        // Configurar ActivityResultLauncher para seleccionar imagen
        setupImagePicker();
        
        // Inicializar vistas
        initViews();
        
        // Configurar listeners
        setupListeners();
        
        // Observar cambios en el ViewModel
        observeViewModel();
    }
    
    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    if (imageUri != null) {
                        selectedImageUri = imageUri;
                        // Cargar imagen en el ImageView
                        logoImageView.setImageURI(imageUri);
                        // Guardar la URI como string para pasarla al ViewModel
                        logoPath = imageUri.toString();
                    }
                }
            }
        );
    }
    
    private void initViews() {
        // Vistas comunes
        stepIndicatorTextView = findViewById(R.id.stepIndicatorTextView);
        subtitleTextView = findViewById(R.id.subtitleTextView);
        backToLoginButton = findViewById(R.id.backToLoginButton);
        
        // Paso 1: Empresa
        step1Layout = findViewById(R.id.step1Layout);
        empresaNombreEditText = findViewById(R.id.empresaNombreEditText);
        empresaRubroEditText = findViewById(R.id.empresaRubroEditText);
        empresaDescripcionEditText = findViewById(R.id.empresaDescripcionEditText);
        empresaMargenEditText = findViewById(R.id.empresaMargenEditText);
        empresaNombreInputLayout = findViewById(R.id.empresaNombreInputLayout);
        empresaRubroInputLayout = findViewById(R.id.empresaRubroInputLayout);
        empresaDescripcionInputLayout = findViewById(R.id.empresaDescripcionInputLayout);
        empresaMargenInputLayout = findViewById(R.id.empresaMargenInputLayout);
        logoImageView = findViewById(R.id.logoImageView);
        selectLogoButton = findViewById(R.id.selectLogoButton);
        continueToStep2Button = findViewById(R.id.continueToStep2Button);
        progressIndicator1 = findViewById(R.id.progressIndicator1);
        
        // Paso 2: Usuario
        step2Layout = findViewById(R.id.step2Layout);
        usuarioNombreEditText = findViewById(R.id.usuarioNombreEditText);
        usuarioEmailEditText = findViewById(R.id.usuarioEmailEditText);
        usuarioPasswordEditText = findViewById(R.id.usuarioPasswordEditText);
        usuarioConfirmPasswordEditText = findViewById(R.id.usuarioConfirmPasswordEditText);
        usuarioNombreInputLayout = findViewById(R.id.usuarioNombreInputLayout);
        usuarioEmailInputLayout = findViewById(R.id.usuarioEmailInputLayout);
        usuarioPasswordInputLayout = findViewById(R.id.usuarioPasswordInputLayout);
        usuarioConfirmPasswordInputLayout = findViewById(R.id.usuarioConfirmPasswordInputLayout);
        registerButton = findViewById(R.id.registerButton);
        progressIndicator2 = findViewById(R.id.progressIndicator2);
    }
    
    private void setupListeners() {
        // Botón continuar al paso 2
        continueToStep2Button.setOnClickListener(v -> {
            String nombre = empresaNombreEditText.getText() != null ? empresaNombreEditText.getText().toString() : "";
            String rubro = empresaRubroEditText.getText() != null ? empresaRubroEditText.getText().toString() : "";
            String descripcion = empresaDescripcionEditText.getText() != null ? empresaDescripcionEditText.getText().toString() : "";
            String margenStr = empresaMargenEditText.getText() != null ? empresaMargenEditText.getText().toString() : "";
            
            // Limpiar errores previos
            clearStep1Errors();
            
            // Validar campos
            if (TextUtils.isEmpty(nombre)) {
                empresaNombreInputLayout.setError("El nombre de la empresa es requerido");
                return;
            }
            
            if (TextUtils.isEmpty(rubro)) {
                empresaRubroInputLayout.setError("El rubro es requerido");
                return;
            }
            
            double margenGanancia = 0.0;
            if (!TextUtils.isEmpty(margenStr)) {
                try {
                    margenGanancia = Double.parseDouble(margenStr);
                    if (margenGanancia < 0) {
                        empresaMargenInputLayout.setError("El margen de ganancia debe ser positivo");
                        return;
                    }
                } catch (NumberFormatException e) {
                    empresaMargenInputLayout.setError("Ingrese un valor numérico válido");
                    return;
                }
            }
            
            // Registrar empresa y avanzar al paso 2
            viewModel.registrarEmpresa(nombre, rubro, descripcion, margenGanancia, logoPath);
        });
        
        // Botón registrar usuario (paso 2)
        registerButton.setOnClickListener(v -> {
            String nombre = usuarioNombreEditText.getText() != null ? usuarioNombreEditText.getText().toString() : "";
            String email = usuarioEmailEditText.getText() != null ? usuarioEmailEditText.getText().toString() : "";
            String password = usuarioPasswordEditText.getText() != null ? usuarioPasswordEditText.getText().toString() : "";
            String confirmPassword = usuarioConfirmPasswordEditText.getText() != null ? usuarioConfirmPasswordEditText.getText().toString() : "";
            
            // Limpiar errores previos
            clearStep2Errors();
            
            // Validar campos
            if (TextUtils.isEmpty(nombre)) {
                usuarioNombreInputLayout.setError("El nombre es requerido");
                return;
            }
            
            if (TextUtils.isEmpty(email)) {
                usuarioEmailInputLayout.setError("El email es requerido");
                return;
            }
            
            if (TextUtils.isEmpty(password)) {
                usuarioPasswordInputLayout.setError("La contraseña es requerida");
                return;
            }
            
            if (TextUtils.isEmpty(confirmPassword)) {
                usuarioConfirmPasswordInputLayout.setError("La confirmación de contraseña es requerida");
                return;
            }
            
            // Registrar usuario
            viewModel.registrarUsuario(nombre, email, password, confirmPassword);
        });
        
        // Selector de logo
        selectLogoButton.setOnClickListener(v -> {
            openImagePicker();
        });
        
        // Botón volver a login
        backToLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
    
    private void observeViewModel() {
        // Observar paso actual
        viewModel.getCurrentStep().observe(this, step -> {
            if (step != null) {
                updateStepDisplay(step);
            }
        });
        
        // Observar estado de carga
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                Integer currentStep = viewModel.getCurrentStep().getValue();
                if (currentStep != null && currentStep == 1) {
                    // Paso 1: Empresa
                    continueToStep2Button.setEnabled(!isLoading);
                    if (progressIndicator1 != null) {
                        progressIndicator1.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                    }
                } else if (currentStep != null && currentStep == 2) {
                    // Paso 2: Usuario
                    registerButton.setEnabled(!isLoading);
                    if (progressIndicator2 != null) {
                        progressIndicator2.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                    }
                }
            }
        });
        
        // Observar errores
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                
                // Mostrar errores en los campos apropiados según el paso
                Integer currentStep = viewModel.getCurrentStep().getValue();
                if (currentStep != null && currentStep == 1) {
                    // Errores del paso 1
                    if (errorMessage.contains("nombre") || errorMessage.contains("Nombre")) {
                        empresaNombreInputLayout.setError(errorMessage);
                    } else if (errorMessage.contains("rubro") || errorMessage.contains("Rubro")) {
                        empresaRubroInputLayout.setError(errorMessage);
                    } else if (errorMessage.contains("margen") || errorMessage.contains("Margen")) {
                        empresaMargenInputLayout.setError(errorMessage);
                    }
                } else if (currentStep != null && currentStep == 2) {
                    // Errores del paso 2
                    if (errorMessage.contains("nombre") || errorMessage.contains("Nombre")) {
                        usuarioNombreInputLayout.setError(errorMessage);
                    } else if (errorMessage.contains("email") || errorMessage.contains("Email")) {
                        usuarioEmailInputLayout.setError(errorMessage);
                    } else if (errorMessage.contains("contraseña") || errorMessage.contains("Contraseña") || 
                               errorMessage.contains("password") || errorMessage.contains("Password") ||
                               errorMessage.contains("coinciden")) {
                        if (errorMessage.contains("coinciden")) {
                            usuarioConfirmPasswordInputLayout.setError(errorMessage);
                        } else {
                            usuarioPasswordInputLayout.setError(errorMessage);
                        }
                    }
                }
            }
        });
        
        // Observar estado de registro
        viewModel.getRegisterState().observe(this, state -> {
            if (state != null) {
                if (state.isRegisterSuccess()) {
                    // Registro completo exitoso
                    handleRegisterSuccess();
                } else if (state.isError()) {
                    // Error ya manejado por errorMessage
                    Integer currentStep = viewModel.getCurrentStep().getValue();
                    if (currentStep != null && currentStep == 1) {
                        clearStep1Errors();
                    } else {
                        clearStep2Errors();
                    }
                } else if (state.isSuccess() && state.getCurrentState() == BaseState.STATE_SUCCESS) {
                    // Empresa registrada exitosamente, avanzar al paso 2
                    // Ya se maneja en getCurrentStep()
                }
            }
        });
        
        // Observar usuario registrado
        viewModel.getUsuarioRegistrado().observe(this, usuario -> {
            if (usuario != null) {
                handleRegisterSuccess(usuario);
            }
        });
    }
    
    private void updateStepDisplay(int step) {
        if (step == 1) {
            // Mostrar paso 1
            step1Layout.setVisibility(View.VISIBLE);
            step2Layout.setVisibility(View.GONE);
            stepIndicatorTextView.setText("Paso 1 de 2");
            subtitleTextView.setText("Datos de tu emprendimiento");
        } else if (step == 2) {
            // Mostrar paso 2
            step1Layout.setVisibility(View.GONE);
            step2Layout.setVisibility(View.VISIBLE);
            stepIndicatorTextView.setText("Paso 2 de 2");
            subtitleTextView.setText("Datos de tu cuenta");
        }
    }
    
    private void handleRegisterSuccess() {
        Usuario usuario = viewModel.getUsuarioRegistrado().getValue();
        if (usuario != null) {
            handleRegisterSuccess(usuario);
        }
    }
    
    private void handleRegisterSuccess(Usuario usuario) {
        Toast.makeText(this, "¡Cuenta creada exitosamente! Bienvenido " + usuario.getNombre(), Toast.LENGTH_LONG).show();
        
        // Volver a la pantalla de login después de un breve delay
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("registered_email", usuario.getEmail());
            startActivity(intent);
            finish();
        }, 1500);
    }
    
    private void clearStep1Errors() {
        empresaNombreInputLayout.setError(null);
        empresaRubroInputLayout.setError(null);
        empresaDescripcionInputLayout.setError(null);
        empresaMargenInputLayout.setError(null);
    }
    
    private void clearStep2Errors() {
        usuarioNombreInputLayout.setError(null);
        usuarioEmailInputLayout.setError(null);
        usuarioPasswordInputLayout.setError(null);
        usuarioConfirmPasswordInputLayout.setError(null);
    }
    
    /**
     * Abre el selector de imágenes de la galería
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
}
