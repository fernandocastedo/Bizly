package com.bizly.app.presentation.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Clase base para todas las Activities del proyecto.
 * Proporciona funcionalidad común y configuración base.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivity();
    }

    /**
     * Configuración inicial de la Activity.
     * Puede ser sobrescrito por las clases hijas.
     */
    protected void setupActivity() {
        // Configuración común para todas las activities
    }

    /**
     * Método para mostrar mensajes de error
     */
    protected void showError(String message) {
        // Implementar según necesidad (Toast, Snackbar, etc.)
    }

    /**
     * Método para mostrar mensajes de éxito
     */
    protected void showSuccess(String message) {
        // Implementar según necesidad
    }

    /**
     * Método para mostrar loading
     */
    protected void showLoading(boolean show) {
        // Implementar según necesidad
    }
}

