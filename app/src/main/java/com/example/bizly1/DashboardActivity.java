package com.example.bizly1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends AppCompatActivity {

    private MaterialButton btnAddProduct, btnAddSupply;
    private MaterialButton btnInventory, btnSales, btnCustomers, btnEmployees, btnCosts, btnExpenses;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Views
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddSupply = findViewById(R.id.btnAddSupply);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        
        // Grid Buttons
        btnInventory = findViewById(R.id.btnInventory);
        btnSales = findViewById(R.id.btnSales);
        btnCustomers = findViewById(R.id.btnCustomers);
        btnEmployees = findViewById(R.id.btnEmployees);
        btnCosts = findViewById(R.id.btnCosts);
        btnExpenses = findViewById(R.id.btnExpenses);

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
        
        // Grid Click Listeners
        View.OnClickListener gridClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "";
                if (v.getId() == R.id.btnInventory) text = "Inventario";
                else if (v.getId() == R.id.btnSales) text = "Ventas";
                else if (v.getId() == R.id.btnCustomers) text = "Clientes";
                else if (v.getId() == R.id.btnEmployees) text = "Empleado";
                else if (v.getId() == R.id.btnCosts) text = "Costos";
                else if (v.getId() == R.id.btnExpenses) text = "Gastos";
                
                Toast.makeText(DashboardActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        };
        
        btnInventory.setOnClickListener(gridClickListener);
        btnSales.setOnClickListener(gridClickListener);
        btnCustomers.setOnClickListener(gridClickListener);
        btnEmployees.setOnClickListener(gridClickListener);
        btnCosts.setOnClickListener(gridClickListener);
        btnExpenses.setOnClickListener(gridClickListener);
        
        // Setup Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_inventory) {
                Toast.makeText(this, "Inventario", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_profile) {
                Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }
}