package com.example.bizly1.ventas;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.data.utils.SyncManager;
import com.example.bizly1.models.Venta;
import com.example.bizly1.ventas.adapters.PedidoAdapter;

import java.util.List;

public class PedidosPendientesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPedidos;
    private TextView txtEmpty;
    private Toolbar toolbar;
    private DBHelper dbHelper;
    private SyncManager syncManager;
    private PedidoAdapter adapter;
    private List<Venta> pedidosPendientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_pendientes);

        dbHelper = new DBHelper(this);
        syncManager = new SyncManager(this);

        inicializarVistas();
        configurarToolbar();
        configurarAdapter();
        cargarPedidos();
    }

    private void inicializarVistas() {
        toolbar = findViewById(R.id.toolbar);
        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos);
        txtEmpty = findViewById(R.id.txtEmpty);
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pedidos Pendientes");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configurarAdapter() {
        adapter = new PedidoAdapter(pedidosPendientes);
        adapter.setOnCompletarClickListener(venta -> mostrarDialogoCompletar(venta));
        adapter.setOnCancelarClickListener(venta -> mostrarDialogoCancelar(venta));
        adapter.setOnConfirmarPagoClickListener(venta -> confirmarPago(venta));
        adapter.setOnConfirmarEnvioClickListener(venta -> confirmarEnvio(venta));

        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPedidos.setAdapter(adapter);
    }

    private void cargarPedidos() {
        pedidosPendientes = dbHelper.obtenerVentasPendientes();
        adapter.updateList(pedidosPendientes);
        actualizarEmptyMessage();
    }

    private void actualizarEmptyMessage() {
        if (pedidosPendientes.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerViewPedidos.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerViewPedidos.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarDialogoCompletar(Venta venta) {
        new AlertDialog.Builder(this)
                .setTitle("Completar Pedido")
                .setMessage("¿Desea marcar este pedido como completado?")
                .setPositiveButton("Sí", (dialog, which) -> completarPedido(venta))
                .setNegativeButton("No", null)
                .show();
    }

    private void mostrarDialogoCancelar(Venta venta) {
        new AlertDialog.Builder(this)
                .setTitle("Cancelar Pedido")
                .setMessage("¿Está seguro de cancelar este pedido?")
                .setPositiveButton("Sí", (dialog, which) -> cancelarPedido(venta))
                .setNegativeButton("No", null)
                .show();
    }

    private void completarPedido(Venta venta) {
        venta.setEstadoPedido("completado");
        if ("pendiente".equals(venta.getEstadoPago())) {
            venta.setEstadoPago("pagado");
        }
        dbHelper.actualizarVenta(venta);

        if (NetworkUtils.isNetworkAvailable(this)) {
            syncManager.agregarASyncQueue("Venta", "UPDATE", venta.getId(), venta);
        }

        Toast.makeText(this, "Pedido completado", Toast.LENGTH_SHORT).show();
        cargarPedidos();
    }

    private void cancelarPedido(Venta venta) {
        venta.setEstadoPedido("cancelado");
        dbHelper.actualizarVenta(venta);

        if (NetworkUtils.isNetworkAvailable(this)) {
            syncManager.agregarASyncQueue("Venta", "UPDATE", venta.getId(), venta);
        }

        Toast.makeText(this, "Pedido cancelado", Toast.LENGTH_SHORT).show();
        cargarPedidos();
    }

    private void confirmarPago(Venta venta) {
        venta.setEstadoPago("pagado");
        if ("pendiente".equals(venta.getEstadoPedido()) && !venta.getEsEnvio()) {
            venta.setEstadoPedido("completado");
        }
        dbHelper.actualizarVenta(venta);

        if (NetworkUtils.isNetworkAvailable(this)) {
            syncManager.agregarASyncQueue("Venta", "UPDATE", venta.getId(), venta);
        }

        Toast.makeText(this, "Pago confirmado", Toast.LENGTH_SHORT).show();
        cargarPedidos();
    }

    private void confirmarEnvio(Venta venta) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Envío")
                .setMessage("¿Se realizó el envío del pedido?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    venta.setEstadoPedido("completado");
                    if ("pendiente".equals(venta.getEstadoPago())) {
                        venta.setEstadoPago("pagado");
                    }
                    dbHelper.actualizarVenta(venta);

                    if (NetworkUtils.isNetworkAvailable(this)) {
                        syncManager.agregarASyncQueue("Venta", "UPDATE", venta.getId(), venta);
                    }

                    Toast.makeText(this, "Envío confirmado", Toast.LENGTH_SHORT).show();
                    cargarPedidos();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPedidos();
    }
}

