package com.example.bizly1.sucursales;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.data.utils.SyncManager;
import com.example.bizly1.models.Sucursal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SucursalFormActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private Marker currentMarker;
    private LatLng selectedLocation;
    private TextInputEditText txtNombreSucursal, txtDireccion, txtCiudad, txtDepartamento, txtTelefono; // Changed to TextInputEditText
    private MaterialButton btnGuardarSucursal; // Changed type
    private ImageButton btnBack; // Added
    private TextView headerTitle; // Added

    private DBHelper dbHelper;
    private SyncManager syncManager;
    private Integer empresaId = 1; // TODO: Obtener del usuario logueado
    private Integer sucursalId = null; // null = nuevo, != null = edición

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursal_form);

        dbHelper = new DBHelper(this);
        syncManager = new SyncManager(this);

        // Obtener ID si viene de edición
        sucursalId = getIntent().getIntExtra("sucursal_id", -1);
        if (sucursalId == -1) {
            sucursalId = null;
        }

        inicializarVistas();
        
        btnBack.setOnClickListener(v -> finish());
        btnGuardarSucursal.setOnClickListener(v -> guardarSucursal());

        if (sucursalId != null) {
            headerTitle.setText("Editar Sucursal");
            cargarSucursal();
        } else {
            headerTitle.setText("Nueva Sucursal");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void inicializarVistas() {
        headerTitle = findViewById(R.id.headerTitle);
        btnBack = findViewById(R.id.btnBack);
        txtNombreSucursal = findViewById(R.id.txtNombreSucursal);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtDepartamento = findViewById(R.id.txtDepartamento);
        txtTelefono = findViewById(R.id.txtTelefono);
        btnGuardarSucursal = findViewById(R.id.btnGuardarSucursal);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        // Cargar todas las sucursales guardadas y mostrarlas en el mapa
        cargarMarcadoresGuardados();

        // Ubicación por defecto (Santa Cruz, Bolivia)
        LatLng santaCruz = new LatLng(-17.8146, -63.1561);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(santaCruz, 12f));

        // Configurar listener para cuando el usuario mantiene presionado
        myMap.setOnMapLongClickListener(latLng -> {
            selectedLocation = latLng;
            // Eliminar marcador temporal anterior si existe
            if (currentMarker != null) {
                currentMarker.remove();
            }
            // Agregar nuevo marcador temporal (azul) para la nueva ubicación
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title("Nueva Sucursal")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            currentMarker = myMap.addMarker(markerOptions);
            Toast.makeText(this, "Ubicación seleccionada", Toast.LENGTH_SHORT).show();
        });
    }

    // Cargar todos los marcadores de las sucursales guardadas
    private void cargarMarcadoresGuardados() {
        // Obtener todas las sucursales guardadas
        List<Sucursal> sucursales = dbHelper.obtenerTodasSucursales();

        // Agregar un marcador para cada sucursal guardada
        for (Sucursal sucursal : sucursales) {
            if (sucursal.getLatitud() != null && sucursal.getLongitud() != null) {
                LatLng posicion = new LatLng(sucursal.getLatitud(), sucursal.getLongitud());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(posicion)
                        .title(sucursal.getNombre())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                myMap.addMarker(markerOptions);
            }
        }
    }

    private void cargarSucursal() {
        if (sucursalId == null) return;

        Sucursal sucursal = dbHelper.obtenerSucursal(sucursalId);
        if (sucursal != null) {
            txtNombreSucursal.setText(sucursal.getNombre());
            txtDireccion.setText(sucursal.getDireccion());
            txtCiudad.setText(sucursal.getCiudad());
            txtDepartamento.setText(sucursal.getDepartamento());
            txtTelefono.setText(sucursal.getTelefono());

            if (sucursal.getLatitud() != null && sucursal.getLongitud() != null) {
                selectedLocation = new LatLng(sucursal.getLatitud(), sucursal.getLongitud());
                // Mover cámara a la ubicación de la sucursal
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15f));
                // Agregar marcador
                if (currentMarker != null) {
                    currentMarker.remove();
                }
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(selectedLocation)
                        .title(sucursal.getNombre())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                currentMarker = myMap.addMarker(markerOptions);
            }
        }
    }

    private void guardarSucursal() {
        // Obtener el nombre de la sucursal
        String nombre = txtNombreSucursal.getText().toString().trim();

        // Validar que el nombre no esté vacío
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingrese un nombre para la sucursal", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que haya seleccionado una ubicación en el mapa
        if (selectedLocation == null) {
            Toast.makeText(this, "Seleccione una ubicación en el mapa (mantenga presionado)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto sucursal
        Sucursal sucursal = new Sucursal();
        if (sucursalId != null) {
            sucursal.setId(sucursalId);
        }
        sucursal.setEmpresaId(empresaId);
        sucursal.setNombre(nombre);
        sucursal.setDireccion(txtDireccion.getText().toString().trim());
        sucursal.setCiudad(txtCiudad.getText().toString().trim());
        sucursal.setDepartamento(txtDepartamento.getText().toString().trim());
        sucursal.setTelefono(txtTelefono.getText().toString().trim());
        sucursal.setLatitud(selectedLocation.latitude);
        sucursal.setLongitud(selectedLocation.longitude);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String now = sdf.format(new Date());
        
        if (sucursalId == null) {
            sucursal.setCreatedAt(now);
        }
        sucursal.setUpdatedAt(now);

        // Guardar localmente
        if (sucursalId == null) {
            long id = dbHelper.insertarSucursal(sucursal);
            sucursal.setId((int) id);
        } else {
            dbHelper.actualizarSucursal(sucursal);
        }

        // Agregar a sync queue
        if (NetworkUtils.isNetworkAvailable(this)) {
            syncManager.agregarASyncQueue("Sucursal", sucursalId == null ? "INSERT" : "UPDATE", sucursal.getId(), sucursal);
        }

        // Mostrar mensaje de éxito
        Toast.makeText(this, "Sucursal '" + nombre + "' registrada correctamente", Toast.LENGTH_SHORT).show();

        // Cerrar esta pantalla y volver a la anterior
        finish();
    }
}

