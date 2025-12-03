package com.example.bizly1.productos.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.Insumo;

import java.util.List;

public class InsumoDialogAdapter extends RecyclerView.Adapter<InsumoDialogAdapter.ViewHolder> {

    private final List<Insumo> insumos;
    private OnInsumoClickListener listener;

    public interface OnInsumoClickListener {
        void onInsumoClick(Insumo insumo);
    }

    public InsumoDialogAdapter(List<Insumo> insumos, OnInsumoClickListener listener) {
        this.insumos = insumos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_insumo_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Insumo insumo = insumos.get(position);
        holder.bind(insumo);
    }

    @Override
    public int getItemCount() {
        return insumos.size();
    }

    public void updateList(List<Insumo> nuevosInsumos) {
        this.insumos.clear();
        this.insumos.addAll(nuevosInsumos);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtStock, txtUnidad, txtPrecioUnitario;
        CardView cardInsumo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardInsumo = itemView.findViewById(R.id.cardInsumo);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtStock = itemView.findViewById(R.id.txtStock);
            txtUnidad = itemView.findViewById(R.id.txtUnidad);
            txtPrecioUnitario = itemView.findViewById(R.id.txtPrecioUnitario);
        }

        public void bind(Insumo insumo) {
            txtNombre.setText(insumo.getNombre());
            txtStock.setText(String.format("%.2f", insumo.getCantidad()));
            
            String unidad = insumo.getUnidadMedida();
            if (unidad == null || unidad.isEmpty()) {
                unidad = "u";
            }
            txtUnidad.setText(" " + unidad);
            
            if (insumo.getPrecioUnitario() != null) {
                txtPrecioUnitario.setText(String.format("Precio: Bs. %.2f", insumo.getPrecioUnitario()));
            } else {
                txtPrecioUnitario.setText("Precio: Bs. 0.00");
            }

            cardInsumo.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onInsumoClick(insumo);
                }
            });
        }
    }
}

