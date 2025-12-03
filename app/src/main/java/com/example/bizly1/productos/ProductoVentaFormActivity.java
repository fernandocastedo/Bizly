package com.example.bizly1.productos;

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
import com.example.bizly1.data.network.ApiClient;
import com.example.bizly1.data.network.ApiService;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.data.utils.SyncManager;
import com.example.bizly1.models.Insumo;
import com.example.bizly1.models.ProductoVenta;
import com.example.bizly1.models.ProductoVentaInsumo;
import com.example.bizly1.productos.adapters.InsumoSeleccionadoAdapter;
import com.example.bizly1.productos.dialogs.SeleccionarInsumoDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoVentaFormActivity extends AppCompatActivity {

    private TextInputEditText etNombre, etDescripcion, etPrecioVenta;
    private TextView txtCostoTotal, txtPrecioSugerido, txtEmptyInsumos, headerTitle;
    private RecyclerView recyclerViewInsumos;
    private MaterialButton btnGuardar, btnAgregarInsumo;
    private ImageButton btnBack;
    
    private DBHelper dbHelper;
    private SyncManager syncManager;
    private ApiService apiService;
    
    private Integer productoVentaId = null; // null = nuevo, != null = edición
    private Integer empresaId = 1; // TODO: Obtener del usuario logueado
    private Double margenGananciaEmpresa = 30.0; // TODO: Obtener de la configuración del emprendimiento
    
    private List<ProductoVentaInsumo> insumosSeleccionados = new ArrayList<>();
    private InsumoSeleccionadoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_venta_form);

        dbHelper = new DBHelper(this);
        syncManager = new SyncManager(this);
        apiService = ApiClient.getApiService();

        // Obtener ID si viene de edición
        productoVentaId = getIntent().getIntExtra("producto_venta_id", -1);
        if (productoVentaId == -1) {
            productoVentaId = null;
        }

        inicializarVistas();
        configurarListeners();
        configurarAdapter();

        if (productoVentaId != null) {
            headerTitle.setText("Editar Producto");
            cargarProductoVenta();
        } else {
            headerTitle.setText("Nuevo Producto");
            actualizarResumen();
        }
    }

    private void inicializarVistas() {
        headerTitle = findViewById(R.id.headerTitle);
        btnBack = findViewById(R.id.btnBack);
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecioVenta = findViewById(R.id.etPrecioVenta);
        txtCostoTotal = findViewById(R.id.txtCostoTotal);
        txtPrecioSugerido = findViewById(R.id.txtPrecioSugerido);
        txtEmptyInsumos = findViewById(R.id.txtEmptyInsumos);
        recyclerViewInsumos = findViewById(R.id.recyclerViewInsumos);
        btnAgregarInsumo = findViewById(R.id.btnAgregarInsumo);
        btnGuardar = findViewById(R.id.btnGuardar);
    }


    private void configurarAdapter() {
        adapter = new InsumoSeleccionadoAdapter(insumosSeleccionados);
        adapter.setOnDeleteClickListener(position -> {
            insumosSeleccionados.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, insumosSeleccionados.size());
            actualizarResumen();
            actualizarEmptyMessage();
        });
        
        recyclerViewInsumos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewInsumos.setAdapter(adapter);
    }

    private void configurarListeners() {
        btnGuardar.setOnClickListener(v -> guardarProductoVenta());
        btnBack.setOnClickListener(v -> finish());
        btnAgregarInsumo.setOnClickListener(v -> mostrarDialogoSeleccionarInsumo());

        // Calcular precio sugerido cuando cambia el costo
        TextWatcher calcularWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarResumen();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etPrecioVenta.addTextChangedListener(calcularWatcher);
    }

    private void mostrarDialogoSeleccionarInsumo() {
        // Obtener IDs de insumos ya seleccionados
        List<Integer> idsYaSeleccionados = new ArrayList<>();
        for (ProductoVentaInsumo pvi : insumosSeleccionados) {
            if (pvi.getInsumoId() != null) {
                idsYaSeleccionados.add(pvi.getInsumoId());
            }
        }

        SeleccionarInsumoDialog.show(
                this,
                dbHelper,
                idsYaSeleccionados,
                (insumo, cantidad) -> {
                    // Agregar insumo a la lista
                    ProductoVentaInsumo pvi = new ProductoVentaInsumo();
                    pvi.setInsumoId(insumo.getId());
                    pvi.setCantidadUsada(cantidad);
                    pvi.setInsumo(insumo);
                    
                    insumosSeleccionados.add(pvi);
                    adapter.notifyItemInserted(insumosSeleccionados.size() - 1);
                    actualizarResumen();
                    actualizarEmptyMessage();
                }
        );
    }

    private void actualizarResumen() {
        // Calcular costo total
        double costoTotal = 0.0;
        for (ProductoVentaInsumo pvi : insumosSeleccionados) {
            costoTotal += pvi.calcularCosto();
        }
        
        txtCostoTotal.setText(String.format(Locale.getDefault(), "Bs. %.2f", costoTotal));
        
        // Calcular precio sugerido
        double precioSugerido = costoTotal * (1 + (margenGananciaEmpresa / 100));
        txtPrecioSugerido.setText(String.format(Locale.getDefault(), "Bs. %.2f", precioSugerido));
        
        // Si el campo de precio está vacío, sugerir el precio calculado
        if (etPrecioVenta.getText().toString().trim().isEmpty()) {
            etPrecioVenta.setText(String.format(Locale.getDefault(), "%.2f", precioSugerido));
        }
    }

    private void actualizarEmptyMessage() {
        if (insumosSeleccionados.isEmpty()) {
            txtEmptyInsumos.setVisibility(View.VISIBLE);
            recyclerViewInsumos.setVisibility(View.GONE);
        } else {
            txtEmptyInsumos.setVisibility(View.GONE);
            recyclerViewInsumos.setVisibility(View.VISIBLE);
        }
    }

    private void cargarProductoVenta() {
        if (productoVentaId == null) return;

        ProductoVenta pv = dbHelper.obtenerProductoVenta(productoVentaId);
        if (pv != null) {
            etNombre.setText(pv.getNombre());
            etDescripcion.setText(pv.getDescripcion());
            if (pv.getPrecioVenta() != null) {
                etPrecioVenta.setText(String.format(Locale.getDefault(), "%.2f", pv.getPrecioVenta()));
            }
            
            // Cargar insumos
            if (pv.getInsumos() != null) {
                insumosSeleccionados.clear();
                insumosSeleccionados.addAll(pv.getInsumos());
                adapter.notifyDataSetChanged();
                actualizarEmptyMessage();
            }
            
            actualizarResumen();
        }
    }

    private void guardarProductoVenta() {
        // Validaciones
        String nombre = etNombre.getText().toString().trim();
        if (nombre.isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            etNombre.requestFocus();
            return;
        }

        if (insumosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Debe agregar al menos un insumo", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar stock de todos los insumos
        for (ProductoVentaInsumo pvi : insumosSeleccionados) {
            if (pvi.getInsumo() != null) {
                if (pvi.getInsumo().getCantidad() == null || 
                    pvi.getInsumo().getCantidad() < pvi.getCantidadUsada()) {
                    Toast.makeText(this, 
                        "No hay suficiente stock de " + pvi.getInsumo().getNombre() + 
                        ". Disponible: " + (pvi.getInsumo().getCantidad() != null ? pvi.getInsumo().getCantidad() : 0),
                        Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        String precioVentaStr = etPrecioVenta.getText().toString().trim();
        if (precioVentaStr.isEmpty()) {
            etPrecioVenta.setError("El precio de venta es obligatorio");
            etPrecioVenta.requestFocus();
            return;
        }

        double precioVenta;
        try {
            precioVenta = Double.parseDouble(precioVentaStr);
            if (precioVenta <= 0) {
                etPrecioVenta.setError("El precio debe ser mayor a 0");
                etPrecioVenta.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etPrecioVenta.setError("Precio inválido");
            etPrecioVenta.requestFocus();
            return;
        }

        // Calcular costo total
        double costoTotal = 0.0;
        for (ProductoVentaInsumo pvi : insumosSeleccionados) {
            costoTotal += pvi.calcularCosto();
        }

        // Crear objeto ProductoVenta
        ProductoVenta productoVenta = new ProductoVenta();
        if (productoVentaId != null) {
            productoVenta.setId(productoVentaId);
        }
        productoVenta.setEmpresaId(empresaId);
        productoVenta.setNombre(nombre);
        productoVenta.setDescripcion(etDescripcion.getText().toString().trim());
        productoVenta.setPrecioVenta(precioVenta);
        productoVenta.setCostoTotal(costoTotal);
        productoVenta.setMargenGanancia(margenGananciaEmpresa);
        productoVenta.setActivo(true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String now = sdf.format(new Date());
        
        if (productoVentaId == null) {
            productoVenta.setCreatedAt(now);
        }
        productoVenta.setUpdatedAt(now);

        // Guardar localmente primero
        if (productoVentaId == null) {
            long id = dbHelper.insertarProductoVenta(productoVenta);
            productoVenta.setId((int) id);
        } else {
            // Eliminar insumos antiguos
            dbHelper.eliminarInsumosDeProductoVenta(productoVentaId);
            dbHelper.actualizarProductoVenta(productoVenta);
        }

        // Guardar insumos asociados
        for (ProductoVentaInsumo pvi : insumosSeleccionados) {
            pvi.setProductoVentaId(productoVenta.getId());
            dbHelper.insertarProductoVentaInsumo(pvi);
        }

        // Intentar sincronizar con API
        if (NetworkUtils.isNetworkAvailable(this)) {
            if (productoVentaId == null) {
                // Crear nuevo
                apiService.crearProductoVenta(productoVenta).enqueue(new Callback<ProductoVenta>() {
                    @Override
                    public void onResponse(Call<ProductoVenta> call, Response<ProductoVenta> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ProductoVenta pvServer = response.body();
                            pvServer.setId(productoVenta.getId());
                            pvServer.setServerId(pvServer.getId());
                            pvServer.setSyncStatus("synced");
                            dbHelper.actualizarProductoVenta(pvServer);
                            Toast.makeText(ProductoVentaFormActivity.this, "Producto guardado y sincronizado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductoVenta> call, Throwable t) {
                        syncManager.agregarASyncQueue("ProductoVenta", "INSERT", productoVenta.getId(), productoVenta);
                        Toast.makeText(ProductoVentaFormActivity.this, "Producto guardado localmente. Se sincronizará cuando haya conexión.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            } else {
                // Actualizar existente
                if (productoVenta.getServerId() != null) {
                    apiService.actualizarProductoVenta(productoVenta.getServerId(), productoVenta).enqueue(new Callback<ProductoVenta>() {
                        @Override
                        public void onResponse(Call<ProductoVenta> call, Response<ProductoVenta> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ProductoVenta pvServer = response.body();
                                pvServer.setId(productoVenta.getId());
                                pvServer.setServerId(pvServer.getId());
                                pvServer.setSyncStatus("synced");
                                dbHelper.actualizarProductoVenta(pvServer);
                                Toast.makeText(ProductoVentaFormActivity.this, "Producto actualizado y sincronizado", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductoVenta> call, Throwable t) {
                            syncManager.agregarASyncQueue("ProductoVenta", "UPDATE", productoVenta.getId(), productoVenta);
                            Toast.makeText(ProductoVentaFormActivity.this, "Producto actualizado localmente. Se sincronizará cuando haya conexión.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                } else {
                    // No tiene server_id, intentar crear
                    apiService.crearProductoVenta(productoVenta).enqueue(new Callback<ProductoVenta>() {
                        @Override
                        public void onResponse(Call<ProductoVenta> call, Response<ProductoVenta> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ProductoVenta pvServer = response.body();
                                pvServer.setId(productoVenta.getId());
                                pvServer.setServerId(pvServer.getId());
                                pvServer.setSyncStatus("synced");
                                dbHelper.actualizarProductoVenta(pvServer);
                                Toast.makeText(ProductoVentaFormActivity.this, "Producto guardado y sincronizado", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductoVenta> call, Throwable t) {
                            syncManager.agregarASyncQueue("ProductoVenta", "UPDATE", productoVenta.getId(), productoVenta);
                            Toast.makeText(ProductoVentaFormActivity.this, "Producto actualizado localmente. Se sincronizará cuando haya conexión.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
            }
        } else {
            // Offline: agregar a cola de sincronización
            syncManager.agregarASyncQueue("ProductoVenta", productoVentaId == null ? "INSERT" : "UPDATE", productoVenta.getId(), productoVenta);
            Toast.makeText(this, "Producto guardado localmente. Se sincronizará cuando haya conexión.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}

