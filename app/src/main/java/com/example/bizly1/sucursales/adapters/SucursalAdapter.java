package com.example.bizly1.sucursales.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.Sucursal;

import java.util.ArrayList;
import java.util.List;

public class SucursalAdapter extends RecyclerView.Adapter<SucursalAdapter.ViewHolder> {

    private List<Sucursal> sucursales;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Sucursal sucursal);
    }

    public SucursalAdapter(List<Sucursal> sucursales) {
        this.sucursales = sucursales != null ? sucursales : new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<Sucursal> nuevasSucursales) {
        this.sucursales.clear();
        if (nuevasSucursales != null) {
            this.sucursales.addAll(nuevasSucursales);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sucursal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sucursal sucursal = sucursales.get(position);
        holder.bind(sucursal);
    }

    @Override
    public int getItemCount() {
        return sucursales.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDireccion, txtCiudad;
        CardView cardSucursal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardSucursal = itemView.findViewById(R.id.cardSucursal);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtDireccion = itemView.findViewById(R.id.txtDireccion);
            txtCiudad = itemView.findViewById(R.id.txtCiudad);
        }

        public void bind(Sucursal sucursal) {
            txtNombre.setText(sucursal.getNombre());
            if (sucursal.getDireccion() != null && !sucursal.getDireccion().isEmpty()) {
                txtDireccion.setText(sucursal.getDireccion());
                txtDireccion.setVisibility(View.VISIBLE);
            } else {
                txtDireccion.setVisibility(View.GONE);
            }
            if (sucursal.getCiudad() != null && !sucursal.getCiudad().isEmpty()) {
                txtCiudad.setText(sucursal.getCiudad() + (sucursal.getDepartamento() != null && !sucursal.getDepartamento().isEmpty() ? ", " + sucursal.getDepartamento() : ""));
            } else {
                txtCiudad.setVisibility(View.GONE);
            }

            cardSucursal.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(sucursal);
                }
            });
        }
    }
}

