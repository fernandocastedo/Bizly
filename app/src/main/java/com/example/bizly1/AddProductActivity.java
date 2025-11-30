package com.example.bizly1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bizly1.R;
import com.example.bizly1.adapter.ImageAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private RecyclerView rvImages;
    private ImageAdapter imageAdapter;
    private List<Uri> imageUris;
    private View btnAddImage;
    private MaterialButton btnSave;
    private ImageButton btnBack;
    private SwitchMaterial switchCompound;
    private LinearLayout layoutIngredients;
    private Spinner spinnerUnit;
    
    // ActivityResultLauncher for image selection
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize Views
        rvImages = findViewById(R.id.rvImages);
        btnAddImage = findViewById(R.id.btnAddImage);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        switchCompound = findViewById(R.id.switchCompound);
        layoutIngredients = findViewById(R.id.layoutIngredients);
        spinnerUnit = findViewById(R.id.spinnerUnit);

        // Setup Back Button
        btnBack.setOnClickListener(v -> finish());

        // Setup Image Grid
        imageUris = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageUris, position -> {
            imageUris.remove(position);
            imageAdapter.notifyItemRemoved(position);
        });
        rvImages.setLayoutManager(new GridLayoutManager(this, 2));
        rvImages.setAdapter(imageAdapter);

        // Setup Image Picker Launcher
        imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        if (imageUris.size() < 6) {
                            imageUris.add(selectedImageUri);
                            imageAdapter.notifyItemInserted(imageUris.size() - 1);
                        } else {
                            Toast.makeText(this, "Máximo 6 imágenes", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        );

        btnAddImage.setOnClickListener(v -> {
            if (imageUris.size() < 6) {
                // Open gallery to select image
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                imagePickerLauncher.launch(intent);
            } else {
                Toast.makeText(this, "Máximo 6 imágenes", Toast.LENGTH_SHORT).show();
            }
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

        // Setup Save Button
        btnSave.setOnClickListener(v -> {
            Toast.makeText(this, "Producto Guardado", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}