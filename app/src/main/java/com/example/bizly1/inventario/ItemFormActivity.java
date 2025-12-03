package com.example.bizly1.inventario;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.data.network.ApiClient;
import com.example.bizly1.data.network.ApiService;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.data.utils.SyncManager;
import com.example.bizly1.models.Insumo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemFormActivity extends AppCompatActivity {

    private TextInputEditText etNombre, etDescripcion, etCantidad, etUnidad, 
                              etPrecioUnitario, etPrecioTotal, etCategoria, etStockMinimo;
    private MaterialButton btnGuardar;
    private ImageButton btnBack;
    private TextView headerTitle;
    
    private DBHelper dbHelper;
    private SyncManager syncManager;
    private ApiService apiService;
    
    private Integer insumoId = null; // null = nuevo, != null = edición
    private Integer empresaId = 1; // TODO: Obtener del usuario logueado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form);

        dbHelper = new DBHelper(this);
        syncManager = new SyncManager(this);
        apiService = ApiClient.getApiService();

        // Obtener ID si viene de edición
        insumoId = getIntent().getIntExtra("insumo_id", -1);
        if (insumoId == -1) {
            insumoId = null;
        }

        inicializarVistas();
        configurarListeners();

        if (insumoId != null) {
            headerTitle.setText("Editar Insumo");
            cargarInsumo();
        } else {
            headerTitle.setText("Nuevo Insumo");
        }
    }

    private void inicializarVistas() {
        headerTitle = findViewById(R.id.headerTitle);
        btnBack = findViewById(R.id.btnBack);
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etCantidad = findViewById(R.id.etCantidad);
        etUnidad = findViewById(R.id.etUnidad);
        etPrecioUnitario = findViewById(R.id.etPrecioUnitario);
        etPrecioTotal = findViewById(R.id.etPrecioTotal);
        etCategoria = findViewById(R.id.etCategoria);
        etStockMinimo = findViewById(R.id.etStockMinimo);
        btnGuardar = findViewById(R.id.btnGuardar);
    }


    private void configurarListeners() {
        btnGuardar.setOnClickListener(v -> guardarInsumo());
        btnBack.setOnClickListener(v -> finish());

        // Calcular precio total automáticamente
        TextWatcher calcularPrecioWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcularPrecioTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etCantidad.addTextChangedListener(calcularPrecioWatcher);
        etPrecioUnitario.addTextChangedListener(calcularPrecioWatcher);
    }

    private void calcularPrecioTotal() {
        try {
            String cantidadStr = etCantidad.getText().toString().trim();
            String precioUnitarioStr = etPrecioUnitario.getText().toString().trim();

            if (!cantidadStr.isEmpty() && !precioUnitarioStr.isEmpty()) {
                double cantidad = Double.parseDouble(cantidadStr);
                double precioUnitario = Double.parseDouble(precioUnitarioStr);
                double precioTotal = cantidad * precioUnitario;
                etPrecioTotal.setText(String.format(Locale.getDefault(), "%.2f", precioTotal));
            } else {
                etPrecioTotal.setText("");
            }
        } catch (NumberFormatException e) {
            etPrecioTotal.setText("");
        }
    }

    private void cargarInsumo() {
        if (insumoId == null) return;

        Insumo insumo = dbHelper.obtenerInsumo(insumoId);
        if (insumo != null) {
            etNombre.setText(insumo.getNombre());
            etDescripcion.setText(insumo.getDescripcion());
            etCantidad.setText(String.format(Locale.getDefault(), "%.2f", insumo.getCantidad()));
            etUnidad.setText(insumo.getUnidadMedida());
            if (insumo.getPrecioUnitario() != null) {
                etPrecioUnitario.setText(String.format(Locale.getDefault(), "%.2f", insumo.getPrecioUnitario()));
            }
            if (insumo.getPrecioTotal() != null) {
                etPrecioTotal.setText(String.format(Locale.getDefault(), "%.2f", insumo.getPrecioTotal()));
            }
            etCategoria.setText(insumo.getCategoria());
            if (insumo.getStockMinimo() != null) {
                etStockMinimo.setText(String.format(Locale.getDefault(), "%.2f", insumo.getStockMinimo()));
            }
        }
    }

    private void guardarInsumo() {
        // Validaciones
        String nombre = etNombre.getText().toString().trim();
        if (nombre.isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            etNombre.requestFocus();
            return;
        }

        String cantidadStr = etCantidad.getText().toString().trim();
        if (cantidadStr.isEmpty()) {
            etCantidad.setError("La cantidad es obligatoria");
            etCantidad.requestFocus();
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(cantidadStr);
            if (cantidad <= 0) {
                etCantidad.setError("La cantidad debe ser mayor a 0");
                etCantidad.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etCantidad.setError("Cantidad inválida");
            etCantidad.requestFocus();
            return;
        }

        // Crear objeto Insumo
        Insumo insumo = new Insumo();
        if (insumoId != null) {
            insumo.setId(insumoId);
        }
        insumo.setEmpresaId(empresaId);
        insumo.setNombre(nombre);
        insumo.setDescripcion(etDescripcion.getText().toString().trim());
        insumo.setCantidad(cantidad);
        insumo.setUnidadMedida(etUnidad.getText().toString().trim());

        try {
            String precioUnitarioStr = etPrecioUnitario.getText().toString().trim();
            if (!precioUnitarioStr.isEmpty()) {
                insumo.setPrecioUnitario(Double.parseDouble(precioUnitarioStr));
            }
        } catch (NumberFormatException e) {
            insumo.setPrecioUnitario(0.0);
        }

        try {
            String precioTotalStr = etPrecioTotal.getText().toString().trim();
            if (!precioTotalStr.isEmpty()) {
                insumo.setPrecioTotal(Double.parseDouble(precioTotalStr));
            } else {
                insumo.calcularPrecioTotal();
            }
        } catch (NumberFormatException e) {
            insumo.calcularPrecioTotal();
        }

        insumo.setCategoria(etCategoria.getText().toString().trim());

        try {
            String stockMinimoStr = etStockMinimo.getText().toString().trim();
            if (!stockMinimoStr.isEmpty()) {
                insumo.setStockMinimo(Double.parseDouble(stockMinimoStr));
            }
        } catch (NumberFormatException e) {
            // Stock mínimo opcional
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String now = sdf.format(new Date());
        
        if (insumoId == null) {
            insumo.setCreatedAt(now);
        }
        insumo.setUpdatedAt(now);

        // Guardar localmente primero
        if (insumoId == null) {
            long id = dbHelper.insertarInsumo(insumo);
            insumo.setId((int) id);
        } else {
            dbHelper.actualizarInsumo(insumo);
        }

        // Intentar sincronizar con API
        if (NetworkUtils.isNetworkAvailable(this)) {
            if (insumoId == null) {
                // Crear nuevo
                apiService.crearInsumo(insumo).enqueue(new Callback<Insumo>() {
                    @Override
                    public void onResponse(Call<Insumo> call, Response<Insumo> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Insumo insumoServer = response.body();
                            insumoServer.setId(insumo.getId());
                            insumoServer.setServerId(insumoServer.getId());
                            insumoServer.setSyncStatus("synced");
                            dbHelper.actualizarInsumo(insumoServer);
                            Toast.makeText(ItemFormActivity.this, "Insumo guardado y sincronizado", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Insumo> call, Throwable t) {
                        syncManager.agregarASyncQueue("Insumo", "INSERT", insumo.getId(), insumo);
                        Toast.makeText(ItemFormActivity.this, "Insumo guardado localmente. Se sincronizará cuando haya conexión.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            } else {
                // Actualizar existente
                if (insumo.getServerId() != null) {
                    apiService.actualizarInsumo(insumo.getServerId(), insumo).enqueue(new Callback<Insumo>() {
                        @Override
                        public void onResponse(Call<Insumo> call, Response<Insumo> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Insumo insumoServer = response.body();
                                insumoServer.setId(insumo.getId());
                                insumoServer.setServerId(insumoServer.getId());
                                insumoServer.setSyncStatus("synced");
                                dbHelper.actualizarInsumo(insumoServer);
                                Toast.makeText(ItemFormActivity.this, "Insumo actualizado y sincronizado", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Insumo> call, Throwable t) {
                            syncManager.agregarASyncQueue("Insumo", "UPDATE", insumo.getId(), insumo);
                            Toast.makeText(ItemFormActivity.this, "Insumo actualizado localmente. Se sincronizará cuando haya conexión.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                } else {
                    // No tiene server_id, intentar crear
                    apiService.crearInsumo(insumo).enqueue(new Callback<Insumo>() {
                        @Override
                        public void onResponse(Call<Insumo> call, Response<Insumo> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Insumo insumoServer = response.body();
                                insumoServer.setId(insumo.getId());
                                insumoServer.setServerId(insumoServer.getId());
                                insumoServer.setSyncStatus("synced");
                                dbHelper.actualizarInsumo(insumoServer);
                                Toast.makeText(ItemFormActivity.this, "Insumo guardado y sincronizado", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Insumo> call, Throwable t) {
                            syncManager.agregarASyncQueue("Insumo", "UPDATE", insumo.getId(), insumo);
                            Toast.makeText(ItemFormActivity.this, "Insumo actualizado localmente. Se sincronizará cuando haya conexión.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                }
            }
        } else {
            // Offline: agregar a cola de sincronización
            syncManager.agregarASyncQueue("Insumo", insumoId == null ? "INSERT" : "UPDATE", insumo.getId(), insumo);
            Toast.makeText(this, "Insumo guardado localmente. Se sincronizará cuando haya conexión.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}

