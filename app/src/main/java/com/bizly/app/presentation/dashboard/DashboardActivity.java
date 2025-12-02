package com.bizly.app.presentation.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
    
    private int usuarioId;
    
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
        
        // Configurar listeners
        setupListeners();
        
        // Observar cambios en el ViewModel
        observeViewModel();
        
        // Cargar datos
        viewModel.cargarDatos(usuarioId);
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
    }
    
    private void setupToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    
    private void setupListeners() {
        // Botón editar emprendimiento
        editarEmprendimientoButton.setOnClickListener(v -> {
            // TODO: Navegar a EmprendimientoActivity cuando se implemente
            Toast.makeText(this, "Módulo de edición de emprendimiento próximamente", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Módulo de Trabajadores próximamente", Toast.LENGTH_SHORT).show();
        });
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
        
        // Observar estado
        viewModel.getDashboardState().observe(this, state -> {
            if (state != null && state.isDataLoaded()) {
                // Datos cargados exitosamente
            }
        });
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

