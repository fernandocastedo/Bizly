package com.example.bizly1.sucursales;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.models.Sucursal;
import com.example.bizly1.sucursales.adapters.SucursalAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SucursalesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSucursales;
    private Toolbar toolbar;
    private FloatingActionButton fabAgregar;
    private DBHelper dbHelper;
    private SucursalAdapter adapter;
    private List<Sucursal> sucursales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursales);

        dbHelper = new DBHelper(this);

        inicializarVistas();
        configurarToolbar();
        configurarRecyclerView();
        cargarSucursales();
    }

    private void inicializarVistas() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewSucursales = findViewById(R.id.recyclerViewSucursales);
        fabAgregar = findViewById(R.id.fabAgregar);
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Sucursales");
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

        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, SucursalFormActivity.class);
            startActivity(intent);
        });
    }

    private void cargarSucursales() {
        sucursales = dbHelper.obtenerTodasSucursales();
        adapter.updateList(sucursales);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarSucursales();
    }
}

