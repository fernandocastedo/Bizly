package com.example.bizly1;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bizly1.adapter.ImageAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private RecyclerView rvImages;
    private ImageAdapter imageAdapter;
    private List<Uri> imageUris;
    private MaterialButton btnAddImage, btnScan, btnBottomSave, btnTopSave;
    private ImageButton btnBack;
    private SwitchMaterial switchCompound;
    private LinearLayout layoutIngredients;
    private Spinner spinnerUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize Views
        rvImages = findViewById(R.id.rvImages);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnScan = findViewById(R.id.btnScan);
        btnBottomSave = findViewById(R.id.btnBottomSave);
        btnTopSave = findViewById(R.id.btnTopSave);
        btnBack = findViewById(R.id.btnBack);
        switchCompound = findViewById(R.id.switchCompound);
        layoutIngredients = findViewById(R.id.layoutIngredients);
        spinnerUnit = findViewById(R.id.spinnerUnit);

        // Setup Back Button
        btnBack.setOnClickListener(v -> finish());

        // Setup Image Carousel
        imageUris = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageUris, position -> {
            imageUris.remove(position);
            imageAdapter.notifyItemRemoved(position);
        });
        rvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvImages.setAdapter(imageAdapter);

        btnAddImage.setOnClickListener(v -> {
            if (imageUris.size() < 5) {
                // Mock adding an image URI (In real app, open gallery intent)
                // Using a placeholder resource URI for demo
                Uri mockUri = Uri.parse("android.resource://" + getPackageName() + "/" + android.R.drawable.ic_menu_gallery);
                imageUris.add(mockUri);
                imageAdapter.notifyItemInserted(imageUris.size() - 1);
            } else {
                Toast.makeText(this, "Máximo 5 imágenes", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup Scan Button
        btnScan.setOnClickListener(v -> {
            Toast.makeText(this, "Abrir Cámara / Escáner", Toast.LENGTH_SHORT).show();
        });

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Kg", "Un", "Lt", "g"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);

        // Setup Compound Product Switch
        switchCompound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                layoutIngredients.setVisibility(View.VISIBLE);
            } else {
                layoutIngredients.setVisibility(View.GONE);
            }
        });

        // Setup Save Buttons
        View.OnClickListener saveListener = v -> {
            Toast.makeText(this, "Producto Guardado", Toast.LENGTH_SHORT).show();
            finish();
        };
        btnBottomSave.setOnClickListener(saveListener);
        btnTopSave.setOnClickListener(saveListener);
    }
}


