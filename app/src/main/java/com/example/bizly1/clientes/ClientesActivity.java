package com.example.bizly1.clientes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.clientes.adapters.ClienteAdapter;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.models.Cliente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ClientesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerViewClientes;
    private TextView txtEmpty;
    private FloatingActionButton fabAgregar;
    
    private DBHelper dbHelper;
    private ClienteAdapter adapter;
    private List<Cliente> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        dbHelper = new DBHelper(this);

        inicializarVistas();
        configurarToolbar();
        configurarRecyclerView();
        cargarClientes();
    }

    private void inicializarVistas() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewClientes = findViewById(R.id.recyclerViewClientes);
        txtEmpty = findViewById(R.id.txtEmpty);
        fabAgregar = findViewById(R.id.fabAgregar);
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Clientes");
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
        adapter = new ClienteAdapter(clientes);
        adapter.setOnItemClickListener(cliente -> {
            // Editar cliente
            Intent intent = new Intent(this, ClienteFormActivity.class);
            intent.putExtra("cliente_id", cliente.getId());
            startActivity(intent);
        });

        recyclerViewClientes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClientes.setAdapter(adapter);

        fabAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClienteFormActivity.class);
            startActivity(intent);
        });
    }

    private void cargarClientes() {
        clientes = dbHelper.obtenerTodosClientes();
        adapter.updateList(clientes);
        actualizarEmptyMessage();
    }

    private void actualizarEmptyMessage() {
        if (clientes.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerViewClientes.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerViewClientes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarClientes();
    }
}

