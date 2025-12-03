package com.example.bizly1.inventario.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.Insumo;

import java.util.List;

public class InsumoAdapter extends RecyclerView.Adapter<InsumoAdapter.InsumoViewHolder> {

    private List<Insumo> insumos;
    private OnInsumoClickListener listener;

    public interface OnInsumoClickListener {
        void onInsumoClick(Insumo insumo);
        void onEditClick(Insumo insumo);
        void onDeleteClick(Insumo insumo);
        void onRestockClick(Insumo insumo);
    }

    public InsumoAdapter(List<Insumo> insumos, OnInsumoClickListener listener) {
        this.insumos = insumos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InsumoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_insumo, parent, false);
        return new InsumoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InsumoViewHolder holder, int position) {
        Insumo insumo = insumos.get(position);
        holder.bind(insumo);
    }

    @Override
    public int getItemCount() {
        return insumos != null ? insumos.size() : 0;
    }

    public void updateList(List<Insumo> nuevasInsumos) {
        this.insumos = nuevasInsumos;
        notifyDataSetChanged();
    }

    class InsumoViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNombre, txtCantidad, txtUnidad, txtPrecioUnitario, 
                        txtPrecioTotal, txtCategoria, txtStockAlerta;
        private CardView cardInsumo;
        private Button btnEdit, btnDelete, btnRestock;

        public InsumoViewHolder(@NonNull View itemView) {
            super(itemView);
            cardInsumo = itemView.findViewById(R.id.cardInsumo);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            txtUnidad = itemView.findViewById(R.id.txtUnidad);
            txtPrecioUnitario = itemView.findViewById(R.id.txtPrecioUnitario);
            txtPrecioTotal = itemView.findViewById(R.id.txtPrecioTotal);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            txtStockAlerta = itemView.findViewById(R.id.txtStockAlerta);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnRestock = itemView.findViewById(R.id.btnRestock);
        }

        public void bind(Insumo insumo) {
            txtNombre.setText(insumo.getNombre());
            txtCantidad.setText(String.format("%.2f", insumo.getCantidad()));
            txtUnidad.setText(insumo.getUnidadMedida() != null ? insumo.getUnidadMedida() : "u");
            
            if (insumo.getPrecioUnitario() != null) {
                txtPrecioUnitario.setText(String.format("Bs. %.2f", insumo.getPrecioUnitario()));
            } else {
                txtPrecioUnitario.setText("Bs. 0.00");
            }
            
            if (insumo.getPrecioTotal() != null) {
                txtPrecioTotal.setText(String.format("Bs. %.2f", insumo.getPrecioTotal()));
            } else {
                txtPrecioTotal.setText("Bs. 0.00");
            }
            
            txtCategoria.setText(insumo.getCategoria() != null ? insumo.getCategoria() : "General");

            // Verificar stock bajo (RF-16)
            if (insumo.isStockBajo()) {
                txtStockAlerta.setVisibility(View.VISIBLE);
                txtStockAlerta.setText("⚠ Stock bajo");
                cardInsumo.setCardBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_light, null));
            } else {
                txtStockAlerta.setVisibility(View.GONE);
                cardInsumo.setCardBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.white, null));
            }

            // Listeners
            cardInsumo.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onInsumoClick(insumo);
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(insumo);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(insumo);
                }
            });

            btnRestock.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRestockClick(insumo);
                }
            });
        }
    }
}

