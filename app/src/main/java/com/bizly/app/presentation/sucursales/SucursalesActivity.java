package com.bizly.app.presentation.sucursales;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.bizly.app.presentation.base.BaseState;
import com.bizly.app.presentation.dashboard.DashboardActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity para ver y gestionar sucursales
 */
public class SucursalesActivity extends AppCompatActivity {
    
    private SucursalesViewModel viewModel;
    private RecyclerView sucursalesRecyclerView;
    private SucursalAdapter adapter;
    private MaterialButton agregarSucursalButton;
    private TextView emptyTextView;
    private CircularProgressIndicator progressIndicator;
    
    private int usuarioId;
    private List<Sucursal> sucursalesList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sucursales);
        
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
        viewModel = new ViewModelProvider(this).get(SucursalesViewModel.class);
        
        // Inicializar vistas
        initViews();
        
        // Configurar toolbar
        setupToolbar();
        
        // Configurar RecyclerView
        setupRecyclerView();
        
        // Configurar listeners
        setupListeners();
        
        // Observar cambios en el ViewModel
        observeViewModel();
        
        // Cargar sucursales
        viewModel.cargarSucursales(usuarioId);
    }
    
    private void initViews() {
        sucursalesRecyclerView = findViewById(R.id.sucursalesRecyclerView);
        agregarSucursalButton = findViewById(R.id.agregarSucursalButton);
        emptyTextView = findViewById(R.id.emptyTextView);
        progressIndicator = findViewById(R.id.progressIndicator);
    }
    
    private void setupToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupRecyclerView() {
        adapter = new SucursalAdapter(sucursalesList, sucursal -> {
            // Al hacer clic en una sucursal, podría mostrar detalles o seleccionarla
            // Por ahora solo mostramos un mensaje
            Toast.makeText(this, "Sucursal seleccionada: " + sucursal.getNombre(), Toast.LENGTH_SHORT).show();
        });
        
        sucursalesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sucursalesRecyclerView.setAdapter(adapter);
    }
    
    private void setupListeners() {
        agregarSucursalButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrarSucursalActivity.class);
            intent.putExtra("usuario_id", usuarioId);
            intent.putExtra("empresa_id", viewModel.getEmpresaId());
            startActivityForResult(intent, 100);
        });
    }
    
    private void observeViewModel() {
        // Observar estado de carga
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                agregarSucursalButton.setEnabled(!isLoading);
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
                adapter.updateSucursales(sucursales);
                
                // Mostrar/ocultar mensaje de vacío
                if (sucursales.isEmpty()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                    sucursalesRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyTextView.setVisibility(View.GONE);
                    sucursalesRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            // Recargar sucursales después de crear una nueva
            // Usar postValue para asegurar que se ejecute en el hilo principal
            viewModel.recargarSucursales();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Recargar datos cuando la actividad vuelve a estar visible
        // Esto asegura que los datos estén actualizados si se hicieron cambios en otra pantalla
        if (usuarioId > 0) {
            viewModel.recargarSucursales();
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

