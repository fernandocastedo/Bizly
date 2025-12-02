package com.bizly.app.presentation.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bizly.app.MainActivity;
import com.bizly.app.R;
import com.bizly.app.domain.model.Empresa;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.presentation.base.BaseState;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

/**
 * Activity principal del dashboard
 * RF-07
 */
public class DashboardActivity extends AppCompatActivity {
    
    private DashboardViewModel viewModel;
    
    // Vistas
    private TextView welcomeTextView;
    private TextView userNameTextView;
    private ImageView empresaLogoImageView;
    private TextView empresaNombreTextView;
    private TextView empresaRubroTextView;
    private TextView empresaDescripcionTextView;
    private TextView empresaMargenTextView;
    private MaterialButton editarEmprendimientoButton;
    private MaterialCardView inventarioCard;
    private MaterialCardView productosCard;
    private MaterialCardView ventasCard;
    private MaterialCardView trabajadoresCard;
    private CircularProgressIndicator progressIndicator;
    private AutoCompleteTextView sucursalAutoComplete;
    private com.google.android.material.textfield.TextInputLayout sucursalSpinnerLayout;
    
    private int usuarioId;
    private List<com.bizly.app.domain.model.Sucursal> sucursalesList = new ArrayList<>();
    private ArrayAdapter<String> sucursalAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        
        // Configurar insets para el CoordinatorLayout
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
            // Si no hay usuarioId, volver a login
            Toast.makeText(this, "Sesión inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        
        // Inicializar vistas
        initViews();
        
        // Configurar toolbar
        setupToolbar();
        
        // Configurar selector de sucursal
        setupSucursalSelector();
        
        // Configurar listeners
        setupListeners();
        
        // Observar cambios en el ViewModel
        observeViewModel();
        
        // Cargar datos
        viewModel.cargarDatos(usuarioId);
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
                viewModel.seleccionarSucursal(null);
            } else if (position > 0 && position <= sucursalesList.size()) {
                // Sucursal específica
                com.bizly.app.domain.model.Sucursal sucursal = sucursalesList.get(position - 1);
                viewModel.seleccionarSucursal(sucursal);
                Toast.makeText(this, "Sucursal seleccionada: " + sucursal.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void initViews() {
        welcomeTextView = findViewById(R.id.welcomeTextView);
        userNameTextView = findViewById(R.id.userNameTextView);
        empresaLogoImageView = findViewById(R.id.empresaLogoImageView);
        empresaNombreTextView = findViewById(R.id.empresaNombreTextView);
        empresaRubroTextView = findViewById(R.id.empresaRubroTextView);
        empresaDescripcionTextView = findViewById(R.id.empresaDescripcionTextView);
        empresaMargenTextView = findViewById(R.id.empresaMargenTextView);
        editarEmprendimientoButton = findViewById(R.id.editarEmprendimientoButton);
        inventarioCard = findViewById(R.id.inventarioCard);
        productosCard = findViewById(R.id.productosCard);
        ventasCard = findViewById(R.id.ventasCard);
        trabajadoresCard = findViewById(R.id.trabajadoresCard);
        progressIndicator = findViewById(R.id.progressIndicator);
        sucursalAutoComplete = findViewById(R.id.sucursalAutoComplete);
        sucursalSpinnerLayout = findViewById(R.id.sucursalSpinnerLayout);
    }
    
    private void setupToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    
    private void setupListeners() {
        // Botón editar emprendimiento
        editarEmprendimientoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.bizly.app.presentation.emprendimiento.EmprendimientoActivity.class);
            intent.putExtra("usuario_id", usuarioId);
            startActivity(intent);
        });
        
        // Cards de acceso rápido (placeholders por ahora)
        inventarioCard.setOnClickListener(v -> {
            Toast.makeText(this, "Módulo de Inventario próximamente", Toast.LENGTH_SHORT).show();
        });
        
        productosCard.setOnClickListener(v -> {
            Toast.makeText(this, "Módulo de Productos próximamente", Toast.LENGTH_SHORT).show();
        });
        
        ventasCard.setOnClickListener(v -> {
            Toast.makeText(this, "Módulo de Ventas próximamente", Toast.LENGTH_SHORT).show();
        });
        
        trabajadoresCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.bizly.app.presentation.trabajadores.TrabajadoresActivity.class);
            intent.putExtra("usuario_id", usuarioId);
            startActivity(intent);
        });
        
        // Card de sucursales
        MaterialCardView sucursalesCard = findViewById(R.id.sucursalesCard);
        if (sucursalesCard != null) {
            sucursalesCard.setOnClickListener(v -> {
                Intent intent = new Intent(this, com.bizly.app.presentation.sucursales.SucursalesActivity.class);
                intent.putExtra("usuario_id", usuarioId);
                startActivity(intent);
            });
        }
    }
    
    private void observeViewModel() {
        // Observar estado de carga
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
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
        
        // Observar usuario
        viewModel.getUsuario().observe(this, usuario -> {
            if (usuario != null) {
                updateUsuarioInfo(usuario);
            }
        });
        
        // Observar empresa
        viewModel.getEmpresa().observe(this, empresa -> {
            if (empresa != null) {
                updateEmpresaInfo(empresa);
            }
        });
        
        // Observar sucursales
        viewModel.getSucursales().observe(this, sucursales -> {
            if (sucursales != null) {
                sucursalesList = sucursales;
                updateSucursalSelector();
            }
        });
        
        // Observar sucursal seleccionada
        viewModel.getSucursalSeleccionada().observe(this, sucursal -> {
            // La sucursal seleccionada se puede usar para filtrar datos en otras pantallas
            // Por ahora solo la guardamos en el ViewModel
        });
        
        // Observar estado
        viewModel.getDashboardState().observe(this, state -> {
            if (state != null && state.isDataLoaded()) {
                // Datos cargados exitosamente
            }
        });
    }
    
    private void updateSucursalSelector() {
        List<String> opciones = new ArrayList<>();
        opciones.add("Todas las sucursales");
        
        for (com.bizly.app.domain.model.Sucursal sucursal : sucursalesList) {
            opciones.add(sucursal.getNombre());
        }
        
        sucursalAdapter.clear();
        sucursalAdapter.addAll(opciones);
        sucursalAdapter.notifyDataSetChanged();
        
        // Si hay sucursales, seleccionar la primera por defecto
        if (!sucursalesList.isEmpty()) {
            com.bizly.app.domain.model.Sucursal primeraSucursal = sucursalesList.get(0);
            sucursalAutoComplete.setText(primeraSucursal.getNombre(), false);
            viewModel.seleccionarSucursal(primeraSucursal);
        }
    }
    
    private void updateUsuarioInfo(Usuario usuario) {
        if (userNameTextView != null) {
            userNameTextView.setText(usuario.getNombre());
        }
    }
    
    private void updateEmpresaInfo(Empresa empresa) {
        if (empresaNombreTextView != null) {
            empresaNombreTextView.setText(empresa.getNombre());
        }
        
        if (empresaRubroTextView != null) {
            empresaRubroTextView.setText(empresa.getRubro());
        }
        
        if (empresaDescripcionTextView != null) {
            String descripcion = empresa.getDescripcion();
            if (descripcion == null || descripcion.isEmpty()) {
                empresaDescripcionTextView.setText("Sin descripción");
            } else {
                empresaDescripcionTextView.setText(descripcion);
            }
        }
        
        if (empresaMargenTextView != null) {
            empresaMargenTextView.setText(String.format("%.1f%%", empresa.getMargenGanancia()));
        }
        
        // Cargar logo si existe
        if (empresaLogoImageView != null && empresa.getLogoUrl() != null && !empresa.getLogoUrl().isEmpty()) {
            try {
                Uri logoUri = Uri.parse(empresa.getLogoUrl());
                empresaLogoImageView.setImageURI(logoUri);
            } catch (Exception e) {
                // Si falla, mantener la imagen por defecto
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Recargar datos cuando la actividad vuelve a estar visible
        // Esto asegura que los datos estén actualizados si se hicieron cambios en otra pantalla
        if (usuarioId > 0) {
            viewModel.recargarDatos();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            // Cerrar sesión y volver a login
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

