package com.bizly.app.presentation.emprendimiento;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bizly.app.R;
import com.bizly.app.domain.model.Empresa;
import com.bizly.app.presentation.base.BaseState;
import com.bizly.app.presentation.dashboard.DashboardActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Activity para ver y editar datos del emprendimiento
 * RF-03, RF-04, RF-05, RF-06
 */
public class EmprendimientoActivity extends AppCompatActivity {
    
    private EmprendimientoViewModel viewModel;
    
    // Vistas
    private ImageView logoImageView;
    private MaterialButton selectLogoButton;
    private TextInputEditText nombreEditText;
    private TextInputEditText rubroEditText;
    private TextInputEditText descripcionEditText;
    private TextInputEditText margenEditText;
    private TextInputLayout nombreInputLayout;
    private TextInputLayout rubroInputLayout;
    private TextInputLayout descripcionInputLayout;
    private TextInputLayout margenInputLayout;
    private MaterialButton guardarButton;
    private CircularProgressIndicator progressIndicator;
    
    private int usuarioId;
    private String logoPath = null;
    private Uri selectedImageUri = null;
    
    // ActivityResultLauncher para seleccionar imagen
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emprendimiento);
        
        // Configurar insets
        androidx.coordinatorlayout.widget.CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
        if (coordinatorLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(coordinatorLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        
        // Obtener usuarioId del Intent
        usuarioId = getIntent().getIntExtra("usuario_id", -1);
        if (usuarioId <= 0) {
            Toast.makeText(this, "Sesión inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(EmprendimientoViewModel.class);
        
        // Configurar ActivityResultLauncher para seleccionar imagen
        setupImagePicker();
        
        // Inicializar vistas
        initViews();
        
        // Configurar toolbar
        setupToolbar();
        
        // Configurar listeners
        setupListeners();
        
        // Observar cambios en el ViewModel
        observeViewModel();
        
        // Cargar datos
        viewModel.cargarDatos(usuarioId);
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
                        // Guardar la URI como string
                        logoPath = imageUri.toString();
                    }
                }
            }
        );
    }
    
    private void initViews() {
        logoImageView = findViewById(R.id.logoImageView);
        selectLogoButton = findViewById(R.id.selectLogoButton);
        nombreEditText = findViewById(R.id.nombreEditText);
        rubroEditText = findViewById(R.id.rubroEditText);
        descripcionEditText = findViewById(R.id.descripcionEditText);
        margenEditText = findViewById(R.id.margenEditText);
        nombreInputLayout = findViewById(R.id.nombreInputLayout);
        rubroInputLayout = findViewById(R.id.rubroInputLayout);
        descripcionInputLayout = findViewById(R.id.descripcionInputLayout);
        margenInputLayout = findViewById(R.id.margenInputLayout);
        guardarButton = findViewById(R.id.guardarButton);
        progressIndicator = findViewById(R.id.progressIndicator);
    }
    
    private void setupToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupListeners() {
        // Botón seleccionar logo
        selectLogoButton.setOnClickListener(v -> {
            openImagePicker();
        });
        
        // Botón guardar
        guardarButton.setOnClickListener(v -> {
            String nombre = nombreEditText.getText() != null ? nombreEditText.getText().toString() : "";
            String rubro = rubroEditText.getText() != null ? rubroEditText.getText().toString() : "";
            String descripcion = descripcionEditText.getText() != null ? descripcionEditText.getText().toString() : "";
            String margenStr = margenEditText.getText() != null ? margenEditText.getText().toString() : "";
            
            // Limpiar errores previos
            clearErrors();
            
            // Validar campos
            if (TextUtils.isEmpty(nombre)) {
                nombreInputLayout.setError("El nombre de la empresa es requerido");
                return;
            }
            
            if (TextUtils.isEmpty(rubro)) {
                rubroInputLayout.setError("El rubro es requerido");
                return;
            }
            
            double margenGanancia = 0.0;
            if (!TextUtils.isEmpty(margenStr)) {
                try {
                    margenGanancia = Double.parseDouble(margenStr);
                    if (margenGanancia < 0) {
                        margenInputLayout.setError("El margen de ganancia debe ser positivo");
                        return;
                    }
                } catch (NumberFormatException e) {
                    margenInputLayout.setError("Ingrese un valor numérico válido");
                    return;
                }
            }
            
            // Actualizar emprendimiento
            viewModel.actualizarEmprendimiento(nombre, rubro, descripcion, margenGanancia, logoPath);
        });
    }
    
    private void observeViewModel() {
        // Observar estado de carga
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                guardarButton.setEnabled(!isLoading);
                if (progressIndicator != null) {
                    progressIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                }
            }
        });
        
        // Observar errores
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                
                // Mostrar errores en los campos apropiados
                if (errorMessage.contains("nombre") || errorMessage.contains("Nombre")) {
                    nombreInputLayout.setError(errorMessage);
                } else if (errorMessage.contains("rubro") || errorMessage.contains("Rubro")) {
                    rubroInputLayout.setError(errorMessage);
                } else if (errorMessage.contains("margen") || errorMessage.contains("Margen")) {
                    margenInputLayout.setError(errorMessage);
                }
            }
        });
        
        // Observar empresa
        viewModel.getEmpresa().observe(this, empresa -> {
            if (empresa != null) {
                updateEmpresaFields(empresa);
            }
        });
        
        // Observar estado
        viewModel.getEmprendimientoState().observe(this, state -> {
            if (state != null) {
                if (state.isUpdateSuccess()) {
                    // Actualización exitosa
                    Toast.makeText(this, "Emprendimiento actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    // Volver al dashboard después de un breve delay
                    new android.os.Handler().postDelayed(() -> {
                        finish();
                    }, 1000);
                } else if (state.isError()) {
                    // Error ya manejado por errorMessage
                    clearErrors();
                }
            }
        });
    }
    
    private void updateEmpresaFields(Empresa empresa) {
        if (nombreEditText != null) {
            nombreEditText.setText(empresa.getNombre());
        }
        
        if (rubroEditText != null) {
            rubroEditText.setText(empresa.getRubro());
        }
        
        if (descripcionEditText != null) {
            descripcionEditText.setText(empresa.getDescripcion() != null ? empresa.getDescripcion() : "");
        }
        
        if (margenEditText != null) {
            margenEditText.setText(String.valueOf(empresa.getMargenGanancia()));
        }
        
        // Cargar logo si existe
        if (logoImageView != null && empresa.getLogoUrl() != null && !empresa.getLogoUrl().isEmpty()) {
            try {
                Uri logoUri = Uri.parse(empresa.getLogoUrl());
                logoImageView.setImageURI(logoUri);
                logoPath = empresa.getLogoUrl();
            } catch (Exception e) {
                // Si falla, mantener la imagen por defecto
            }
        }
    }
    
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
    
    private void clearErrors() {
        nombreInputLayout.setError(null);
        rubroInputLayout.setError(null);
        descripcionInputLayout.setError(null);
        margenInputLayout.setError(null);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

