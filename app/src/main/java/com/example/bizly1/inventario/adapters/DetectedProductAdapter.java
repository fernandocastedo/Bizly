package com.example.bizly1.inventario.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.Product;

import java.util.List;

public class DetectedProductAdapter extends RecyclerView.Adapter<DetectedProductAdapter.ViewHolder> {

    private final List<Product> items;
    private OnEditClickListener onEditClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnEditClickListener {
        void onEditClick(int position, Product product);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public DetectedProductAdapter(List<Product> items) {
        this.items = items;
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.onEditClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detected_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = items.get(position);

        holder.txtName.setText(p.getName());
        holder.txtQuantity.setText(String.format("%.2f", p.getQuantity()));
        
        String unit = p.getUnit();
        if (unit == null || unit.isEmpty()) {
            unit = "u";
        }
        holder.txtUnit.setText(unit);
        
        holder.txtUnitPrice.setText(String.format("Bs. %.2f", p.getUnitPrice()));
        holder.txtTotalPrice.setText(String.format("Bs. %.2f", p.getTotalPrice()));

        holder.btnEdit.setOnClickListener(v -> {
            if (onEditClickListener != null) {
                onEditClickListener.onEditClick(position, p);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtQuantity, txtUnit, txtUnitPrice, txtTotalPrice;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtDetectedName);
            txtQuantity = itemView.findViewById(R.id.txtDetectedQuantity);
            txtUnit = itemView.findViewById(R.id.txtDetectedUnit);
            txtUnitPrice = itemView.findViewById(R.id.txtDetectedUnitPrice);
            txtTotalPrice = itemView.findViewById(R.id.txtDetectedTotalPrice);
            btnEdit = itemView.findViewById(R.id.btnEditProduct);
            btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }
}

