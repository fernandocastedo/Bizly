package com.example.bizly1.productos;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.data.utils.SyncManager;
import com.example.bizly1.models.ProductoVenta;
import com.example.bizly1.productos.adapters.ProductoVentaAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ProductosVentaActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProductos;
    private ProductoVentaAdapter adapter;
    private DBHelper dbHelper;
    private SyncManager syncManager;
    
    private List<ProductoVenta> listaProductos = new ArrayList<>();
    private List<ProductoVenta> listaProductosCompleta = new ArrayList<>();
    
    private TextInputEditText etBuscar;
    private TextView txtEmpty;
    private MaterialButton btnAgregar; // Changed from FAB
    private ImageButton btnBack; // Added
    
    private boolean mostrarSoloActivos = false;
    private boolean mostrarSoloConStock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_venta);

        dbHelper = new DBHelper(this);
        syncManager = new SyncManager(this);

        inicializarVistas();
        
        btnBack.setOnClickListener(v -> finish());
        
        configurarListeners();
        cargarProductos();

        // Verificar conexión y sincronizar si hay internet
        if (NetworkUtils.isNetworkAvailable(this)) {
            sincronizarDesdeAPI();
            syncManager.sincronizarPendientes();
        } else {
            mostrarMensajeSinConexion();
        }
    }

    private void inicializarVistas() {
        btnBack = findViewById(R.id.btnBack);
        recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
        etBuscar = findViewById(R.id.etBuscar);
        txtEmpty = findViewById(R.id.txtEmpty);
        btnAgregar = findViewById(R.id.btnAgregar);

        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new ProductoVentaAdapter(listaProductos, new ProductoVentaAdapter.OnProductoVentaClickListener() {
            @Override
            public void onProductoVentaClick(ProductoVenta productoVenta) {
                // Mostrar detalles (opcional)
            }

            @Override
            public void onEditClick(ProductoVenta productoVenta) {
                editarProductoVenta(productoVenta);
            }

            @Override
            public void onDeleteClick(ProductoVenta productoVenta) {
                confirmarEliminar(productoVenta);
            }

            @Override
            public void onDesactivarClick(ProductoVenta productoVenta) {
                desactivarProductoVenta(productoVenta);
            }
        });
        
        recyclerViewProductos.setAdapter(adapter);
    }


    private void configurarListeners() {
        btnAgregar.setOnClickListener(v -> {
            abrirFormulario();
        });

        // Listener para el buscador
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarProductos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void cargarProductos() {
        // Cargar desde SQLite
        listaProductosCompleta = dbHelper.obtenerTodosProductosVenta();
        // Aplicar filtro actual si existe
        String textoBusqueda = etBuscar != null ? etBuscar.getText().toString() : "";
        aplicarFiltros(textoBusqueda);
    }
    
    private void aplicarFiltros(String textoBusqueda) {
        listaProductos.clear();
        
        for (ProductoVenta producto : listaProductosCompleta) {
            boolean coincide = true;
            
            // Filtro por texto
            if (textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {
                String textoLower = textoBusqueda.toLowerCase().trim();
                boolean coincideTexto = (producto.getNombre() != null && 
                    producto.getNombre().toLowerCase().contains(textoLower)) ||
                    (producto.getDescripcion() != null && 
                    producto.getDescripcion().toLowerCase().contains(textoLower));
                if (!coincideTexto) {
                    coincide = false;
                }
            }
            
            // Filtro por activos
            if (mostrarSoloActivos) {
                if (producto.getActivo() == null || !producto.getActivo()) {
                    coincide = false;
                }
            }
            
            // Filtro por stock
            if (mostrarSoloConStock) {
                if (!producto.tieneStockSuficiente()) {
                    coincide = false;
                }
            }
            
            if (coincide) {
                listaProductos.add(producto);
            }
        }
        
        adapter.updateList(listaProductos);
        actualizarMensajeVacio();
    }
    
    private void filtrarProductos(String textoBusqueda) {
        aplicarFiltros(textoBusqueda);
    }

    private void actualizarMensajeVacio() {
        if (listaProductos.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerViewProductos.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerViewProductos.setVisibility(View.VISIBLE);
        }
    }

    private void sincronizarDesdeAPI() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            return;
        }

        syncManager.sincronizarProductosVenta();
    }

    private void mostrarMensajeSinConexion() {
        Toast.makeText(this,
                "No estás conectado a internet. Tus datos se guardarán de forma local y se subirán cuando te conectes a internet!",
                Toast.LENGTH_LONG).show();
    }

    private void abrirFormulario() {
        android.content.Intent intent = new android.content.Intent(this, ProductoVentaFormActivity.class);
        startActivity(intent);
    }

    private void editarProductoVenta(ProductoVenta productoVenta) {
        android.content.Intent intent = new android.content.Intent(this, ProductoVentaFormActivity.class);
        intent.putExtra("producto_venta_id", productoVenta.getId());
        startActivity(intent);
    }

    private void confirmarEliminar(ProductoVenta productoVenta) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Está seguro de que desea eliminar el producto: " + productoVenta.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarProductoVenta(productoVenta))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarProductoVenta(ProductoVenta productoVenta) {
        // Eliminar localmente primero
        dbHelper.eliminarProductoVenta(productoVenta.getId());
        cargarProductos();

        // Intentar sincronizar con API
        if (NetworkUtils.isNetworkAvailable(this)) {
            if (productoVenta.getServerId() != null) {
                // TODO: Llamar a API para eliminar
                syncManager.agregarASyncQueue("ProductoVenta", "DELETE", productoVenta.getId(), productoVenta);
            }
            Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
        } else {
            syncManager.agregarASyncQueue("ProductoVenta", "DELETE", productoVenta.getId(), productoVenta);
            Toast.makeText(this, "Producto eliminado localmente. Se sincronizará cuando haya conexión.", Toast.LENGTH_LONG).show();
        }
    }

    private void desactivarProductoVenta(ProductoVenta productoVenta) {
        boolean nuevoEstado = productoVenta.getActivo() == null || !productoVenta.getActivo();
        productoVenta.setActivo(nuevoEstado);
        
        // Actualizar localmente
        dbHelper.actualizarProductoVenta(productoVenta);
        cargarProductos();

        // Intentar sincronizar con API
        if (NetworkUtils.isNetworkAvailable(this)) {
            if (productoVenta.getServerId() != null) {
                syncManager.agregarASyncQueue("ProductoVenta", "UPDATE", productoVenta.getId(), productoVenta);
            }
        } else {
            syncManager.agregarASyncQueue("ProductoVenta", "UPDATE", productoVenta.getId(), productoVenta);
        }
        
        Toast.makeText(this, "Producto " + (nuevoEstado ? "activado" : "desactivado"), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarProductos();
        // Si hay conexión, intentar sincronizar pendientes
        if (NetworkUtils.isNetworkAvailable(this)) {
            syncManager.sincronizarPendientes();
        }
    }
}

