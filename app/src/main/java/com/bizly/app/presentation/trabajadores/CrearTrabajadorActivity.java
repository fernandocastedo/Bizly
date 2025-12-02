package com.bizly.app.presentation.trabajadores;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bizly.app.R;
import com.bizly.app.domain.model.Sucursal;
import com.bizly.app.domain.model.Trabajador;
import com.bizly.app.presentation.base.BaseState;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity para crear un nuevo trabajador
 * RF-46
 */
public class CrearTrabajadorActivity extends AppCompatActivity {
    
    private CrearTrabajadorViewModel viewModel;
    
    private TextInputEditText nombreEditText;
    private TextInputEditText cargoEditText;
    private TextInputEditText sueldoEditText;
    private AutoCompleteTextView sucursalAutoComplete;
    private AutoCompleteTextView tipoGastoAutoComplete;
    private TextInputLayout nombreInputLayout;
    private TextInputLayout cargoInputLayout;
    private TextInputLayout sueldoInputLayout;
    private TextInputLayout tipoGastoInputLayout;
    private MaterialButton guardarButton;
    private CircularProgressIndicator progressIndicator;
    
    private int empresaId;
    private List<Sucursal> sucursalesList = new ArrayList<>();
    private ArrayAdapter<String> sucursalAdapter;
    private Integer sucursalSeleccionadaId = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_trabajador);
        
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
        viewModel = new ViewModelProvider(this).get(CrearTrabajadorViewModel.class);
        
        // Inicializar vistas
        initViews();
        
        // Configurar toolbar
        setupToolbar();
        
        // Configurar selectores
        setupSelectors();
        
        // Configurar listeners
        setupListeners();
        
        // Observar cambios en el ViewModel
        observeViewModel();
        
        // Cargar sucursales
        viewModel.cargarSucursales(empresaId);
    }
    
    private void initViews() {
        nombreEditText = findViewById(R.id.nombreEditText);
        cargoEditText = findViewById(R.id.cargoEditText);
        sueldoEditText = findViewById(R.id.sueldoEditText);
        sucursalAutoComplete = findViewById(R.id.sucursalAutoComplete);
        tipoGastoAutoComplete = findViewById(R.id.tipoGastoAutoComplete);
        nombreInputLayout = findViewById(R.id.nombreInputLayout);
        cargoInputLayout = findViewById(R.id.cargoInputLayout);
        sueldoInputLayout = findViewById(R.id.sueldoInputLayout);
        tipoGastoInputLayout = findViewById(R.id.tipoGastoInputLayout);
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
    
    private void setupSelectors() {
        // Selector de tipo de gasto
        String[] tiposGasto = {"Fijo", "Variable"};
        ArrayAdapter<String> tipoGastoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tiposGasto);
        tipoGastoAutoComplete.setAdapter(tipoGastoAdapter);
        tipoGastoAutoComplete.setText("Fijo", false);
        
        // Selector de sucursal (se actualizará cuando se carguen las sucursales)
        List<String> opciones = new ArrayList<>();
        sucursalAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opciones);
        sucursalAutoComplete.setAdapter(sucursalAdapter);
        sucursalAutoComplete.setThreshold(1); // Mostrar opciones después de 1 carácter
        sucursalAutoComplete.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                sucursalAutoComplete.showDropDown();
            }
        });
        sucursalAutoComplete.setOnClickListener(v -> {
            sucursalAutoComplete.showDropDown();
        });
        
        // Listener para selección de sucursal
        sucursalAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            // Sucursal específica (requerida)
            if (position >= 0 && position < sucursalesList.size()) {
                Sucursal sucursal = sucursalesList.get(position);
                sucursalSeleccionadaId = sucursal.getId();
            }
        });
    }
    
    private void setupListeners() {
        guardarButton.setOnClickListener(v -> {
            // Verificar si ya está en proceso de carga
            Boolean isLoading = viewModel.getIsLoading().getValue();
            if (isLoading != null && isLoading) {
                return; // Ya está procesando, ignorar clic
            }
            
            String nombre = nombreEditText.getText() != null ? nombreEditText.getText().toString() : "";
            String cargo = cargoEditText.getText() != null ? cargoEditText.getText().toString() : "";
            String sueldoStr = sueldoEditText.getText() != null ? sueldoEditText.getText().toString() : "";
            String tipoGastoStr = tipoGastoAutoComplete.getText() != null ? tipoGastoAutoComplete.getText().toString() : "";
            
            // Limpiar errores previos
            clearErrors();
            
            // Validar campos
            if (TextUtils.isEmpty(nombre)) {
                nombreInputLayout.setError("El nombre del trabajador es requerido");
                return;
            }
            
            if (TextUtils.isEmpty(cargo)) {
                cargoInputLayout.setError("El cargo es requerido");
                return;
            }
            
            double sueldoMensual = 0.0;
            if (!TextUtils.isEmpty(sueldoStr)) {
                try {
                    sueldoMensual = Double.parseDouble(sueldoStr);
                    if (sueldoMensual < 0) {
                        sueldoInputLayout.setError("El sueldo mensual debe ser positivo");
                        return;
                    }
                } catch (NumberFormatException e) {
                    sueldoInputLayout.setError("Ingrese un valor numérico válido");
                    return;
                }
            }
            
            // Convertir tipo de gasto
            String tipoGasto = tipoGastoStr.equalsIgnoreCase("Fijo") ? "fijo" : "variable";
            
            // Validar que se haya seleccionado una sucursal (requerida)
            if (sucursalSeleccionadaId == null || sucursalSeleccionadaId <= 0) {
                Toast.makeText(this, "Debe seleccionar una sucursal", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Deshabilitar botón inmediatamente para evitar doble clic
            guardarButton.setEnabled(false);
            
            // Registrar trabajador
            viewModel.registrarTrabajador(nombre, cargo, sueldoMensual, tipoGasto, sucursalSeleccionadaId);
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
                } else if (errorMessage.contains("cargo") || errorMessage.contains("Cargo")) {
                    cargoInputLayout.setError(errorMessage);
                } else if (errorMessage.contains("sueldo") || errorMessage.contains("Sueldo")) {
                    sueldoInputLayout.setError(errorMessage);
                } else if (errorMessage.contains("tipo de gasto") || errorMessage.contains("Tipo de gasto")) {
                    tipoGastoInputLayout.setError(errorMessage);
                }
            }
        });
        
        // Observar sucursales
        viewModel.getSucursales().observe(this, sucursales -> {
            if (sucursales != null) {
                sucursalesList = sucursales;
                updateSucursalSelector();
            }
        });
        
        // Observar estado
        viewModel.getState().observe(this, state -> {
            if (state != null && state.isSuccess()) {
                // Registro exitoso
                Trabajador trabajador = viewModel.getTrabajadorRegistrado().getValue();
                if (trabajador != null) {
                    Toast.makeText(this, "Trabajador registrado exitosamente", Toast.LENGTH_SHORT).show();
                    // Volver a la lista de trabajadores
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            } else if (state != null && state.isError()) {
                // Rehabilitar botón en caso de error
                guardarButton.setEnabled(true);
            }
        });
    }
    
    private void updateSucursalSelector() {
        List<String> opciones = new ArrayList<>();
        
        for (Sucursal sucursal : sucursalesList) {
            opciones.add(sucursal.getNombre());
        }
        
        sucursalAdapter.clear();
        sucursalAdapter.addAll(opciones);
        sucursalAdapter.notifyDataSetChanged();
        
        // Seleccionar la primera sucursal por defecto
        if (!sucursalesList.isEmpty()) {
            Sucursal primeraSucursal = sucursalesList.get(0);
            sucursalAutoComplete.setText(primeraSucursal.getNombre(), false);
            sucursalSeleccionadaId = primeraSucursal.getId();
        }
    }
    
    private void clearErrors() {
        nombreInputLayout.setError(null);
        cargoInputLayout.setError(null);
        sueldoInputLayout.setError(null);
        tipoGastoInputLayout.setError(null);
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

