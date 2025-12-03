package com.example.bizly1.costos.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.CostoGasto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CostoGastoAdapter extends RecyclerView.Adapter<CostoGastoAdapter.ViewHolder> {

    private List<CostoGasto> costosGastos;
    private OnItemClickListener listener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(CostoGasto costoGasto);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(CostoGasto costoGasto);
    }

    public CostoGastoAdapter(List<CostoGasto> costosGastos) {
        this.costosGastos = costosGastos != null ? costosGastos : new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void updateList(List<CostoGasto> nuevosCostosGastos) {
        this.costosGastos.clear();
        if (nuevosCostosGastos != null) {
            this.costosGastos.addAll(nuevosCostosGastos);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_costo_gasto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CostoGasto cg = costosGastos.get(position);
        holder.bind(cg);
    }

    @Override
    public int getItemCount() {
        return costosGastos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTipo, txtDescripcion, txtMonto, txtFecha, txtCategoria;
        CardView cardCostoGasto;
        ImageButton btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardCostoGasto = itemView.findViewById(R.id.cardCostoGasto);
            txtTipo = itemView.findViewById(R.id.txtTipo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtMonto = itemView.findViewById(R.id.txtMonto);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        public void bind(CostoGasto cg) {
            // Tipo con color según el tipo
            String tipo = cg.getTipo();
            if (tipo != null) {
                String tipoCapitalizado = tipo.substring(0, 1).toUpperCase() + tipo.substring(1);
                txtTipo.setText(tipoCapitalizado);
                
                // Color según tipo
                int colorRes;
                switch (tipo.toLowerCase()) {
                    case "costo":
                        colorRes = R.color.color_costo; // Verde
                        break;
                    case "gasto":
                        colorRes = R.color.color_gasto; // Rojo
                        break;
                    case "sueldo":
                        colorRes = R.color.color_sueldo; // Azul
                        break;
                    case "adelanto":
                        colorRes = R.color.color_adelanto; // Naranja
                        break;
                    default:
                        colorRes = android.R.color.darker_gray;
                }
                txtTipo.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), colorRes));
            }

            txtDescripcion.setText(cg.getDescripcion());
            if (cg.getMonto() != null) {
                txtMonto.setText(String.format(Locale.getDefault(), "Bs. %.2f", cg.getMonto()));
            }
            if (cg.getFecha() != null) {
                txtFecha.setText("Fecha: " + cg.getFecha());
            }
            if (cg.getCategoria() != null && !cg.getCategoria().isEmpty()) {
                txtCategoria.setText("Categoría: " + cg.getCategoria());
                txtCategoria.setVisibility(View.VISIBLE);
            } else {
                txtCategoria.setVisibility(View.GONE);
            }

            cardCostoGasto.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(cg);
                }
            });

            btnEliminar.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(cg);
                }
            });
        }
    }
}

