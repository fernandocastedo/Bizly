package com.example.bizly1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Locale;

public class AddSupplyActivity extends AppCompatActivity {

    private TextInputEditText etPrice, etQuantity;
    private TextView tvTotalPrice;
    private Spinner spinnerUnit;
    private MaterialButton btnBottomAdd, btnTopSave;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supply);

        // Initialize Views
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        btnBottomAdd = findViewById(R.id.btnBottomAdd);
        btnTopSave = findViewById(R.id.btnTopSave);
        btnBack = findViewById(R.id.btnBack);

        // Setup Back Button
        btnBack.setOnClickListener(v -> finish());

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Kg", "Un", "Lt", "g"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);

        // Setup Price Calculation Logic
        TextWatcher calculationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etPrice.addTextChangedListener(calculationWatcher);
        etQuantity.addTextChangedListener(calculationWatcher);

        // Setup Save/Add Buttons
        btnBottomAdd.setOnClickListener(v -> saveSupply());
        btnTopSave.setOnClickListener(v -> saveSupply());
    }

    private void calculateTotal() {
        String priceStr = etPrice.getText().toString();
        String qtyStr = etQuantity.getText().toString();

        if (!priceStr.isEmpty() && !qtyStr.isEmpty()) {
            try {
                double price = Double.parseDouble(priceStr);
                double quantity = Double.parseDouble(qtyStr);
                double total = price * quantity;
                tvTotalPrice.setText(String.format(Locale.getDefault(), "%.2f Bs.", total));
            } catch (NumberFormatException e) {
                tvTotalPrice.setText("0.00 Bs.");
            }
        } else {
            tvTotalPrice.setText("0.00 Bs.");
        }
    }

    private void saveSupply() {
        // TODO: Save supply logic
        Toast.makeText(this, "Insumo Agregado", Toast.LENGTH_SHORT).show();
        finish();
    }
}




