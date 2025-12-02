package com.example.bizly1.ventas.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.VentaProducto;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Locale;

public class ProductoVentaSeleccionadoAdapter extends RecyclerView.Adapter<ProductoVentaSeleccionadoAdapter.ViewHolder> {

    private List<VentaProducto> productos;
    private OnDeleteClickListener onDeleteClickListener;
    private OnCantidadChangeListener onCantidadChangeListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnCantidadChangeListener {
        void onCantidadChanged();
    }

    public ProductoVentaSeleccionadoAdapter(List<VentaProducto> productos) {
        this.productos = productos;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setOnCantidadChangeListener(OnCantidadChangeListener listener) {
        this.onCantidadChangeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto_venta_seleccionado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VentaProducto vp = productos.get(position);
        holder.bind(vp, position);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPrecioUnitario, txtSubtotal;
        TextInputEditText etCantidad;
        ImageButton btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPrecioUnitario = itemView.findViewById(R.id.txtPrecioUnitario);
            txtSubtotal = itemView.findViewById(R.id.txtSubtotal);
            etCantidad = itemView.findViewById(R.id.etCantidad);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }

        public void bind(VentaProducto vp, int position) {
            if (vp.getProductoVenta() != null) {
                txtNombre.setText(vp.getProductoVenta().getNombre());
            } else {
                txtNombre.setText("Producto #" + vp.getProductoVentaId());
            }

            if (vp.getPrecioUnitario() != null) {
                txtPrecioUnitario.setText(String.format(Locale.getDefault(), "Bs. %.2f c/u", vp.getPrecioUnitario()));
            }

            if (vp.getCantidad() != null) {
                etCantidad.setText(String.format(Locale.getDefault(), "%.0f", vp.getCantidad()));
            }

            actualizarSubtotal(vp);

            etCantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String cantidadStr = s.toString().trim();
                    if (!cantidadStr.isEmpty()) {
                        try {
                            double cantidad = Double.parseDouble(cantidadStr);
                            if (cantidad > 0) {
                                vp.setCantidad(cantidad);
                                actualizarSubtotal(vp);
                                if (onCantidadChangeListener != null) {
                                    onCantidadChangeListener.onCantidadChanged();
                                }
                            }
                        } catch (NumberFormatException e) {
                            // Ignorar
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            btnEliminar.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(position);
                }
            });
        }

        private void actualizarSubtotal(VentaProducto vp) {
            if (vp.getSubtotal() != null) {
                txtSubtotal.setText(String.format(Locale.getDefault(), "Subtotal: Bs. %.2f", vp.getSubtotal()));
            }
        }
    }
}

