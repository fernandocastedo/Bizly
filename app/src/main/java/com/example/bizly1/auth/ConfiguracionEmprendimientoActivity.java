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
import com.example.bizly1.models.Empresa;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfiguracionEmprendimientoActivity extends AppCompatActivity {

    private TextInputEditText nombreEditText;
    private TextInputEditText rubroEditText;
    private TextInputEditText margenEditText;
    private TextInputEditText descripcionEditText;
    private TextInputLayout nombreInputLayout;
    private TextInputLayout rubroInputLayout;
    private TextInputLayout margenInputLayout;
    private MaterialButton saveButton;
    private ProgressBar progressBar;
    
    private ApiService apiService;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_emprendimiento);
        
        // Inicializar ApiClient
        ApiClient.init(this);
        apiService = ApiClient.getApiService();
        authManager = AuthManager.getInstance(this);
        
        // Verificar si ya tiene empresa configurada
        if (authManager.isEmpresaConfigurada()) {
            navigateToMain();
            return;
        }
        
        initViews();
        setupListeners();
    }
    
    private void initViews() {
        nombreEditText = findViewById(R.id.nombreEditText);
        rubroEditText = findViewById(R.id.rubroEditText);
        margenEditText = findViewById(R.id.margenEditText);
        descripcionEditText = findViewById(R.id.descripcionEditText);
        nombreInputLayout = findViewById(R.id.nombreInputLayout);
        rubroInputLayout = findViewById(R.id.rubroInputLayout);
        margenInputLayout = findViewById(R.id.margenInputLayout);
        saveButton = findViewById(R.id.saveButton);
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void setupListeners() {
        saveButton.setOnClickListener(v -> saveEmpresa());
    }
    
    private void saveEmpresa() {
        String nombre = nombreEditText.getText().toString().trim();
        String rubro = rubroEditText.getText().toString().trim();
        String margenStr = margenEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();
        
        // Validar campos
        if (!validateFields(nombre, rubro, margenStr)) {
            return;
        }
        
        // Convertir margen a double
        double margen;
        try {
            margen = Double.parseDouble(margenStr);
            if (margen < 0 || margen > 100) {
                margenInputLayout.setError("El margen debe estar entre 0 y 100");
                return;
            }
        } catch (NumberFormatException e) {
            margenInputLayout.setError("Ingresa un número válido");
            return;
        }
        
        // Mostrar progress bar
        setLoading(true);
        
        // Crear empresa
        Empresa empresa = new Empresa(nombre, rubro, margen, descripcion);
        
        // Realizar petición
        Call<Empresa> call = apiService.crearEmpresa(empresa);
        call.enqueue(new Callback<Empresa>() {
            @Override
            public void onResponse(Call<Empresa> call, Response<Empresa> response) {
                setLoading(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    Empresa empresaCreada = response.body();
                    
                    // Guardar ID de empresa y marcar como configurada
                    if (empresaCreada.getId() != null) {
                        authManager.saveUserInfo(
                            authManager.getUserId(),
                            authManager.getUserName(),
                            authManager.getUserEmail(),
                            authManager.getUserRol(),
                            empresaCreada.getId()
                        );
                        authManager.setEmpresaConfigurada(true);
                    }
                    
                    Toast.makeText(ConfiguracionEmprendimientoActivity.this, 
                        "Emprendimiento configurado exitosamente", Toast.LENGTH_SHORT).show();
                    
                    // Navegar al MainActivity
                    navigateToMain();
                } else {
                    String errorMessage = "Error al crear emprendimiento";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(ConfiguracionEmprendimientoActivity.this, 
                        errorMessage, Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<Empresa> call, Throwable t) {
                setLoading(false);
                Toast.makeText(ConfiguracionEmprendimientoActivity.this, 
                    "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private boolean validateFields(String nombre, String rubro, String margen) {
        boolean isValid = true;
        
        // Validar nombre
        if (TextUtils.isEmpty(nombre)) {
            nombreInputLayout.setError("El nombre del emprendimiento es requerido");
            isValid = false;
        } else if (nombre.length() < 3) {
            nombreInputLayout.setError("El nombre debe tener al menos 3 caracteres");
            isValid = false;
        } else {
            nombreInputLayout.setError(null);
        }
        
        // Validar rubro
        if (TextUtils.isEmpty(rubro)) {
            rubroInputLayout.setError("El rubro es requerido");
            isValid = false;
        } else {
            rubroInputLayout.setError(null);
        }
        
        // Validar margen
        if (TextUtils.isEmpty(margen)) {
            margenInputLayout.setError("El margen de ganancia es requerido");
            isValid = false;
        } else {
            margenInputLayout.setError(null);
        }
        
        return isValid;
    }
    
    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        saveButton.setEnabled(!loading);
        nombreEditText.setEnabled(!loading);
        rubroEditText.setEnabled(!loading);
        margenEditText.setEnabled(!loading);
        descripcionEditText.setEnabled(!loading);
    }
    
    private void navigateToMain() {
        Intent intent = new Intent(ConfiguracionEmprendimientoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

