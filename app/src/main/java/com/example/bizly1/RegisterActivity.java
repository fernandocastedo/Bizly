package com.example.bizly1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private CheckBox termsCheckbox;
    private MaterialButton continueButton;
    private View backButton;
    private TextView termsLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        termsCheckbox = findViewById(R.id.termsCheckbox);
        continueButton = findViewById(R.id.continueButton);
        backButton = findViewById(R.id.backButton);
        termsLink = findViewById(R.id.termsLink);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        termsLink.setOnClickListener(v -> {
            // Aquí puedes abrir los términos y condiciones
            // Por ejemplo, abrir una URL o una nueva Activity
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com/terms"));
            startActivity(intent);
        });

        continueButton.setOnClickListener(v -> {
            if (validateInputs()) {
                // Aquí puedes agregar la lógica para registrar al usuario
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean validateInputs() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (email.isEmpty()) {
            emailEditText.setError("Por favor ingresa tu correo electrónico");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Por favor ingresa un correo electrónico válido");
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Por favor ingresa tu contraseña");
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("La contraseña debe tener al menos 6 caracteres");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Por favor confirma tu contraseña");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Las contraseñas no coinciden");
            return false;
        }

        if (!termsCheckbox.isChecked()) {
            Toast.makeText(this, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
