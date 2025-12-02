package com.example.bizly1.productos.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.models.Insumo;
import com.example.bizly1.productos.adapters.InsumoDialogAdapter;

import java.util.ArrayList;
import java.util.List;

public class SeleccionarInsumoDialog {

    public interface OnInsumoSeleccionadoListener {
        void onInsumoSeleccionado(Insumo insumo, double cantidad);
    }

    public static void show(Context context, DBHelper dbHelper, 
                           List<Integer> insumosYaSeleccionados,
                           OnInsumoSeleccionadoListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_seleccionar_insumo, null);
        
        RecyclerView recyclerViewInsumos = view.findViewById(R.id.recyclerViewInsumos);
        TextView txtEmpty = view.findViewById(R.id.txtEmpty);
        com.google.android.material.textfield.TextInputEditText etBuscar = view.findViewById(R.id.etBuscar);
        com.google.android.material.textfield.TextInputEditText etCantidad = view.findViewById(R.id.etCantidad);
        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        Button btnAgregar = view.findViewById(R.id.btnAgregar);

        // Cargar todos los insumos
        List<Insumo> todosInsumos = dbHelper.obtenerTodosInsumos();
        
        // Filtrar los que ya están seleccionados
        List<Insumo> insumosDisponibles = new ArrayList<>();
        for (Insumo insumo : todosInsumos) {
            if (!insumosYaSeleccionados.contains(insumo.getId())) {
                insumosDisponibles.add(insumo);
            }
        }
        
        List<Insumo> insumosFiltrados = new ArrayList<>(insumosDisponibles);
        
        InsumoDialogAdapter adapter = new InsumoDialogAdapter(insumosFiltrados, insumo -> {
            // Al seleccionar un insumo, habilitar el botón agregar
            etCantidad.setTag(insumo);
            etCantidad.setEnabled(true);
            btnAgregar.setEnabled(true);
        });
        
        recyclerViewInsumos.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewInsumos.setAdapter(adapter);
        
        updateEmptyMessage(txtEmpty, recyclerViewInsumos, insumosFiltrados.size());

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(true)
                .create();

        // Búsqueda
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String texto = s.toString().toLowerCase().trim();
                insumosFiltrados.clear();
                
                if (texto.isEmpty()) {
                    insumosFiltrados.addAll(insumosDisponibles);
                } else {
                    for (Insumo insumo : insumosDisponibles) {
                        if (insumo.getNombre() != null && 
                            insumo.getNombre().toLowerCase().contains(texto)) {
                            insumosFiltrados.add(insumo);
                        }
                    }
                }
                
                adapter.updateList(insumosFiltrados);
                updateEmptyMessage(txtEmpty, recyclerViewInsumos, insumosFiltrados.size());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnAgregar.setOnClickListener(v -> {
            Insumo insumoSeleccionado = (Insumo) etCantidad.getTag();
            if (insumoSeleccionado == null) {
                Toast.makeText(context, "Seleccione un insumo", Toast.LENGTH_SHORT).show();
                return;
            }

            String cantidadStr = etCantidad.getText().toString().trim();
            if (cantidadStr.isEmpty()) {
                etCantidad.setError("Ingrese la cantidad");
                return;
            }

            double cantidad;
            try {
                cantidad = Double.parseDouble(cantidadStr);
                if (cantidad <= 0) {
                    etCantidad.setError("La cantidad debe ser mayor a 0");
                    return;
                }
                
                // Verificar stock disponible
                if (insumoSeleccionado.getCantidad() == null || 
                    insumoSeleccionado.getCantidad() < cantidad) {
                    Toast.makeText(context, 
                        "No hay suficiente stock. Disponible: " + 
                        (insumoSeleccionado.getCantidad() != null ? insumoSeleccionado.getCantidad() : 0),
                        Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (NumberFormatException e) {
                etCantidad.setError("Cantidad inválida");
                return;
            }

            if (listener != null) {
                listener.onInsumoSeleccionado(insumoSeleccionado, cantidad);
            }
            dialog.dismiss();
        });

        // Inicialmente deshabilitar cantidad y agregar
        etCantidad.setEnabled(false);
        btnAgregar.setEnabled(false);

        dialog.show();
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
}

