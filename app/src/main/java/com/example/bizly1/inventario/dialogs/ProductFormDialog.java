package com.example.bizly1.inventario.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.data.network.ApiClient;
import com.example.bizly1.data.network.ApiService;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.data.utils.SyncManager;
import com.example.bizly1.inventario.adapters.DetectedProductAdapter;
import com.example.bizly1.models.Insumo;
import com.example.bizly1.models.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFormDialog {

    public interface OnProductsSavedListener {
        void onSaved();
    }

    // Método para múltiples productos
    public static void showMultiple(Context context, List<Product> products, 
                                     DBHelper dbHelper, SyncManager syncManager, 
                                     Integer empresaId, OnProductsSavedListener listener) {
        if (products == null || products.isEmpty()) {
            Toast.makeText(context, "No se detectaron productos", Toast.LENGTH_SHORT).show();
            return;
        }

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_product_form, null);
        
        LinearLayout containerList = view.findViewById(R.id.containerProductList);
        LinearLayout containerForm = view.findViewById(R.id.containerEditForm);
        
        RecyclerView recyclerProducts = view.findViewById(R.id.recyclerDetectedProducts);
        TextView txtEmpty = view.findViewById(R.id.txtEmptyMessage);
        Button btnConfirmAll = view.findViewById(R.id.btnConfirmAll);
        
        containerForm.setVisibility(View.GONE);
        containerList.setVisibility(View.VISIBLE);

        // Crear copia mutable de la lista
        List<Product> productsList = new ArrayList<>(products);
        
        DetectedProductAdapter adapter = new DetectedProductAdapter(productsList);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(context));
        recyclerProducts.setAdapter(adapter);
        
        updateEmptyMessage(txtEmpty, recyclerProducts, productsList.size());

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(true)
                .create();

        // Editar producto
        adapter.setOnEditClickListener((position, product) -> {
            showEditForm(context, view, productsList, position, adapter, txtEmpty, recyclerProducts);
        });

        // Eliminar producto
        adapter.setOnDeleteClickListener(position -> {
            productsList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, productsList.size());
            updateEmptyMessage(txtEmpty, recyclerProducts, productsList.size());
            
            if (productsList.isEmpty()) {
                Toast.makeText(context, "No hay productos para guardar", Toast.LENGTH_SHORT).show();
            }
        });

        // Confirmar todos
        btnConfirmAll.setOnClickListener(v -> {
            if (productsList.isEmpty()) {
                Toast.makeText(context, "No hay productos para guardar", Toast.LENGTH_SHORT).show();
                return;
            }

            guardarProductos(context, productsList, dbHelper, syncManager, empresaId, dialog, listener);
        });

        dialog.show();
    }

    private static void showEditForm(Context context, View rootView, List<Product> productsList, 
                                     int position, DetectedProductAdapter adapter, 
                                     TextView txtEmpty, RecyclerView recycler) {
        LinearLayout containerList = rootView.findViewById(R.id.containerProductList);
        LinearLayout containerForm = rootView.findViewById(R.id.containerEditForm);
        
        containerList.setVisibility(View.GONE);
        containerForm.setVisibility(View.VISIBLE);

        Product product = productsList.get(position);
        
        EditText edtName = rootView.findViewById(R.id.edtName);
        EditText edtQuantity = rootView.findViewById(R.id.edtQuantity);
        EditText edtUnit = rootView.findViewById(R.id.edtUnit);
        EditText edtUnitPrice = rootView.findViewById(R.id.edtUnitPrice);
        EditText edtTotalPrice = rootView.findViewById(R.id.edtTotalPrice);
        EditText edtCategoria = rootView.findViewById(R.id.edtCategoria);
        
        Button btnCancel = rootView.findViewById(R.id.btnCancelEdit);
        Button btnSave = rootView.findViewById(R.id.btnConfirmEdit);

        // Llenar campos
        edtName.setText(product.getName());
        edtQuantity.setText(String.format("%.2f", product.getQuantity()));
        edtUnit.setText(product.getUnit() != null ? product.getUnit() : "");
        edtUnitPrice.setText(String.format("%.2f", product.getUnitPrice()));
        edtTotalPrice.setText(String.format("%.2f", product.getTotalPrice()));
        edtCategoria.setText(product.getCategoria() != null ? product.getCategoria() : "General");

        // Calcular precio total automáticamente
        TextWatcher calcularWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String cantidadStr = edtQuantity.getText().toString().trim();
                    String precioUnitarioStr = edtUnitPrice.getText().toString().trim();
                    if (!cantidadStr.isEmpty() && !precioUnitarioStr.isEmpty()) {
                        double cantidad = Double.parseDouble(cantidadStr);
                        double precioUnitario = Double.parseDouble(precioUnitarioStr);
                        double precioTotal = cantidad * precioUnitario;
                        edtTotalPrice.setText(String.format(Locale.getDefault(), "%.2f", precioTotal));
                    }
                } catch (NumberFormatException e) {
                    // Ignorar
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        edtQuantity.addTextChangedListener(calcularWatcher);
        edtUnitPrice.addTextChangedListener(calcularWatcher);

        btnCancel.setOnClickListener(v -> {
            containerForm.setVisibility(View.GONE);
            containerList.setVisibility(View.VISIBLE);
        });

        btnSave.setOnClickListener(v -> {
            try {
                String n = edtName.getText().toString().trim();
                double q = edtQuantity.getText().toString().isEmpty() ? 0 : Double.parseDouble(edtQuantity.getText().toString());
                String u = edtUnit.getText().toString().trim();
                double up = edtUnitPrice.getText().toString().isEmpty() ? 0 : Double.parseDouble(edtUnitPrice.getText().toString());
                double tp = edtTotalPrice.getText().toString().isEmpty() ? 0 : Double.parseDouble(edtTotalPrice.getText().toString());
                String cat = edtCategoria.getText().toString().trim();

                if (n.isEmpty()) {
                    Toast.makeText(context, "Ingrese un nombre", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Calcular valores faltantes
                if (up == 0 && q > 0 && tp > 0) up = tp / q;
                if (tp == 0 && q > 0 && up > 0) tp = q * up;

                // Actualizar producto en la lista
                productsList.set(position, new Product(n, q, u, up, tp, cat));
                
                // Actualizar adapter
                adapter.notifyItemChanged(position);
                
                // Volver a la lista
                containerForm.setVisibility(View.GONE);
                containerList.setVisibility(View.VISIBLE);
                
                Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(context, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void updateEmptyMessage(TextView txtEmpty, RecyclerView recycler, int count) {
        if (count == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }
    }

    private static void guardarProductos(Context context, List<Product> productsList, 
                                         DBHelper dbHelper, SyncManager syncManager, 
                                         Integer empresaId, AlertDialog dialog, 
                                         OnProductsSavedListener listener) {
        ApiService apiService = ApiClient.getApiService();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String now = sdf.format(new Date());

        int guardados = 0;
        for (Product p : productsList) {
            Insumo insumo = p.toInsumo(empresaId);
            insumo.setCreatedAt(now);
            insumo.setUpdatedAt(now);

            // Guardar localmente primero
            long id = dbHelper.insertarInsumo(insumo);
            insumo.setId((int) id);

            // Intentar sincronizar con API
            if (NetworkUtils.isNetworkAvailable(context)) {
                apiService.crearInsumo(insumo).enqueue(new Callback<Insumo>() {
                    @Override
                    public void onResponse(Call<Insumo> call, Response<Insumo> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Insumo insumoServer = response.body();
                            insumoServer.setId(insumo.getId());
                            insumoServer.setServerId(insumoServer.getId());
                            insumoServer.setSyncStatus("synced");
                            dbHelper.actualizarInsumo(insumoServer);
                        }
                    }

                    @Override
                    public void onFailure(Call<Insumo> call, Throwable t) {
                        syncManager.agregarASyncQueue("Insumo", "INSERT", insumo.getId(), insumo);
                    }
                });
            } else {
                syncManager.agregarASyncQueue("Insumo", "INSERT", insumo.getId(), insumo);
            }
            guardados++;
        }

        Toast.makeText(context, guardados + " producto(s) agregado(s) al inventario", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        if (listener != null) listener.onSaved();
    }
}

