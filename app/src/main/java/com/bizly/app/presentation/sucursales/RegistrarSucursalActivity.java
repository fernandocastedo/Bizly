package com.bizly.app.presentation.sucursales;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bizly.app.R;
import com.bizly.app.domain.model.Sucursal;
import com.bizly.app.presentation.base.BaseState;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Activity para registrar una nueva sucursal
 */
public class RegistrarSucursalActivity extends AppCompatActivity {
    
    private RegistrarSucursalViewModel viewModel;
    
    private TextInputEditText nombreEditText;
    private TextInputEditText direccionEditText;
    private TextInputEditText ciudadEditText;
    private TextInputEditText departamentoEditText;
    private TextInputEditText telefonoEditText;
    private TextInputLayout nombreInputLayout;
    private TextInputLayout direccionInputLayout;
    private TextInputLayout ciudadInputLayout;
    private MaterialButton guardarButton;
    private CircularProgressIndicator progressIndicator;
    
    private int empresaId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_sucursal);
        
        // Configurar insets
        androidx.coordinatorlayout.widget.CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
        if (coordinatorLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(coordinatorLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        
        // Obtener empresaId del Intent
        empresaId = getIntent().getIntExtra("empresa_id", -1);
        if (empresaId <= 0) {
            Toast.makeText(this, "Empresa no identificada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(RegistrarSucursalViewModel.class);
        
        // Inicializar vistas
        initViews();
        
        // Configurar toolbar
        setupToolbar();
        
        // Configurar listeners
        setupListeners();
        
        // Observar cambios en el ViewModel
        observeViewModel();
    }
    
    private void initViews() {
        nombreEditText = findViewById(R.id.nombreEditText);
        direccionEditText = findViewById(R.id.direccionEditText);
        ciudadEditText = findViewById(R.id.ciudadEditText);
        departamentoEditText = findViewById(R.id.departamentoEditText);
        telefonoEditText = findViewById(R.id.telefonoEditText);
        nombreInputLayout = findViewById(R.id.nombreInputLayout);
        direccionInputLayout = findViewById(R.id.direccionInputLayout);
        ciudadInputLayout = findViewById(R.id.ciudadInputLayout);
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
        guardarButton.setOnClickListener(v -> {
            String nombre = nombreEditText.getText() != null ? nombreEditText.getText().toString() : "";
            String direccion = direccionEditText.getText() != null ? direccionEditText.getText().toString() : "";
            String ciudad = ciudadEditText.getText() != null ? ciudadEditText.getText().toString() : "";
            String departamento = departamentoEditText.getText() != null ? departamentoEditText.getText().toString() : "";
            String telefono = telefonoEditText.getText() != null ? telefonoEditText.getText().toString() : "";
            
            // Limpiar errores previos
            clearErrors();
            
            // Validar campos
            if (TextUtils.isEmpty(nombre)) {
                nombreInputLayout.setError("El nombre de la sucursal es requerido");
                return;
            }
            
            if (TextUtils.isEmpty(direccion)) {
                direccionInputLayout.setError("La dirección es requerida");
                return;
            }
            
            if (TextUtils.isEmpty(ciudad)) {
                ciudadInputLayout.setError("La ciudad es requerida");
                return;
            }
            
            // Registrar sucursal (latitud y longitud en 0 por ahora)
            viewModel.registrarSucursal(empresaId, nombre, direccion, ciudad, 
                                       departamento.isEmpty() ? null : departamento,
                                       telefono.isEmpty() ? null : telefono, 0.0, 0.0);
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
                } else if (errorMessage.contains("dirección") || errorMessage.contains("Dirección")) {
                    direccionInputLayout.setError(errorMessage);
                } else if (errorMessage.contains("ciudad") || errorMessage.contains("Ciudad")) {
                    ciudadInputLayout.setError(errorMessage);
                }
            }
        });
        
        // Observar estado
        viewModel.getState().observe(this, state -> {
            if (state != null && state.isSuccess()) {
                // Registro exitoso
                Sucursal sucursal = viewModel.getSucursalRegistrada().getValue();
                if (sucursal != null) {
                    Toast.makeText(this, "Sucursal registrada exitosamente", Toast.LENGTH_SHORT).show();
                    // Volver a la lista de sucursales
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }
    
    private void clearErrors() {
        nombreInputLayout.setError(null);
        direccionInputLayout.setError(null);
        ciudadInputLayout.setError(null);
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


