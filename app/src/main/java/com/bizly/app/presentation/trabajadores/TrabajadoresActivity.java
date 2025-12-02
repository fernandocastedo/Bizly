package com.bizly.app.presentation.trabajadores;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bizly.app.R;
import com.bizly.app.domain.model.Sucursal;
import com.bizly.app.domain.model.Trabajador;
import com.bizly.app.presentation.base.BaseState;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity para ver y gestionar trabajadores
 * RF-46, RF-47, RF-51
 */
public class TrabajadoresActivity extends AppCompatActivity {
    
    private TrabajadoresViewModel viewModel;
    private RecyclerView trabajadoresRecyclerView;
    private TrabajadorAdapter adapter;
    private MaterialButton agregarTrabajadorButton;
    private TextView emptyTextView;
    private CircularProgressIndicator progressIndicator;
    private AutoCompleteTextView sucursalAutoComplete;
    private TextInputLayout sucursalSpinnerLayout;
    
    private int usuarioId;
    private List<Trabajador> trabajadoresList = new ArrayList<>();
    private List<Sucursal> sucursalesList = new ArrayList<>();
    private ArrayAdapter<String> sucursalAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trabajadores);
        
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
        viewModel = new ViewModelProvider(this).get(TrabajadoresViewModel.class);
        
        // Inicializar vistas
        initViews();
        
        // Configurar toolbar
        setupToolbar();
        
        // Configurar RecyclerView
        setupRecyclerView();
        
        // Configurar selector de sucursal
        setupSucursalSelector();
        
        // Configurar listeners
        setupListeners();
        
        // Observar cambios en el ViewModel
        observeViewModel();
        
        // Cargar trabajadores
        viewModel.cargarTrabajadores(usuarioId);
    }
    
    private void initViews() {
        trabajadoresRecyclerView = findViewById(R.id.trabajadoresRecyclerView);
        agregarTrabajadorButton = findViewById(R.id.agregarTrabajadorButton);
        emptyTextView = findViewById(R.id.emptyTextView);
        progressIndicator = findViewById(R.id.progressIndicator);
        sucursalAutoComplete = findViewById(R.id.sucursalAutoComplete);
        sucursalSpinnerLayout = findViewById(R.id.sucursalSpinnerLayout);
    }
    
    private void setupToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupRecyclerView() {
        adapter = new TrabajadorAdapter(trabajadoresList, new TrabajadorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Trabajador trabajador) {
                // Al hacer clic en un trabajador, podría mostrar detalles
                // Por ahora no hacemos nada
            }
            
            @Override
            public void onEditClick(Trabajador trabajador) {
                // Navegar a editar trabajador
                Intent intent = new Intent(TrabajadoresActivity.this, EditarTrabajadorActivity.class);
                intent.putExtra("trabajador_id", trabajador.getId());
                intent.putExtra("usuario_id", usuarioId);
                intent.putExtra("empresa_id", viewModel.getEmpresaId());
                startActivityForResult(intent, 201);
            }
            
            @Override
            public void onDeleteClick(Trabajador trabajador) {
                // Mostrar diálogo de confirmación
                new androidx.appcompat.app.AlertDialog.Builder(TrabajadoresActivity.this)
                    .setTitle("Eliminar Trabajador")
                    .setMessage("¿Está seguro que desea eliminar a " + trabajador.getNombre() + "?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        viewModel.eliminarTrabajador(trabajador.getId());
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            }
        });
        
        trabajadoresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trabajadoresRecyclerView.setAdapter(adapter);
    }
    
    private void setupSucursalSelector() {
        // Crear lista de opciones (incluyendo "Todas")
        List<String> opciones = new ArrayList<>();
        opciones.add("Todas las sucursales");
        
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
        sucursalAutoComplete.setText("Todas las sucursales", false);
        
        // Listener para cuando se selecciona una sucursal
        sucursalAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                // "Todas las sucursales"
                viewModel.filtrarPorSucursal(null);
            } else if (position > 0 && position <= sucursalesList.size()) {
                // Sucursal específica
                Sucursal sucursal = sucursalesList.get(position - 1);
                viewModel.filtrarPorSucursal(sucursal.getId());
            }
        });
    }
    
    private void setupListeners() {
        agregarTrabajadorButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearTrabajadorActivity.class);
            intent.putExtra("usuario_id", usuarioId);
            intent.putExtra("empresa_id", viewModel.getEmpresaId());
            startActivityForResult(intent, 200);
        });
    }
    
    private void observeViewModel() {
        // Observar estado de carga
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                agregarTrabajadorButton.setEnabled(!isLoading);
                if (progressIndicator != null) {
                    progressIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                }
            }
        });
        
        // Observar errores
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
        
        // Observar sucursales
        viewModel.getSucursales().observe(this, sucursales -> {
            if (sucursales != null) {
                sucursalesList = sucursales;
                updateSucursalSelector();
            }
        });
        
        // Observar trabajadores
        viewModel.getTrabajadores().observe(this, trabajadores -> {
            if (trabajadores != null) {
                trabajadoresList = trabajadores;
                adapter.updateTrabajadores(trabajadores);
                
                // Mostrar/ocultar mensaje de vacío
                if (trabajadores.isEmpty()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                    trabajadoresRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyTextView.setVisibility(View.GONE);
                    trabajadoresRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    
    private void updateSucursalSelector() {
        List<String> opciones = new ArrayList<>();
        opciones.add("Todas las sucursales");
        
        for (Sucursal sucursal : sucursalesList) {
            opciones.add(sucursal.getNombre());
        }
        
        sucursalAdapter.clear();
        sucursalAdapter.addAll(opciones);
        sucursalAdapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 200 || requestCode == 201) && resultCode == RESULT_OK) {
            // Recargar trabajadores después de crear o editar
            viewModel.recargarTrabajadores();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Recargar datos cuando la actividad vuelve a estar visible
        // Esto asegura que los datos estén actualizados si se hicieron cambios en otra pantalla
        if (usuarioId > 0) {
            viewModel.recargarTrabajadores();
        }
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

