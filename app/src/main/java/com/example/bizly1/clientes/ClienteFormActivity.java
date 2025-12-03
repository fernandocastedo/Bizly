package com.example.bizly1.clientes;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.data.utils.SyncManager;
import com.example.bizly1.models.Cliente;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClienteFormActivity extends AppCompatActivity {

    private TextInputLayout layoutNombre, layoutNit, layoutTelefono, layoutEmail, layoutDireccion;
    private TextInputEditText etNombre, etNit, etTelefono, etEmail, etDireccion;
    private MaterialButton btnGuardar;
    private android.widget.TextView headerTitle;
    private android.widget.ImageButton backButton;
    
    private DBHelper dbHelper;
    private SyncManager syncManager;
    private Integer empresaId = 1; // TODO: Obtener del usuario logueado
    private Integer sucursalId = 1; // TODO: Obtener de la configuración
    private Integer clienteId = null; // null = nuevo, != null = edición

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_form);

        dbHelper = new DBHelper(this);
        syncManager = new SyncManager(this);

        // Obtener ID si viene de edición
        clienteId = getIntent().getIntExtra("cliente_id", -1);
        if (clienteId == -1) {
            clienteId = null;
        }

        inicializarVistas();
        
        // Configurar Header
        headerTitle.setText(clienteId != null ? "Editar Cliente" : "Nuevo Cliente");
        backButton.setOnClickListener(v -> finish());
        
        configurarListeners();

        if (clienteId != null) {
            cargarCliente();
        }
    }

    private void inicializarVistas() {
        headerTitle = findViewById(R.id.headerTitle);
        backButton = findViewById(R.id.backButton);
        
        layoutNombre = findViewById(R.id.layoutNombre);
        layoutNit = findViewById(R.id.layoutNit);
        layoutTelefono = findViewById(R.id.layoutTelefono);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutDireccion = findViewById(R.id.layoutDireccion);
        etNombre = findViewById(R.id.etNombre);
        etNit = findViewById(R.id.etNit);
        etTelefono = findViewById(R.id.etTelefono);
        etEmail = findViewById(R.id.etEmail);
        etDireccion = findViewById(R.id.etDireccion);
        btnGuardar = findViewById(R.id.btnGuardar);
    }



    private void configurarListeners() {
        btnGuardar.setOnClickListener(v -> guardarCliente());
    }

    private void cargarCliente() {
        if (clienteId == null) return;

        Cliente cliente = dbHelper.obtenerCliente(clienteId);
        if (cliente != null) {
            etNombre.setText(cliente.getNombre());
            if (cliente.getNit() != null) {
                etNit.setText(String.valueOf(cliente.getNit()));
            }
            etTelefono.setText(cliente.getTelefono());
            etEmail.setText(cliente.getEmail());
            etDireccion.setText(cliente.getDireccion());
        }
    }

    private void guardarCliente() {
        // Validaciones
        String nombre = etNombre.getText().toString().trim();
        String nitStr = etNit.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingrese el nombre del cliente", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el NIT ya existe (solo para nuevos clientes)
        if (clienteId == null && !nitStr.isEmpty()) {
            try {
                Integer nit = Integer.parseInt(nitStr);
                Cliente clienteExistente = dbHelper.obtenerClientePorNit(nit);
                if (clienteExistente != null) {
                    Toast.makeText(this, "Ya existe un cliente con este NIT", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "NIT inválido", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Crear o actualizar cliente
        Cliente cliente;
        if (clienteId != null) {
            cliente = dbHelper.obtenerCliente(clienteId);
        } else {
            cliente = new Cliente();
        }

        cliente.setEmpresaId(empresaId);
        cliente.setSucursalId(sucursalId);
        cliente.setNombre(nombre);
        
        if (!nitStr.isEmpty()) {
            try {
                cliente.setNit(Integer.parseInt(nitStr));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "NIT inválido", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        
        cliente.setTelefono(etTelefono.getText().toString().trim());
        cliente.setEmail(etEmail.getText().toString().trim());
        cliente.setDireccion(etDireccion.getText().toString().trim());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String now = sdf.format(new Date());
        
        if (clienteId == null) {
            cliente.setCreatedAt(now);
        }

        // Guardar localmente
        if (clienteId == null) {
            long id = dbHelper.insertarCliente(cliente);
            cliente.setId((int) id);
        } else {
            dbHelper.actualizarCliente(cliente);
        }

        // Agregar a sync queue
        if (NetworkUtils.isNetworkAvailable(this)) {
            syncManager.agregarASyncQueue("Cliente", clienteId == null ? "INSERT" : "UPDATE", cliente.getId(), cliente);
        }

        Toast.makeText(this, "Cliente guardado exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}

