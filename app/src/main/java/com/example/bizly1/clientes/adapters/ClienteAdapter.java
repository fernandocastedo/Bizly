package com.example.bizly1.clientes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {

    private List<Cliente> clientes;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Cliente cliente);
    }

    public ClienteAdapter(List<Cliente> clientes) {
        this.clientes = clientes != null ? clientes : new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<Cliente> nuevosClientes) {
        this.clientes.clear();
        if (nuevosClientes != null) {
            this.clientes.addAll(nuevosClientes);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.bind(cliente);
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtNit, txtTelefono, txtEmail, txtDireccion;
        CardView cardCliente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardCliente = itemView.findViewById(R.id.cardCliente);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtNit = itemView.findViewById(R.id.txtNit);
            txtTelefono = itemView.findViewById(R.id.txtTelefono);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtDireccion = itemView.findViewById(R.id.txtDireccion);
        }

        public void bind(Cliente cliente) {
            txtNombre.setText(cliente.getNombre());
            
            if (cliente.getNit() != null) {
                txtNit.setText("NIT: " + cliente.getNit());
                txtNit.setVisibility(View.VISIBLE);
            } else {
                txtNit.setVisibility(View.GONE);
            }
            
            if (cliente.getTelefono() != null && !cliente.getTelefono().isEmpty()) {
                txtTelefono.setText("Tel: " + cliente.getTelefono());
                txtTelefono.setVisibility(View.VISIBLE);
            } else {
                txtTelefono.setVisibility(View.GONE);
            }
            
            if (cliente.getEmail() != null && !cliente.getEmail().isEmpty()) {
                txtEmail.setText("Email: " + cliente.getEmail());
                txtEmail.setVisibility(View.VISIBLE);
            } else {
                txtEmail.setVisibility(View.GONE);
            }
            
            if (cliente.getDireccion() != null && !cliente.getDireccion().isEmpty()) {
                txtDireccion.setText("Dirección: " + cliente.getDireccion());
                txtDireccion.setVisibility(View.VISIBLE);
            } else {
                txtDireccion.setVisibility(View.GONE);
            }

            cardCliente.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(cliente);
                }
            });
        }
    }
}

