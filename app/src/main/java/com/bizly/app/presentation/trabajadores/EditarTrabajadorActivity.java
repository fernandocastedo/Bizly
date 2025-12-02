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
 * Activity para editar un trabajador existente
 * RF-47
 */
public class EditarTrabajadorActivity extends AppCompatActivity {
    
    private EditarTrabajadorViewModel viewModel;
    
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
    private int trabajadorId;
    private List<Sucursal> sucursalesList = new ArrayList<>();
    private ArrayAdapter<String> sucursalAdapter;
    private Integer sucursalSeleccionadaId = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_trabajador);
        
        // Configurar insets
        androidx.coordinatorlayout.widget.CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
        if (coordinatorLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(coordinatorLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        
        // Obtener IDs del Intent
        trabajadorId = getIntent().getIntExtra("trabajador_id", -1);
        empresaId = getIntent().getIntExtra("empresa_id", -1);
        
        if (trabajadorId <= 0 || empresaId <= 0) {
            Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(EditarTrabajadorViewModel.class);
        
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
        
        // Cargar datos
        viewModel.cargarDatos(trabajadorId, empresaId);
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
            getSupportActionBar().setTitle("Editar Trabajador");
        }
    }
    
    private void setupSelectors() {
        // Selector de tipo de gasto
        String[] tiposGasto = {"Fijo", "Variable"};
        ArrayAdapter<String> tipoGastoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tiposGasto);
        tipoGastoAutoComplete.setAdapter(tipoGastoAdapter);
        
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
            if (position >= 0 && position < sucursalesList.size()) {
                Sucursal sucursal = sucursalesList.get(position);
                sucursalSeleccionadaId = sucursal.getId();
            }
        });
    }
    
    private void setupListeners() {
        guardarButton.setOnClickListener(v -> {
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
            
            // Actualizar trabajador
            viewModel.actualizarTrabajador(nombre, cargo, sueldoMensual, tipoGasto, sucursalSeleccionadaId);
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
        
        // Observar trabajador
        viewModel.getTrabajador().observe(this, trabajador -> {
            if (trabajador != null) {
                // Llenar campos con datos del trabajador
                nombreEditText.setText(trabajador.getNombre());
                cargoEditText.setText(trabajador.getCargo());
                sueldoEditText.setText(String.valueOf(trabajador.getSueldoMensual()));
                
                // Tipo de gasto
                String tipoGasto = trabajador.getTipoGasto();
                if (tipoGasto != null) {
                    tipoGastoAutoComplete.setText(tipoGasto.equals("fijo") ? "Fijo" : "Variable", false);
                }
                
                // Sucursal
                if (trabajador.getSucursalId() != null) {
                    sucursalSeleccionadaId = trabajador.getSucursalId();
                    // Actualizar el texto cuando se carguen las sucursales
                }
            }
        });
        
        // Observar sucursales
        viewModel.getSucursales().observe(this, sucursales -> {
            if (sucursales != null) {
                sucursalesList = sucursales;
                updateSucursalSelector();
                
                // Seleccionar la sucursal actual del trabajador
                Trabajador trabajador = viewModel.getTrabajador().getValue();
                if (trabajador != null && trabajador.getSucursalId() != null) {
                    for (int i = 0; i < sucursalesList.size(); i++) {
                        if (sucursalesList.get(i).getId() == trabajador.getSucursalId()) {
                            sucursalAutoComplete.setText(sucursalesList.get(i).getNombre(), false);
                            sucursalSeleccionadaId = trabajador.getSucursalId();
                            break;
                        }
                    }
                }
            }
        });
        
        // Observar estado
        viewModel.getState().observe(this, state -> {
            if (state != null && state.isSuccess()) {
                Boolean actualizado = viewModel.getTrabajadorActualizado().getValue();
                if (actualizado != null && actualizado) {
                    Toast.makeText(this, "Trabajador actualizado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
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
        
        // Si no hay sucursal seleccionada, seleccionar la primera por defecto
        Trabajador trabajador = viewModel.getTrabajador().getValue();
        if (trabajador != null && trabajador.getSucursalId() != null) {
            // Seleccionar la sucursal actual del trabajador
            for (int i = 0; i < sucursalesList.size(); i++) {
                if (sucursalesList.get(i).getId() == trabajador.getSucursalId()) {
                    sucursalAutoComplete.setText(sucursalesList.get(i).getNombre(), false);
                    sucursalSeleccionadaId = trabajador.getSucursalId();
                    return;
                }
            }
        }
        
        // Si no se encontró la sucursal del trabajador o no tiene, seleccionar la primera
        if (!sucursalesList.isEmpty() && sucursalSeleccionadaId == null) {
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

