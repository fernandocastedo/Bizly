package com.example.bizly1.productos.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.models.ProductoVenta;

import java.util.List;

public class ProductoVentaAdapter extends RecyclerView.Adapter<ProductoVentaAdapter.ProductoVentaViewHolder> {

    private List<ProductoVenta> productos;
    private OnProductoVentaClickListener listener;

    public interface OnProductoVentaClickListener {
        void onProductoVentaClick(ProductoVenta productoVenta);
        void onEditClick(ProductoVenta productoVenta);
        void onDeleteClick(ProductoVenta productoVenta);
        void onDesactivarClick(ProductoVenta productoVenta);
    }

    public ProductoVentaAdapter(List<ProductoVenta> productos, OnProductoVentaClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoVentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto_venta, parent, false);
        return new ProductoVentaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoVentaViewHolder holder, int position) {
        ProductoVenta producto = productos.get(position);
        holder.bind(producto);
    }

    @Override
    public int getItemCount() {
        return productos != null ? productos.size() : 0;
    }

    public void updateList(List<ProductoVenta> nuevosProductos) {
        this.productos = nuevosProductos;
        notifyDataSetChanged();
    }

    class ProductoVentaViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNombre, txtDescripcion, txtPrecioVenta, txtCostoTotal, 
                        txtMargenGanancia, txtStockAlerta, txtEstado;
        private CardView cardProducto;
        private Button btnEdit, btnDelete, btnDesactivar;

        public ProductoVentaViewHolder(@NonNull View itemView) {
            super(itemView);
            cardProducto = itemView.findViewById(R.id.cardProducto);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtPrecioVenta = itemView.findViewById(R.id.txtPrecioVenta);
            txtCostoTotal = itemView.findViewById(R.id.txtCostoTotal);
            txtMargenGanancia = itemView.findViewById(R.id.txtMargenGanancia);
            txtStockAlerta = itemView.findViewById(R.id.txtStockAlerta);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDesactivar = itemView.findViewById(R.id.btnDesactivar);
        }

        public void bind(ProductoVenta producto) {
            txtNombre.setText(producto.getNombre());
            txtDescripcion.setText(producto.getDescripcion() != null ? producto.getDescripcion() : "");
            
            if (producto.getPrecioVenta() != null) {
                txtPrecioVenta.setText(String.format("Bs. %.2f", producto.getPrecioVenta()));
            } else {
                txtPrecioVenta.setText("Bs. 0.00");
            }
            
            if (producto.getCostoTotal() != null) {
                txtCostoTotal.setText(String.format("Costo: Bs. %.2f", producto.getCostoTotal()));
            } else {
                txtCostoTotal.setText("Costo: Bs. 0.00");
            }
            
            if (producto.getMargenGanancia() != null) {
                txtMargenGanancia.setText(String.format("Margen: %.1f%%", producto.getMargenGanancia()));
            } else {
                txtMargenGanancia.setText("Margen: 0%");
            }

            // Verificar stock y estado
            boolean tieneStock = producto.tieneStockSuficiente();
            boolean activo = producto.getActivo() != null && producto.getActivo();
            
            if (!tieneStock) {
                txtStockAlerta.setVisibility(View.VISIBLE);
                txtStockAlerta.setText("⚠ Sin stock suficiente");
                cardProducto.setCardBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.holo_red_light, null));
            } else {
                txtStockAlerta.setVisibility(View.GONE);
                cardProducto.setCardBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.white, null));
            }
            
            if (activo) {
                txtEstado.setText("Activo");
                txtEstado.setTextColor(itemView.getContext().getResources().getColor(android.R.color.holo_green_dark, null));
                btnDesactivar.setText("Desactivar");
            } else {
                txtEstado.setText("Inactivo");
                txtEstado.setTextColor(itemView.getContext().getResources().getColor(android.R.color.darker_gray, null));
                btnDesactivar.setText("Activar");
            }

            // Listeners
            cardProducto.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductoVentaClick(producto);
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(producto);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(producto);
                }
            });

            btnDesactivar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDesactivarClick(producto);
                }
            });
        }
    }
}

