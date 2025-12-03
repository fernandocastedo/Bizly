package com.example.bizly1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends AppCompatActivity {

    private MaterialButton btnAddProduct, btnAddSupply;
    private BottomNavigationView bottomNavigationView;
    private ImageView userIcon, notificationIcon;
    private TextView welcomeText, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Views
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddSupply = findViewById(R.id.btnAddSupply);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        
        // Header views
        userIcon = findViewById(R.id.userIcon);
        notificationIcon = findViewById(R.id.notificationIcon);
        welcomeText = findViewById(R.id.welcomeText);
        userName = findViewById(R.id.userName);

        // Setup Buttons
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        btnAddSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AddSupplyActivity.class);
                startActivity(intent);
            }
        });
        
        // Header listeners
        userIcon.setOnClickListener(v -> {
            // Navigate to profile or settings
            Toast.makeText(DashboardActivity.this, "Perfil de usuario", Toast.LENGTH_SHORT).show();
        });
        
        notificationIcon.setOnClickListener(v -> {
            // Show notifications
            Toast.makeText(DashboardActivity.this, "Sin notificaciones nuevas", Toast.LENGTH_SHORT).show();
        });
        
        // Setup Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_products) {
                // Navigate to Products list
                // Intent intent = new Intent(DashboardActivity.this, ProductsActivity.class);
                // startActivity(intent);
                Toast.makeText(this, "Productos", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_sales) {
                // Navigate to Sales
                // Intent intent = new Intent(DashboardActivity.this, SalesActivity.class);
                // startActivity(intent);
                Toast.makeText(this, "Ventas", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_reports) {
                // Navigate to Reports
                Toast.makeText(this, "Reportes", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}
