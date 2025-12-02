package com.example.bizly1.ventas;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.data.network.ApiClient;
import com.example.bizly1.data.network.ApiService;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.data.utils.SyncManager;
import com.example.bizly1.models.Cliente;
import com.example.bizly1.models.ProductoVenta;
import com.example.bizly1.models.Venta;
import com.example.bizly1.models.VentaProducto;
import com.example.bizly1.ventas.adapters.ProductoVentaSeleccionadoAdapter;
import com.example.bizly1.ventas.dialogs.SeleccionarProductoDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentasActivity extends AppCompatActivity {

    // Cliente
    private TextInputLayout layoutNit, layoutNombreCliente, layoutDireccion, layoutTelefono, layoutEmail;
    private TextInputEditText etNit, etNombreCliente, etDireccion, etTelefono, etEmail;
    private MaterialButton btnExpandirCliente;
    private CheckBox checkPedido;
    private View containerCamposAdicionales, containerCamposEnvio;
    private boolean camposExpandidos = false;
    private Cliente clienteActual = null;

    // Productos
    private RecyclerView recyclerViewProductos;
    private TextView txtTotal, txtEmptyProductos, txtCambio;
    private TextInputEditText etRecibi;
    private MaterialButton btnAgregarProducto;
    private List<VentaProducto> productosSeleccionados = new ArrayList<>();
    private ProductoVentaSeleccionadoAdapter adapter;

    // Venta
    private Spinner spinnerMetodoPago;
    private Spinner spinnerEstadoPago;
    private MaterialButton btnGuardarVenta;
    private Toolbar toolbar;

    private DBHelper dbHelper;
    private SyncManager syncManager;
    private ApiService apiService;
    private Integer empresaId = 1; // TODO: Obtener del usuario logueado
    private Integer usuarioId = 1; // TODO: Obtener del usuario logueado
    private Integer sucursalId = 1; // TODO: Obtener de la configuración

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        dbHelper = new DBHelper(this);
        syncManager = new SyncManager(this);
        apiService = ApiClient.getApiService();

        inicializarVistas();
        configurarToolbar();
        configurarListeners();
        configurarAdapter();
        configurarSpinners();
    }

    private void inicializarVistas() {
        toolbar = findViewById(R.id.toolbar);

        // Cliente
        layoutNit = findViewById(R.id.layoutNit);
        layoutNombreCliente = findViewById(R.id.layoutNombreCliente);
        layoutDireccion = findViewById(R.id.layoutDireccion);
        layoutTelefono = findViewById(R.id.layoutTelefono);
        layoutEmail = findViewById(R.id.layoutEmail);
        etNit = findViewById(R.id.etNit);
        etNombreCliente = findViewById(R.id.etNombreCliente);
        etDireccion = findViewById(R.id.etDireccion);
        etTelefono = findViewById(R.id.etTelefono);
        etEmail = findViewById(R.id.etEmail);
        btnExpandirCliente = findViewById(R.id.btnExpandirCliente);
        checkPedido = findViewById(R.id.checkPedido);
        containerCamposAdicionales = findViewById(R.id.containerCamposAdicionales);
        containerCamposEnvio = findViewById(R.id.containerCamposEnvio);

        // Productos
        recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
        txtTotal = findViewById(R.id.txtTotal);
        txtEmptyProductos = findViewById(R.id.txtEmptyProductos);
        btnAgregarProducto = findViewById(R.id.btnAgregarProducto);
        etRecibi = findViewById(R.id.etRecibi);
        txtCambio = findViewById(R.id.txtCambio);

        // Venta
        spinnerMetodoPago = findViewById(R.id.spinnerMetodoPago);
        spinnerEstadoPago = findViewById(R.id.spinnerEstadoPago);
        btnGuardarVenta = findViewById(R.id.btnGuardarVenta);

        // Inicialmente ocultar campos adicionales
        containerCamposAdicionales.setVisibility(View.GONE);
        containerCamposEnvio.setVisibility(View.GONE);
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Nueva Venta");
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
        adapter = new ProductoVentaSeleccionadoAdapter(productosSeleccionados);
        adapter.setOnDeleteClickListener(position -> {
            productosSeleccionados.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, productosSeleccionados.size());
            actualizarTotal();
            actualizarEmptyMessage();
        });
        adapter.setOnCantidadChangeListener(() -> actualizarTotal());

        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProductos.setAdapter(adapter);
    }

    private void configurarSpinners() {
        // Método de pago
        ArrayAdapter<CharSequence> metodoPagoAdapter = ArrayAdapter.createFromResource(
                this, R.array.metodos_pago, android.R.layout.simple_spinner_item);
        metodoPagoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMetodoPago.setAdapter(metodoPagoAdapter);

        // Estado de pago
        ArrayAdapter<CharSequence> estadoPagoAdapter = ArrayAdapter.createFromResource(
                this, R.array.estados_pago, android.R.layout.simple_spinner_item);
        estadoPagoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstadoPago.setAdapter(estadoPagoAdapter);
    }

    private void configurarListeners() {
        // Buscar cliente por NIT
        etNit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nitStr = s.toString().trim();
                if (nitStr.length() > 0) {
                    try {
                        Integer nit = Integer.parseInt(nitStr);
                        buscarClientePorNit(nit);
                    } catch (NumberFormatException e) {
                        // Ignorar
                    }
                } else {
                    // Limpiar si se borra el NIT
                    clienteActual = null;
                    etNombreCliente.setText("");
                    etNombreCliente.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Botón expandir campos adicionales
        btnExpandirCliente.setOnClickListener(v -> {
            camposExpandidos = !camposExpandidos;
            if (camposExpandidos) {
                containerCamposAdicionales.setVisibility(View.VISIBLE);
                btnExpandirCliente.setText("-");
            } else {
                containerCamposAdicionales.setVisibility(View.GONE);
                btnExpandirCliente.setText("+");
            }
        });

        // Checkbox Pedido (Envío)
        checkPedido.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                containerCamposEnvio.setVisibility(View.VISIBLE);
                // Si no están expandidos, expandir automáticamente
                if (!camposExpandidos) {
                    camposExpandidos = true;
                    containerCamposAdicionales.setVisibility(View.VISIBLE);
                    btnExpandirCliente.setText("-");
                }
            } else {
                containerCamposEnvio.setVisibility(View.GONE);
            }
        });

        // Agregar producto
        btnAgregarProducto.setOnClickListener(v -> {
            SeleccionarProductoDialog dialog = new SeleccionarProductoDialog(
                    this,
                    dbHelper.obtenerProductosVentaActivos(),
                    productoVenta -> {
                        // Verificar si ya está agregado
                        for (VentaProducto vp : productosSeleccionados) {
                            if (vp.getProductoVentaId().equals(productoVenta.getId())) {
                                Toast.makeText(this, "El producto ya está agregado", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        // Agregar nuevo producto
                        VentaProducto vp = new VentaProducto();
                        vp.setProductoVentaId(productoVenta.getId());
                        vp.setCantidad(1.0);
                        vp.setPrecioUnitario(productoVenta.getPrecioVenta());
                        vp.setProductoVenta(productoVenta);
                        productosSeleccionados.add(vp);
                        adapter.notifyItemInserted(productosSeleccionados.size() - 1);
                        actualizarTotal();
                        actualizarEmptyMessage();
                    }
            );
            dialog.show();
        });

        // Guardar venta
        btnGuardarVenta.setOnClickListener(v -> guardarVenta());

        // Calcular cambio cuando se ingresa el monto recibido
        etRecibi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcularCambio();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void buscarClientePorNit(Integer nit) {
        Cliente cliente = dbHelper.obtenerClientePorNit(nit);
        if (cliente != null) {
            clienteActual = cliente;
            etNombreCliente.setText(cliente.getNombre());
            etNombreCliente.setEnabled(false);
            if (cliente.getDireccion() != null) {
                etDireccion.setText(cliente.getDireccion());
            }
            if (cliente.getTelefono() != null) {
                etTelefono.setText(cliente.getTelefono());
            }
            if (cliente.getEmail() != null) {
                etEmail.setText(cliente.getEmail());
            }
        } else {
            clienteActual = null;
            etNombreCliente.setText("");
            etNombreCliente.setEnabled(true);
        }
    }

    private void actualizarTotal() {
        double total = 0.0;
        for (VentaProducto vp : productosSeleccionados) {
            total += vp.getSubtotal();
        }
        txtTotal.setText(String.format(Locale.getDefault(), "Total: Bs. %.2f", total));
        calcularCambio();
    }

    private void calcularCambio() {
        // Calcular total
        double total = 0.0;
        for (VentaProducto vp : productosSeleccionados) {
            total += vp.getSubtotal();
        }

        // Obtener monto recibido
        String recibidoStr = etRecibi.getText().toString().trim();
        double recibido = 0.0;
        if (!recibidoStr.isEmpty()) {
            try {
                recibido = Double.parseDouble(recibidoStr);
            } catch (NumberFormatException e) {
                recibido = 0.0;
            }
        }

        // Calcular cambio
        double cambio = recibido - total;
        if (cambio < 0) {
            cambio = 0.0;
        }

        txtCambio.setText(String.format(Locale.getDefault(), "Cambio: Bs. %.2f", cambio));
    }

    private void actualizarEmptyMessage() {
        if (productosSeleccionados.isEmpty()) {
            txtEmptyProductos.setVisibility(View.VISIBLE);
            recyclerViewProductos.setVisibility(View.GONE);
        } else {
            txtEmptyProductos.setVisibility(View.GONE);
            recyclerViewProductos.setVisibility(View.VISIBLE);
        }
    }

    private void guardarVenta() {
        // Validaciones
        String nitStr = etNit.getText().toString().trim();
        String nombreCliente = etNombreCliente.getText().toString().trim();

        if (nitStr.isEmpty() || nombreCliente.isEmpty()) {
            Toast.makeText(this, "Complete los datos del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        if (productosSeleccionados.isEmpty()) {
            Toast.makeText(this, "Agregue al menos un producto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar o actualizar cliente
        Cliente cliente = clienteActual;
        if (cliente == null) {
            // Crear nuevo cliente
            cliente = new Cliente();
            cliente.setEmpresaId(empresaId);
            cliente.setSucursalId(sucursalId);
            try {
                cliente.setNit(Integer.parseInt(nitStr));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "NIT inválido", Toast.LENGTH_SHORT).show();
                return;
            }
            cliente.setNombre(nombreCliente);
            cliente.setDireccion(etDireccion.getText().toString().trim());
            cliente.setTelefono(etTelefono.getText().toString().trim());
            cliente.setEmail(etEmail.getText().toString().trim());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            cliente.setCreatedAt(sdf.format(new Date()));

            long clienteId = dbHelper.insertarCliente(cliente);
            cliente.setId((int) clienteId);

            // Agregar a sync queue
            if (NetworkUtils.isNetworkAvailable(this)) {
                syncManager.agregarASyncQueue("Cliente", "INSERT", cliente.getId(), cliente);
            }
        } else {
            // Actualizar cliente existente
            cliente.setDireccion(etDireccion.getText().toString().trim());
            cliente.setTelefono(etTelefono.getText().toString().trim());
            cliente.setEmail(etEmail.getText().toString().trim());
            dbHelper.actualizarCliente(cliente);

            // Agregar a sync queue
            if (NetworkUtils.isNetworkAvailable(this)) {
                syncManager.agregarASyncQueue("Cliente", "UPDATE", cliente.getId(), cliente);
            }
        }

        // Crear venta
        Venta venta = new Venta();
        venta.setEmpresaId(empresaId);
        venta.setSucursalId(sucursalId);
        venta.setUsuarioId(usuarioId);
        venta.setClienteId(cliente.getId());
        venta.setEsEnvio(checkPedido.isChecked());
        venta.setMetodoPago(spinnerMetodoPago.getSelectedItem().toString());
        
        String estadoPago = spinnerEstadoPago.getSelectedItem().toString();
        venta.setEstadoPago(estadoPago.toLowerCase());
        
        // Si es envío o pago pendiente, estado_pedido = "pendiente"
        if (venta.getEsEnvio() || "pendiente".equals(venta.getEstadoPago())) {
            venta.setEstadoPedido("pendiente");
        } else {
            venta.setEstadoPedido("completado");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String now = sdf.format(new Date());
        venta.setFecha(now);
        venta.setCreatedAt(now);

        // Calcular total
        double total = 0.0;
        for (VentaProducto vp : productosSeleccionados) {
            total += vp.getSubtotal();
        }
        venta.setTotal(total);

        // Guardar venta localmente
        long ventaId = dbHelper.insertarVenta(venta);
        venta.setId((int) ventaId);

        // Guardar productos de la venta
        for (VentaProducto vp : productosSeleccionados) {
            vp.setVentaId((int) ventaId);
            dbHelper.insertarVentaProducto(vp);
        }

        // Agregar a sync queue
        if (NetworkUtils.isNetworkAvailable(this)) {
            syncManager.agregarASyncQueue("Venta", "INSERT", venta.getId(), venta);
        }

        Toast.makeText(this, "Venta registrada exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}

