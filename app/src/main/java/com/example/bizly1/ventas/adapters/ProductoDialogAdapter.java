package com.example.bizly1.ventas.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.ProductoVenta;

import java.util.List;
import java.util.Locale;

public class ProductoDialogAdapter extends RecyclerView.Adapter<ProductoDialogAdapter.ViewHolder> {

    private List<ProductoVenta> productos;
    private OnProductoClickListener listener;

    public interface OnProductoClickListener {
        void onProductoClick(ProductoVenta producto);
    }

    public ProductoDialogAdapter(List<ProductoVenta> productos) {
        this.productos = productos;
    }

    public void setOnProductoClickListener(OnProductoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto_dialog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductoVenta producto = productos.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPrecio;
        CardView cardProducto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardProducto = itemView.findViewById(R.id.cardProducto);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
        }

        public void bind(ProductoVenta producto) {
            txtNombre.setText(producto.getNombre());
            if (producto.getPrecioVenta() != null) {
                txtPrecio.setText(String.format(Locale.getDefault(), "Bs. %.2f", producto.getPrecioVenta()));
            } else {
                txtPrecio.setText("Bs. 0.00");
            }

            cardProducto.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductoClick(producto);
                }
            });
        }
    }
}

