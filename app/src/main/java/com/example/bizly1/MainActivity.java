package com.example.bizly1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bizly1.auth.ConfiguracionEmprendimientoActivity;
import com.example.bizly1.auth.LoginActivity;
import com.example.bizly1.clientes.ClientesActivity;
import com.example.bizly1.costos.CostosGastosActivity;
import com.example.bizly1.data.network.ApiClient;
import com.example.bizly1.data.utils.AuthManager;
import com.example.bizly1.inventario.InventoryActivity;
import com.example.bizly1.productos.ProductosVentaActivity;
import com.example.bizly1.sucursales.SucursalesActivity;
import com.example.bizly1.ventas.PedidosPendientesActivity;
import com.example.bizly1.ventas.VentasActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inicializar ApiClient
        ApiClient.init(this);
        authManager = AuthManager.getInstance(this);
        
        // Verificar autenticación
        if (!authManager.isLoggedIn()) {
            // No está autenticado, redirigir a Login
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        
        // Verificar si tiene empresa configurada
        if (!authManager.isEmpresaConfigurada()) {
            // No tiene empresa configurada, redirigir a configuración
            Intent intent = new Intent(this, ConfiguracionEmprendimientoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
    }
    
    private void initViews() {
        // Mostrar nombre de usuario si está disponible
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        if (welcomeTextView != null && authManager.getUserName() != null) {
            welcomeTextView.setText("Bienvenido, " + authManager.getUserName());
        }
        
        // Mostrar nombre de empresa si está disponible (puedes obtenerlo de la API)
        TextView empresaTextView = findViewById(R.id.empresaTextView);
        if (empresaTextView != null) {
            empresaTextView.setText("Tu emprendimiento");
        }
    }
    
    private void setupListeners() {
        // Módulo: Inventario
        CardView cardInventario = findViewById(R.id.cardInventario);
        if (cardInventario != null) {
            cardInventario.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
                startActivity(intent);
            });
        }
        
        // Módulo: Productos de Venta
        CardView cardProductos = findViewById(R.id.cardProductos);
        if (cardProductos != null) {
            cardProductos.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProductosVentaActivity.class);
                startActivity(intent);
            });
        }
        
        // Módulo: Ventas
        CardView cardVentas = findViewById(R.id.cardVentas);
        if (cardVentas != null) {
            cardVentas.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, VentasActivity.class);
                startActivity(intent);
            });
        }
        
        // Módulo: Pedidos Pendientes
        CardView cardPedidos = findViewById(R.id.cardPedidos);
        if (cardPedidos != null) {
            cardPedidos.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, PedidosPendientesActivity.class);
                startActivity(intent);
            });
        }
        
        // Módulo: Clientes
        CardView cardClientes = findViewById(R.id.cardClientes);
        if (cardClientes != null) {
            cardClientes.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
                startActivity(intent);
            });
        }
        
        // Módulo: Costos y Gastos
        CardView cardCostos = findViewById(R.id.cardCostos);
        if (cardCostos != null) {
            cardCostos.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CostosGastosActivity.class);
                startActivity(intent);
            });
        }
        
        // Módulo: Sucursales
        CardView cardSucursales = findViewById(R.id.cardSucursales);
        if (cardSucursales != null) {
            cardSucursales.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SucursalesActivity.class);
                startActivity(intent);
            });
        }
        
        // Módulo: Reportes (futuro - por ahora solo muestra mensaje)
        CardView cardReportes = findViewById(R.id.cardReportes);
        if (cardReportes != null) {
            cardReportes.setOnClickListener(v -> {
                Toast.makeText(MainActivity.this, 
                    "Módulo de Reportes próximamente disponible", 
                    Toast.LENGTH_SHORT).show();
            });
        }
        
        // Botón de cerrar sesión
        MaterialButton btnLogout = findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> showLogoutDialog());
        }
    }
    
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí", (dialog, which) -> {
                authManager.logout();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
}