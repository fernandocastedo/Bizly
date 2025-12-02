package com.example.bizly1.inventario;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.data.utils.SyncManager;
import com.example.bizly1.inventario.adapters.InsumoAdapter;
import com.example.bizly1.models.Insumo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewInsumos;
    private InsumoAdapter adapter;
    private DBHelper dbHelper;
    private SyncManager syncManager;
    
    private List<Insumo> listaInsumos = new ArrayList<>();
    private List<Insumo> listaInsumosCompleta = new ArrayList<>();
    
    private TextInputEditText etBuscar;
    private TextView txtEmpty;
    private FloatingActionButton fabAgregar, fabEscanear;
    private Toolbar toolbar;
    
    private String categoriaFiltro = null;
    private boolean mostrarStockBajo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        dbHelper = new DBHelper(this);
        syncManager = new SyncManager(this);

        inicializarVistas();
        configurarToolbar();
        configurarListeners();
        cargarInsumos();

        // Verificar conexión y sincronizar si hay internet
        if (NetworkUtils.isNetworkAvailable(this)) {
            sincronizarDesdeAPI();
            syncManager.sincronizarPendientes();
        } else {
            mostrarMensajeSinConexion();
        }
    }

    private void inicializarVistas() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewInsumos = findViewById(R.id.recyclerViewInsumos);
        etBuscar = findViewById(R.id.etBuscar);
        txtEmpty = findViewById(R.id.txtEmpty);
        fabAgregar = findViewById(R.id.fabAgregar);
        fabEscanear = findViewById(R.id.fabEscanear);

        recyclerViewInsumos.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new InsumoAdapter(listaInsumos, new InsumoAdapter.OnInsumoClickListener() {
            @Override
            public void onInsumoClick(Insumo insumo) {
                // Mostrar detalles (opcional)
            }

            @Override
            public void onEditClick(Insumo insumo) {
                editarInsumo(insumo);
            }

            @Override
            public void onDeleteClick(Insumo insumo) {
                confirmarEliminar(insumo);
            }

            @Override
            public void onRestockClick(Insumo insumo) {
                mostrarDialogoRestock(insumo);
            }
        });
        
        recyclerViewInsumos.setAdapter(adapter);
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Inventario");
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

    private void configurarListeners() {
        fabAgregar.setOnClickListener(v -> {
            abrirFormularioManual();
        });

        fabEscanear.setOnClickListener(v -> {
            abrirCamaraEscaneo();
        });

        // Listener para el buscador
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarInsumos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void cargarInsumos() {
        // Cargar desde SQLite
        listaInsumosCompleta = dbHelper.obtenerTodosInsumos();
        // Aplicar filtro actual si existe
        String textoBusqueda = etBuscar != null ? etBuscar.getText().toString() : "";
        aplicarFiltros(textoBusqueda);
    }
    
    private void aplicarFiltros(String textoBusqueda) {
        listaInsumos.clear();
        
        for (Insumo insumo : listaInsumosCompleta) {
            boolean coincide = true;
            
            // Filtro por texto
            if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
                String textoLower = textoBusqueda.toLowerCase().trim();
                boolean coincideTexto = (insumo.getNombre() != null && 
                    insumo.getNombre().toLowerCase().contains(textoLower)) ||
                    (insumo.getCategoria() != null && 
                    insumo.getCategoria().toLowerCase().contains(textoLower));
                if (!coincideTexto) {
                    coincide = false;
                }
            }
            
            // Filtro por categoría
            if (categoriaFiltro != null && !categoriaFiltro.isEmpty()) {
                if (!categoriaFiltro.equals(insumo.getCategoria())) {
                    coincide = false;
                }
            }
            
            // Filtro por stock bajo
            if (mostrarStockBajo) {
                if (!insumo.isStockBajo()) {
                    coincide = false;
                }
            }
            
            if (coincide) {
                listaInsumos.add(insumo);
            }
        }
        
        adapter.updateList(listaInsumos);
        actualizarMensajeVacio();
    }
    
    private void filtrarInsumos(String textoBusqueda) {
        aplicarFiltros(textoBusqueda);
    }

    private void actualizarMensajeVacio() {
        if (listaInsumos.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerViewInsumos.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerViewInsumos.setVisibility(View.VISIBLE);
        }
    }

    private void sincronizarDesdeAPI() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            return;
        }

        syncManager.sincronizarInsumos();
    }

    private void mostrarMensajeSinConexion() {
        Toast.makeText(this,
                "No estás conectado a internet. Tus datos se guardarán de forma local y se subirán cuando te conectes a internet!",
                Toast.LENGTH_LONG).show();
    }

    private void abrirFormularioManual() {
        android.content.Intent intent = new android.content.Intent(this, ItemFormActivity.class);
        startActivity(intent);
    }

    private void abrirCamaraEscaneo() {
        android.content.Intent intent = new android.content.Intent(this, CameraScanActivity.class);
        startActivity(intent);
    }

    private void editarInsumo(Insumo insumo) {
        android.content.Intent intent = new android.content.Intent(this, ItemFormActivity.class);
        intent.putExtra("insumo_id", insumo.getId());
        startActivity(intent);
    }

    private void confirmarEliminar(Insumo insumo) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Está seguro de que desea eliminar el insumo: " + insumo.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarInsumo(insumo))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarInsumo(Insumo insumo) {
        // Eliminar localmente primero
        dbHelper.eliminarInsumo(insumo.getId());
        cargarInsumos();

        // Intentar sincronizar con API
        if (NetworkUtils.isNetworkAvailable(this)) {
            if (insumo.getServerId() != null) {
                // TODO: Llamar a API para eliminar
                // Por ahora agregar a sync queue
                syncManager.agregarASyncQueue("Insumo", "DELETE", insumo.getId(), insumo);
            }
            Toast.makeText(this, "Insumo eliminado", Toast.LENGTH_SHORT).show();
        } else {
            // Offline: agregar a cola de sincronización
            syncManager.agregarASyncQueue("Insumo", "DELETE", insumo.getId(), insumo);
            Toast.makeText(this, "Insumo eliminado localmente. Se sincronizará cuando haya conexión.", Toast.LENGTH_LONG).show();
        }
    }

    private void mostrarDialogoRestock(Insumo insumo) {
        // Abrir cámara en modo re-stock
        android.content.Intent intent = new android.content.Intent(this, CameraScanActivity.class);
        intent.putExtra("modo_restock", true);
        intent.putExtra("insumo_id", insumo.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarInsumos();
        // Si hay conexión, intentar sincronizar pendientes
        if (NetworkUtils.isNetworkAvailable(this)) {
            syncManager.sincronizarPendientes();
        }
    }
}

