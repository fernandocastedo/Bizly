package com.example.bizly1.costos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.data.utils.SyncManager;
import com.example.bizly1.models.CostoGasto;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CostoGastoFormActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout layoutDescripcion, layoutMonto, layoutCategoria, layoutComprobante;
    private TextInputEditText etDescripcion, etMonto, etCategoria, etComprobante, etFecha;
    private Spinner spinnerTipo, spinnerMetodoPago;
    private MaterialButton btnGuardar, btnSeleccionarFecha;
    
    private DBHelper dbHelper;
    private SyncManager syncManager;
    private Integer empresaId = 1; // TODO: Obtener del usuario logueado
    private Integer sucursalId = 1; // TODO: Obtener de la configuración
    private Integer costoGastoId = null; // null = nuevo, != null = edición
    private String tipoPredeterminado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costo_gasto_form);

        dbHelper = new DBHelper(this);
        syncManager = new SyncManager(this);

        // Obtener tipo predeterminado si viene del botón
        tipoPredeterminado = getIntent().getStringExtra("tipo");
        
        // Obtener ID si viene de edición
        costoGastoId = getIntent().getIntExtra("costo_gasto_id", -1);
        if (costoGastoId == -1) {
            costoGastoId = null;
        }

        inicializarVistas();
        configurarToolbar();
        configurarSpinners();
        configurarListeners();

        if (costoGastoId != null) {
            cargarCostoGasto();
        } else if (tipoPredeterminado != null) {
            // Establecer el tipo predeterminado
            for (int i = 0; i < spinnerTipo.getCount(); i++) {
                if (spinnerTipo.getItemAtPosition(i).toString().toLowerCase().equals(tipoPredeterminado)) {
                    spinnerTipo.setSelection(i);
                    break;
                }
            }
        }
    }

    private void inicializarVistas() {
        toolbar = findViewById(R.id.toolbar);
        layoutDescripcion = findViewById(R.id.layoutDescripcion);
        layoutMonto = findViewById(R.id.layoutMonto);
        layoutCategoria = findViewById(R.id.layoutCategoria);
        layoutComprobante = findViewById(R.id.layoutComprobante);
        etDescripcion = findViewById(R.id.etDescripcion);
        etMonto = findViewById(R.id.etMonto);
        etCategoria = findViewById(R.id.etCategoria);
        etComprobante = findViewById(R.id.etComprobante);
        etFecha = findViewById(R.id.etFecha);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerMetodoPago = findViewById(R.id.spinnerMetodoPago);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(costoGastoId != null ? "Editar Registro" : "Nuevo Registro");
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

    private void configurarSpinners() {
        // Tipo
        ArrayAdapter<CharSequence> tipoAdapter = ArrayAdapter.createFromResource(
                this, R.array.tipos_costo_gasto, android.R.layout.simple_spinner_item);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(tipoAdapter);

        // Método de pago
        ArrayAdapter<CharSequence> metodoPagoAdapter = ArrayAdapter.createFromResource(
                this, R.array.metodos_pago, android.R.layout.simple_spinner_item);
        metodoPagoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMetodoPago.setAdapter(metodoPagoAdapter);
    }

    private void configurarListeners() {
        // Fecha - establecer fecha actual por defecto
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etFecha.setText(sdf.format(new Date()));

        btnSeleccionarFecha.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        etFecha.setText(sdf.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        btnGuardar.setOnClickListener(v -> guardarCostoGasto());
    }

    private void cargarCostoGasto() {
        if (costoGastoId == null) return;

        CostoGasto cg = dbHelper.obtenerCostoGasto(costoGastoId);
        if (cg != null) {
            etDescripcion.setText(cg.getDescripcion());
            if (cg.getMonto() != null) {
                etMonto.setText(String.format(Locale.getDefault(), "%.2f", cg.getMonto()));
            }
            etCategoria.setText(cg.getCategoria());
            etComprobante.setText(cg.getComprobante());
            if (cg.getFecha() != null) {
                etFecha.setText(cg.getFecha());
            }

            // Establecer tipo
            for (int i = 0; i < spinnerTipo.getCount(); i++) {
                if (spinnerTipo.getItemAtPosition(i).toString().toLowerCase().equals(cg.getTipo())) {
                    spinnerTipo.setSelection(i);
                    break;
                }
            }

            // Establecer método de pago
            if (cg.getMetodoPago() != null) {
                for (int i = 0; i < spinnerMetodoPago.getCount(); i++) {
                    if (spinnerMetodoPago.getItemAtPosition(i).toString().equals(cg.getMetodoPago())) {
                        spinnerMetodoPago.setSelection(i);
                        break;
                    }
                }
            }
        }
    }

    private void guardarCostoGasto() {
        // Validaciones
        String descripcion = etDescripcion.getText().toString().trim();
        String montoStr = etMonto.getText().toString().trim();
        String fecha = etFecha.getText().toString().trim();

        if (descripcion.isEmpty()) {
            Toast.makeText(this, "Ingrese una descripción", Toast.LENGTH_SHORT).show();
            return;
        }

        if (montoStr.isEmpty()) {
            Toast.makeText(this, "Ingrese un monto", Toast.LENGTH_SHORT).show();
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
            if (monto <= 0) {
                Toast.makeText(this, "El monto debe ser mayor a cero", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Monto inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fecha.isEmpty()) {
            Toast.makeText(this, "Seleccione una fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto CostoGasto
        CostoGasto costoGasto = new CostoGasto();
        if (costoGastoId != null) {
            costoGasto.setId(costoGastoId);
        }
        costoGasto.setEmpresaId(empresaId);
        costoGasto.setSucursalId(sucursalId);
        costoGasto.setTipo(spinnerTipo.getSelectedItem().toString().toLowerCase());
        costoGasto.setCategoria(etCategoria.getText().toString().trim());
        costoGasto.setDescripcion(descripcion);
        costoGasto.setMonto(monto);
        costoGasto.setFecha(fecha);
        costoGasto.setMetodoPago(spinnerMetodoPago.getSelectedItem().toString());
        costoGasto.setComprobante(etComprobante.getText().toString().trim());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String now = sdf.format(new Date());
        
        if (costoGastoId == null) {
            costoGasto.setCreatedAt(now);
        }
        costoGasto.setUpdatedAt(now);

        // Guardar localmente
        if (costoGastoId == null) {
            long id = dbHelper.insertarCostoGasto(costoGasto);
            costoGasto.setId((int) id);
        } else {
            dbHelper.actualizarCostoGasto(costoGasto);
        }

        // Agregar a sync queue
        if (NetworkUtils.isNetworkAvailable(this)) {
            syncManager.agregarASyncQueue("CostoGasto", costoGastoId == null ? "INSERT" : "UPDATE", costoGasto.getId(), costoGasto);
        }

        Toast.makeText(this, "Registro guardado exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}

