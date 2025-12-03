package com.example.bizly1.sucursales;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.models.Sucursal;
import com.example.bizly1.sucursales.adapters.SucursalAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class SucursalesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSucursales;
    private MaterialButton btnAgregar; // Changed from FAB
    private ImageButton btnBack; // Added
    private TextView txtEmpty; // Added
    private DBHelper dbHelper;
    private SucursalAdapter adapter;
    private List<Sucursal> sucursales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursales);

        dbHelper = new DBHelper(this);

        inicializarVistas();
        
        btnBack.setOnClickListener(v -> finish());
        
        configurarRecyclerView();
        cargarSucursales();
    }

    private void inicializarVistas() {
        btnBack = findViewById(R.id.btnBack);
        recyclerViewSucursales = findViewById(R.id.recyclerViewSucursales);
        btnAgregar = findViewById(R.id.btnAgregar);
        txtEmpty = findViewById(R.id.txtEmpty);
    }


    private void configurarRecyclerView() {
        adapter = new SucursalAdapter(sucursales);
        adapter.setOnItemClickListener(sucursal -> {
            // Editar sucursal
            Intent intent = new Intent(this, SucursalFormActivity.class);
            intent.putExtra("sucursal_id", sucursal.getId());
            startActivity(intent);
        });

        recyclerViewSucursales.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSucursales.setAdapter(adapter);

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, SucursalFormActivity.class);
            startActivity(intent);
        });
    }

    private void cargarSucursales() {
        sucursales = dbHelper.obtenerTodasSucursales();
        adapter.updateList(sucursales);
        
        if (sucursales.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerViewSucursales.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerViewSucursales.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarSucursales();
    }
}

