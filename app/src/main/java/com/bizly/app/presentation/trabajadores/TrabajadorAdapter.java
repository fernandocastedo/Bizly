package com.bizly.app.presentation.trabajadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bizly.app.R;
import com.bizly.app.domain.model.Trabajador;
import com.google.android.material.button.MaterialButton;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter para mostrar trabajadores en RecyclerView
 */
public class TrabajadorAdapter extends RecyclerView.Adapter<TrabajadorAdapter.TrabajadorViewHolder> {
    
    private List<Trabajador> trabajadores;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onItemClick(Trabajador trabajador);
        void onEditClick(Trabajador trabajador);
        void onDeleteClick(Trabajador trabajador);
    }
    
    public TrabajadorAdapter(List<Trabajador> trabajadores, OnItemClickListener listener) {
        this.trabajadores = trabajadores;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public TrabajadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trabajador, parent, false);
        return new TrabajadorViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TrabajadorViewHolder holder, int position) {
        Trabajador trabajador = trabajadores.get(position);
        holder.bind(trabajador, listener);
    }
    
    @Override
    public int getItemCount() {
        return trabajadores != null ? trabajadores.size() : 0;
    }
    
    public void updateTrabajadores(List<Trabajador> newTrabajadores) {
        this.trabajadores = newTrabajadores;
        notifyDataSetChanged();
    }
    
    static class TrabajadorViewHolder extends RecyclerView.ViewHolder {
        private TextView nombreTextView;
        private TextView cargoTextView;
        private TextView sueldoTextView;
        private TextView tipoGastoTextView;
        private MaterialButton editButton;
        private MaterialButton deleteButton;
        
        public TrabajadorViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            cargoTextView = itemView.findViewById(R.id.cargoTextView);
            sueldoTextView = itemView.findViewById(R.id.sueldoTextView);
            tipoGastoTextView = itemView.findViewById(R.id.tipoGastoTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
        
        public void bind(Trabajador trabajador, OnItemClickListener listener) {
            nombreTextView.setText(trabajador.getNombre());
            cargoTextView.setText(trabajador.getCargo());
            
            // Formatear sueldo
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "GT"));
            sueldoTextView.setText(currencyFormat.format(trabajador.getSueldoMensual()));
            
            // Tipo de gasto
            String tipoGasto = trabajador.getTipoGasto();
            if (tipoGasto != null) {
                tipoGastoTextView.setText(tipoGasto.equals("fijo") ? "Gasto Fijo" : "Gasto Variable");
            } else {
                tipoGastoTextView.setText("");
            }
            
            // Botón editar
            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(trabajador);
                }
            });
            
            // Botón eliminar
            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(trabajador);
                }
            });
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(trabajador);
                }
            });
        }
    }
}

