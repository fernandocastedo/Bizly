package com.example.bizly1.productos.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.ProductoVentaInsumo;

import java.util.List;

public class InsumoSeleccionadoAdapter extends RecyclerView.Adapter<InsumoSeleccionadoAdapter.ViewHolder> {

    private final List<ProductoVentaInsumo> insumos;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public InsumoSeleccionadoAdapter(List<ProductoVentaInsumo> insumos) {
        this.insumos = insumos;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_insumo_seleccionado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductoVentaInsumo pvi = insumos.get(position);

        if (pvi.getInsumo() != null) {
            holder.txtNombre.setText(pvi.getInsumo().getNombre());
            holder.txtCantidadUsada.setText(String.format("%.2f", pvi.getCantidadUsada()));
            
            String unidad = pvi.getInsumo().getUnidadMedida();
            if (unidad == null || unidad.isEmpty()) {
                unidad = "u";
            }
            holder.txtUnidad.setText(" " + unidad);
            
            double costo = pvi.calcularCosto();
            holder.txtCosto.setText(String.format("Costo: Bs. %.2f", costo));
        }

        holder.btnEliminar.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return insumos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCantidadUsada, txtUnidad, txtCosto;
        Button btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreInsumo);
            txtCantidadUsada = itemView.findViewById(R.id.txtCantidadUsada);
            txtUnidad = itemView.findViewById(R.id.txtUnidad);
            txtCosto = itemView.findViewById(R.id.txtCostoInsumo);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}

