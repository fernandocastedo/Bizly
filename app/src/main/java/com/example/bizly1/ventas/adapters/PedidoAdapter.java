package com.example.bizly1.ventas.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.Venta;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {

    private List<Venta> pedidos;
    private OnCompletarClickListener onCompletarClickListener;
    private OnCancelarClickListener onCancelarClickListener;
    private OnConfirmarPagoClickListener onConfirmarPagoClickListener;
    private OnConfirmarEnvioClickListener onConfirmarEnvioClickListener;

    public interface OnCompletarClickListener {
        void onCompletarClick(Venta venta);
    }

    public interface OnCancelarClickListener {
        void onCancelarClick(Venta venta);
    }

    public interface OnConfirmarPagoClickListener {
        void onConfirmarPagoClick(Venta venta);
    }

    public interface OnConfirmarEnvioClickListener {
        void onConfirmarEnvioClick(Venta venta);
    }

    public PedidoAdapter(List<Venta> pedidos) {
        this.pedidos = pedidos != null ? pedidos : new ArrayList<>();
    }

    public void setOnCompletarClickListener(OnCompletarClickListener listener) {
        this.onCompletarClickListener = listener;
    }

    public void setOnCancelarClickListener(OnCancelarClickListener listener) {
        this.onCancelarClickListener = listener;
    }

    public void setOnConfirmarPagoClickListener(OnConfirmarPagoClickListener listener) {
        this.onConfirmarPagoClickListener = listener;
    }

    public void setOnConfirmarEnvioClickListener(OnConfirmarEnvioClickListener listener) {
        this.onConfirmarEnvioClickListener = listener;
    }

    public void updateList(List<Venta> nuevosPedidos) {
        this.pedidos.clear();
        if (nuevosPedidos != null) {
            this.pedidos.addAll(nuevosPedidos);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Venta pedido = pedidos.get(position);
        holder.bind(pedido);
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCliente, txtFecha, txtTotal, txtEstadoPedido, txtEstadoPago;
        MaterialButton btnCompletar, btnCancelar, btnConfirmarPago, btnConfirmarEnvio;
        CardView cardPedido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardPedido = itemView.findViewById(R.id.cardPedido);
            txtCliente = itemView.findViewById(R.id.txtCliente);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtEstadoPedido = itemView.findViewById(R.id.txtEstadoPedido);
            txtEstadoPago = itemView.findViewById(R.id.txtEstadoPago);
            btnCompletar = itemView.findViewById(R.id.btnCompletar);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
            btnConfirmarPago = itemView.findViewById(R.id.btnConfirmarPago);
            btnConfirmarEnvio = itemView.findViewById(R.id.btnConfirmarEnvio);
        }

        public void bind(Venta pedido) {
            // Cliente
            if (pedido.getCliente() != null) {
                txtCliente.setText(pedido.getCliente().getNombre());
            } else {
                txtCliente.setText("Cliente #" + pedido.getClienteId());
            }

            // Fecha
            if (pedido.getFecha() != null) {
                txtFecha.setText("Fecha: " + pedido.getFecha());
            }

            // Total
            if (pedido.getTotal() != null) {
                txtTotal.setText(String.format(Locale.getDefault(), "Total: Bs. %.2f", pedido.getTotal()));
            }

            // Estados
            txtEstadoPedido.setText("Estado: " + (pedido.getEstadoPedido() != null ? pedido.getEstadoPedido() : "N/A"));
            txtEstadoPago.setText("Pago: " + (pedido.getEstadoPago() != null ? pedido.getEstadoPago() : "N/A"));

            // Mostrar/ocultar botones según el estado
            boolean esPendiente = "pendiente".equals(pedido.getEstadoPedido());
            boolean pagoPendiente = "pendiente".equals(pedido.getEstadoPago());
            boolean esEnvio = pedido.getEsEnvio() != null && pedido.getEsEnvio();

            btnCompletar.setVisibility(esPendiente ? View.VISIBLE : View.GONE);
            btnCancelar.setVisibility(esPendiente ? View.VISIBLE : View.GONE);
            btnConfirmarPago.setVisibility(pagoPendiente ? View.VISIBLE : View.GONE);
            btnConfirmarEnvio.setVisibility(esEnvio && esPendiente ? View.VISIBLE : View.GONE);

            btnCompletar.setOnClickListener(v -> {
                if (onCompletarClickListener != null) {
                    onCompletarClickListener.onCompletarClick(pedido);
                }
            });

            btnCancelar.setOnClickListener(v -> {
                if (onCancelarClickListener != null) {
                    onCancelarClickListener.onCancelarClick(pedido);
                }
            });

            btnConfirmarPago.setOnClickListener(v -> {
                if (onConfirmarPagoClickListener != null) {
                    onConfirmarPagoClickListener.onConfirmarPagoClick(pedido);
                }
            });

            btnConfirmarEnvio.setOnClickListener(v -> {
                if (onConfirmarEnvioClickListener != null) {
                    onConfirmarEnvioClickListener.onConfirmarEnvioClick(pedido);
                }
            });
        }
    }
}

