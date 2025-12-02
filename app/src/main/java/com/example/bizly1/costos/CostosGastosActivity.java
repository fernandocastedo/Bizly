package com.example.bizly1.costos;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.costos.adapters.CostoGastoAdapter;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.models.CostoGasto;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CostosGastosActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MaterialButton btnCostos, btnGastos, btnSueldos, btnAdelantos, btnTodos;
    private RecyclerView recyclerView;
    private TextView txtEmpty, txtTotal;
    private FloatingActionButton fabAgregar;
    
    private DBHelper dbHelper;
    private CostoGastoAdapter adapter;
    private List<CostoGasto> costosGastos;
    private String tipoFiltro = "todos"; // "todos", "costo", "gasto", "sueldo", "adelanto"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costos_gastos);

        dbHelper = new DBHelper(this);

        inicializarVistas();
        configurarToolbar();
        configurarRecyclerView();
        configurarBotones();
        cargarDatos();
    }

    private void inicializarVistas() {
        toolbar = findViewById(R.id.toolbar);
        btnCostos = findViewById(R.id.btnCostos);
        btnGastos = findViewById(R.id.btnGastos);
        btnSueldos = findViewById(R.id.btnSueldos);
        btnAdelantos = findViewById(R.id.btnAdelantos);
        btnTodos = findViewById(R.id.btnTodos);
        recyclerView = findViewById(R.id.recyclerView);
        txtEmpty = findViewById(R.id.txtEmpty);
        txtTotal = findViewById(R.id.txtTotal);
        fabAgregar = findViewById(R.id.fabAgregar);
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Costos y Gastos");
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
        adapter = new CostoGastoAdapter(costosGastos);
        adapter.setOnItemClickListener(costoGasto -> {
            Intent intent = new Intent(this, CostoGastoFormActivity.class);
            intent.putExtra("costo_gasto_id", costoGasto.getId());
            intent.putExtra("tipo", costoGasto.getTipo());
            startActivity(intent);
        });
        
        adapter.setOnDeleteClickListener(costoGasto -> {
            dbHelper.eliminarCostoGasto(costoGasto.getId());
            cargarDatos();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, CostoGastoFormActivity.class);
            intent.putExtra("tipo", tipoFiltro.equals("todos") ? null : tipoFiltro);
            startActivity(intent);
        });
    }

    private void configurarBotones() {
        // Botón Todos
        btnTodos.setOnClickListener(v -> {
            tipoFiltro = "todos";
            actualizarBotones();
            cargarDatos();
        });

        // Botón Costos
        btnCostos.setOnClickListener(v -> {
            tipoFiltro = "costo";
            actualizarBotones();
            cargarDatos();
        });

        // Botón Gastos
        btnGastos.setOnClickListener(v -> {
            tipoFiltro = "gasto";
            actualizarBotones();
            cargarDatos();
        });

        // Botón Sueldos
        btnSueldos.setOnClickListener(v -> {
            tipoFiltro = "sueldo";
            actualizarBotones();
            cargarDatos();
        });

        // Botón Adelantos
        btnAdelantos.setOnClickListener(v -> {
            tipoFiltro = "adelanto";
            actualizarBotones();
            cargarDatos();
        });

        // Estado inicial
        actualizarBotones();
    }

    private void actualizarBotones() {
        // Resetear todos los botones
        btnTodos.setBackgroundTintList(null);
        btnCostos.setBackgroundTintList(null);
        btnGastos.setBackgroundTintList(null);
        btnSueldos.setBackgroundTintList(null);
        btnAdelantos.setBackgroundTintList(null);

        // Activar el botón seleccionado
        ColorStateList primaryColor = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary_color));
        int whiteColor = ContextCompat.getColor(this, android.R.color.white);
        int blackColor = ContextCompat.getColor(this, android.R.color.black);

        switch (tipoFiltro) {
            case "todos":
                btnTodos.setBackgroundTintList(primaryColor);
                btnTodos.setTextColor(whiteColor);
                break;
            case "costo":
                btnCostos.setBackgroundTintList(primaryColor);
                btnCostos.setTextColor(whiteColor);
                break;
            case "gasto":
                btnGastos.setBackgroundTintList(primaryColor);
                btnGastos.setTextColor(whiteColor);
                break;
            case "sueldo":
                btnSueldos.setBackgroundTintList(primaryColor);
                btnSueldos.setTextColor(whiteColor);
                break;
            case "adelanto":
                btnAdelantos.setBackgroundTintList(primaryColor);
                btnAdelantos.setTextColor(whiteColor);
                break;
        }

        // Resetear colores de texto de los demás
        if (!tipoFiltro.equals("todos")) {
            btnTodos.setTextColor(blackColor);
        }
        if (!tipoFiltro.equals("costo")) {
            btnCostos.setTextColor(blackColor);
        }
        if (!tipoFiltro.equals("gasto")) {
            btnGastos.setTextColor(blackColor);
        }
        if (!tipoFiltro.equals("sueldo")) {
            btnSueldos.setTextColor(blackColor);
        }
        if (!tipoFiltro.equals("adelanto")) {
            btnAdelantos.setTextColor(blackColor);
        }
    }

    private void cargarDatos() {
        if (tipoFiltro.equals("todos")) {
            costosGastos = dbHelper.obtenerTodosCostosGastos();
        } else {
            costosGastos = dbHelper.obtenerCostosGastosPorTipo(tipoFiltro);
        }
        
        adapter.updateList(costosGastos);
        actualizarEmptyMessage();
        actualizarTotal();
    }

    private void actualizarEmptyMessage() {
        if (costosGastos.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void actualizarTotal() {
        double total = 0.0;
        for (CostoGasto cg : costosGastos) {
            if (cg.getMonto() != null) {
                total += cg.getMonto();
            }
        }
        txtTotal.setText(String.format("Total: Bs. %.2f", total));
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();
    }
}

