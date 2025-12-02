package com.bizly.app.presentation.sucursales;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bizly.app.R;
import com.bizly.app.domain.model.Sucursal;
import java.util.List;

/**
 * Adapter para mostrar sucursales en RecyclerView
 */
public class SucursalAdapter extends RecyclerView.Adapter<SucursalAdapter.SucursalViewHolder> {
    
    private List<Sucursal> sucursales;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onItemClick(Sucursal sucursal);
    }
    
    public SucursalAdapter(List<Sucursal> sucursales, OnItemClickListener listener) {
        this.sucursales = sucursales;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public SucursalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sucursal, parent, false);
        return new SucursalViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull SucursalViewHolder holder, int position) {
        Sucursal sucursal = sucursales.get(position);
        holder.bind(sucursal, listener);
    }
    
    @Override
    public int getItemCount() {
        return sucursales != null ? sucursales.size() : 0;
    }
    
    public void updateSucursales(List<Sucursal> newSucursales) {
        this.sucursales = newSucursales;
        notifyDataSetChanged();
    }
    
    static class SucursalViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreTextView;
        private TextView direccionTextView;
        private TextView ciudadTextView;
        
        public SucursalViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            direccionTextView = itemView.findViewById(R.id.direccionTextView);
            ciudadTextView = itemView.findViewById(R.id.ciudadTextView);
        }
        
        public void bind(Sucursal sucursal, OnItemClickListener listener) {
            nombreTextView.setText(sucursal.getNombre());
            direccionTextView.setText(sucursal.getDireccion());
            ciudadTextView.setText(sucursal.getCiudad() + (sucursal.getDepartamento() != null ? ", " + sucursal.getDepartamento() : ""));
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(sucursal);
                }
            });
        }
    }
}


