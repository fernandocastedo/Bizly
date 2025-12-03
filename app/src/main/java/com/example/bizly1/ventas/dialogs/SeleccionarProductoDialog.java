package com.example.bizly1.ventas.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.ProductoVenta;
import com.example.bizly1.ventas.adapters.ProductoDialogAdapter;

import java.util.ArrayList;
import java.util.List;

public class SeleccionarProductoDialog {

    private Dialog dialog;
    private List<ProductoVenta> productos;
    private List<ProductoVenta> productosFiltrados;
    private OnProductoSelectedListener listener;
    private ProductoDialogAdapter adapter;
    private EditText etBuscar;
    private TextView txtEmpty;

    public interface OnProductoSelectedListener {
        void onProductoSelected(ProductoVenta producto);
    }

    public SeleccionarProductoDialog(Context context, List<ProductoVenta> productos, OnProductoSelectedListener listener) {
        this.productos = productos;
        this.productosFiltrados = new ArrayList<>(productos);
        this.listener = listener;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_seleccionar_producto, null);

        etBuscar = view.findViewById(R.id.etBuscar);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewProductos);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        adapter = new ProductoDialogAdapter(productosFiltrados);
        adapter.setOnProductoClickListener(producto -> {
            if (listener != null) {
                listener.onProductoSelected(producto);
            }
            dialog.dismiss();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        // Filtro de búsqueda
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

        actualizarEmptyMessage();

        builder.setView(view)
                .setTitle("Seleccionar Producto")
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        dialog = builder.create();
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    private void filtrarProductos(String query) {
        productosFiltrados.clear();
        if (query.isEmpty()) {
            productosFiltrados.addAll(productos);
        } else {
            String lowerQuery = query.toLowerCase();
            for (ProductoVenta pv : productos) {
                if (pv.getNombre().toLowerCase().contains(lowerQuery)) {
                    productosFiltrados.add(pv);
                }
            }
        }
        adapter.notifyDataSetChanged();
        actualizarEmptyMessage();
    }

    private void actualizarEmptyMessage() {
        if (txtEmpty != null) {
            txtEmpty.setVisibility(productosFiltrados.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }
}

