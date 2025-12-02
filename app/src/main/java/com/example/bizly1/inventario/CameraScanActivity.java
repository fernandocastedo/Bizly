package com.example.bizly1.inventario;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.example.bizly1.R;
import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.data.utils.SyncManager;
import com.example.bizly1.inventario.dialogs.ProductFormDialog;
import com.example.bizly1.models.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CameraScanActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private static final String TAG = "CameraScanActivity";
    
    private DBHelper dbHelper;
    private SyncManager syncManager;
    private Integer empresaId = 1; // TODO: Obtener del usuario logueado
    private boolean modoRestock = false; // true = re-stock, false = nuevo producto

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) startCamera();
                else {
                    Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_scan);

        dbHelper = new DBHelper(this);
        syncManager = new SyncManager(this);
        
        // Verificar si viene en modo re-stock
        modoRestock = getIntent().getBooleanExtra("modo_restock", false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(modoRestock ? "Re-stock por Cámara" : "Escanear Factura");
        }

        previewView = findViewById(R.id.previewView);
        com.google.android.material.button.MaterialButton btnCapture = findViewById(R.id.btnCapture);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

        cameraExecutor = Executors.newSingleThreadExecutor();
        btnCapture.setOnClickListener(v -> capturePhoto());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder().build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Error iniciando cámara: ", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void capturePhoto() {
        if (imageCapture == null) return;

        imageCapture.takePicture(ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy imageProxy) {
                        processImageProxy(imageProxy);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(CameraScanActivity.this, "Error al capturar: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void processImageProxy(ImageProxy imageProxy) {
        try {
            InputImage image = InputImage.fromMediaImage(
                    imageProxy.getImage(),
                    imageProxy.getImageInfo().getRotationDegrees()
            );

            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    .process(image)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text result) {
                            handleTextRecognitionResult(result);
                            imageProxy.close();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CameraScanActivity.this, "Fallo OCR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            imageProxy.close();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            imageProxy.close();
        }
    }

    private void handleTextRecognitionResult(Text result) {
        // ---- MODO TEXTO PLANO: Producto: X, Cantidad: Y, Precio unitario: Z, Precio Total: W ----
        String fullText = result.getText().toLowerCase();

        // Detectar claves simples tipo "producto: chocolate" (modo texto plano)
        if (fullText.contains("producto:")) {
            String name = "", quantity = "", unit = "", unitPrice = "", totalPrice = "";

            Matcher mProd = Pattern.compile("producto[:\\s]+([a-zA-Z0-9 áéíóúñ\\-]+)").matcher(fullText);
            if (mProd.find()) name = mProd.group(1).trim();

            Matcher mCant = Pattern.compile("cantidad[:\\s]+([0-9]+)").matcher(fullText);
            if (mCant.find()) quantity = mCant.group(1).trim();

            Matcher mUnit = Pattern.compile("precio\\s*unitario[:\\s]+([0-9.,]+)").matcher(fullText);
            if (mUnit.find()) unitPrice = mUnit.group(1).trim();

            Matcher mTot = Pattern.compile("(precio\\s*total|total)[:\\s]+([0-9.,]+)").matcher(fullText);
            if (mTot.find()) totalPrice = mTot.group(2).trim();

            // Normalizar números
            quantity = quantity.replace(",", ".");
            unitPrice = unitPrice.replace(",", ".");
            totalPrice = totalPrice.replace(",", ".");

            // Calcular si falta algún dato
            try {
                double q = Double.parseDouble(quantity.isEmpty() ? "0" : quantity);
                double up = Double.parseDouble(unitPrice.isEmpty() ? "0" : unitPrice);
                double tp = Double.parseDouble(totalPrice.isEmpty() ? "0" : totalPrice);
                if (tp == 0 && q > 0 && up > 0) tp = q * up;
                if (up == 0 && tp > 0 && q > 0) up = tp / q;
                
                List<Product> products = new ArrayList<>();
                products.add(new Product(name.isEmpty() ? "Producto detectado" : name, q, unit, up, tp, "General"));
                
                // Si es modo re-stock, buscar coincidencias
                if (modoRestock) {
                    procesarRestock(products);
                } else {
                    ProductFormDialog.showMultiple(
                            this,
                            products,
                            dbHelper,
                            syncManager,
                            empresaId,
                            () -> {
                                Toast.makeText(this, "Producto agregado (texto plano)", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                    );
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error al procesar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // --- MODO FACTURA: Detectar múltiples productos ---
        String rawText = result.getText();
        Log.d(TAG, "Texto original OCR: " + rawText);
        
        String processedText = rawText
                .replace(",", ".")
                .replaceAll("\\s{2,}", " ")
                .toUpperCase();

        Log.d(TAG, "Texto procesado para detección: " + processedText);

        List<Product> detectedProducts = new ArrayList<>();

        // Dividir el texto en líneas
        String[] lines = processedText.split("\n");
        Log.d(TAG, "Total de líneas detectadas: " + lines.length);

        // Patrones más flexibles para detectar productos
        // Patrón 1: Nombre + Cantidad + "unidades" + Precio Unitario + Total (ej: "CHOCLO 5 UNIDADES 10 50")
        Pattern pattern1 = Pattern.compile("([A-ZÁÉÍÓÚÑ][A-ZÁÉÍÓÚÑ\\- ]{2,})\\s+(\\d+[.,]?\\d*)\\s+(?:UNIDADES?|U\\.?|KG|L|LT|ML|PZA|PAQUETE)?\\s+(\\d+[.,]?\\d*)\\s+(\\d+[.,]?\\d*)");
        
        // Patrón 2: Nombre + Cantidad + Precio Unitario + Total (ej: "CHOCLO 5 10 50")
        Pattern pattern2 = Pattern.compile("([A-ZÁÉÍÓÚÑ][A-ZÁÉÍÓÚÑ\\- ]{2,})\\s+(\\d+[.,]?\\d*)\\s+(\\d+[.,]?\\d*)\\s+(\\d+[.,]?\\d*)");
        
        // Patrón 3: Nombre + Cantidad + Precio (ej: "CHOCLO 5 10")
        Pattern pattern3 = Pattern.compile("([A-ZÁÉÍÓÚÑ][A-ZÁÉÍÓÚÑ\\- ]{2,})\\s+(\\d+[.,]?\\d*)\\s+(\\d+[.,]?\\d*)");
        
        // Patrón 4: Más flexible - extrae todos los números de una línea (último recurso)
        Pattern pattern4 = Pattern.compile("([A-ZÁÉÍÓÚÑ][A-ZÁÉÍÓÚÑ\\s\\-]{2,})(?:\\s+.*?)?(\\d+[.,]?\\d*)(?:\\s+.*?)?(\\d+[.,]?\\d*)(?:\\s+.*?)?(\\d+[.,]?\\d*)?");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.length() < 5) continue;

            // Evitar líneas con encabezados o totales
            if (line.contains("PRODUCTO") && line.contains("CANTIDAD") && line.contains("PRECIO")) {
                continue; // Es el encabezado de la tabla
            }
            if (line.matches(".*(TOTAL|SUBTOTAL|NIT|FECHA|CASA|MATRIZ|IMPORTE|MONTO).*")) {
                continue;
            }

            Log.d(TAG, "Procesando línea: " + line);

            String productName = "";
            String quantity = "";
            String unitPrice = "";
            String totalPrice = "";
            String unit = "";

            // Intentar con patrón 1 (con "unidades" y total)
            Matcher m1 = pattern1.matcher(line);
            if (m1.find()) {
                productName = m1.group(1).trim();
                quantity = m1.group(2);
                unitPrice = m1.group(3);
                totalPrice = m1.group(4);
                Log.d(TAG, "✓ Patrón 1 coincide: " + productName + " | Cant: " + quantity + " | Precio: " + unitPrice + " | Total: " + totalPrice);
            } else {
                // Intentar con patrón 2 (sin "unidades" pero con total)
                Matcher m2 = pattern2.matcher(line);
                if (m2.find()) {
                    productName = m2.group(1).trim();
                    quantity = m2.group(2);
                    unitPrice = m2.group(3);
                    totalPrice = m2.group(4);
                    Log.d(TAG, "✓ Patrón 2 coincide: " + productName + " | Cant: " + quantity + " | Precio: " + unitPrice + " | Total: " + totalPrice);
                } else {
                    // Intentar con patrón 3 (sin total, solo precio unitario)
                    Matcher m3 = pattern3.matcher(line);
                    if (m3.find()) {
                        productName = m3.group(1).trim();
                        quantity = m3.group(2);
                        unitPrice = m3.group(3);
                        Log.d(TAG, "✓ Patrón 3 coincide: " + productName + " | Cant: " + quantity + " | Precio: " + unitPrice);
                    } else {
                        // Patrón 4: más flexible como último recurso
                        Matcher m4 = pattern4.matcher(line);
                        if (m4.find()) {
                            productName = m4.group(1).trim();
                            quantity = m4.group(2);
                            unitPrice = m4.group(3);
                            Log.d(TAG, "✓ Patrón 4 coincide: " + productName + " | Cant: " + quantity + " | Precio: " + unitPrice);
                        }
                    }
                }
            }

            // Si encontramos un producto válido
            if (!productName.isEmpty() && !quantity.isEmpty() && !unitPrice.isEmpty()) {
                try {
                    // Limpiar y parsear números
                    quantity = quantity.replaceAll("[^0-9.]", "");
                    unitPrice = unitPrice.replaceAll("[^0-9.]", "");
                    totalPrice = totalPrice.replaceAll("[^0-9.]", "");

                    double q = Double.parseDouble(quantity);
                    double up = Double.parseDouble(unitPrice);
                    double tp = 0;

                    if (!totalPrice.isEmpty()) {
                        tp = Double.parseDouble(totalPrice);
                    } else {
                        // Calcular total si no está presente
                        tp = q * up;
                    }

                    // Validar que tenga datos válidos
                    if (q > 0 && up > 0 && !productName.trim().isEmpty()) {
                        // Limpiar nombre del producto (remover números al final si es necesario)
                        productName = productName.replaceAll("\\s+\\d+$", "").trim();
                        productName = productName.replaceAll("\\s+UNIDADES?$", "").trim();
                        productName = productName.replaceAll("\\s+KG$", "").trim();
                        productName = productName.replaceAll("\\s+L$", "").trim();
                        
                        // Verificar que el nombre no sea solo números o espacios
                        if (!productName.matches("^[\\d\\s]+$") && productName.length() >= 2) {
                            detectedProducts.add(new Product(productName, q, unit, up, tp, "General"));
                            Log.d(TAG, "✓ Producto agregado: " + productName + " - Cant: " + q + " - Precio Unit: " + up + " - Total: " + tp);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parseando línea: " + line + " - " + e.getMessage());
                }
            } else {
                // Método alternativo: extraer todos los números de la línea y tratar de identificar
                Pattern numbersPattern = Pattern.compile("\\d+[.,]?\\d*");
                Matcher numbersMatcher = numbersPattern.matcher(line);
                List<String> numbers = new ArrayList<>();
                while (numbersMatcher.find()) {
                    numbers.add(numbersMatcher.group());
                }
                
                // Si hay al menos 2 números y la línea tiene texto, puede ser un producto
                if (numbers.size() >= 2 && line.matches(".*[A-ZÁÉÍÓÚÑ].*")) {
                    // Extraer nombre (todo antes del primer número)
                    String[] parts = line.split("\\d");
                    if (parts.length > 0) {
                        String possibleName = parts[0].trim();
                        possibleName = possibleName.replaceAll("(UNIDADES?|U\\.?|KG|L|LT|ML|PZA|PAQUETE)$", "").trim();
                        
                        if (possibleName.length() >= 2 && !possibleName.matches(".*(PRODUCTO|CANTIDAD|PRECIO|TOTAL).*")) {
                            try {
                                double q = Double.parseDouble(numbers.get(0).replace(",", "."));
                                double up = Double.parseDouble(numbers.get(1).replace(",", "."));
                                double tp = numbers.size() >= 3 ? Double.parseDouble(numbers.get(2).replace(",", ".")) : q * up;
                                
                                if (q > 0 && up > 0) {
                                    detectedProducts.add(new Product(possibleName, q, unit, up, tp, "General"));
                                    Log.d(TAG, "✓ Producto agregado (método alternativo): " + possibleName + " - Cant: " + q + " - Precio: " + up);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error en método alternativo: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }

        Log.d(TAG, "Total de productos detectados: " + detectedProducts.size());

        // Si no se detectaron productos, intentar con un solo producto genérico
        if (detectedProducts.isEmpty()) {
            Log.d(TAG, "No se detectaron productos con el patrón, creando producto genérico");
            String name = "Producto no detectado";
            String quantity = "1";
            String unit = "";
            String unitPrice = "";
            String totalPrice = "";

            // Buscar total
            Pattern totalPattern = Pattern.compile("(TOTAL|IMPORTE|A PAGAR|MONTO)\\s*(BS|BOLIVIANOS|B\\.|BO)?\\s*[:\\-]?\\s*([0-9.]+)");
            Matcher totalMatcher = totalPattern.matcher(rawText);
            if (totalMatcher.find()) {
                totalPrice = totalMatcher.group(3);
            }

            try {
                double q = 1.0;
                double up = 0;
                double tp = totalPrice.isEmpty() ? 0 : Double.parseDouble(totalPrice);
                
                if (tp > 0) {
                    up = tp / q;
                }
                
                detectedProducts.add(new Product(name, q, unit, up, tp, "General"));
                Log.d(TAG, "Producto genérico creado");
            } catch (Exception e) {
                // Si todo falla, crear un producto vacío
                detectedProducts.add(new Product("Producto detectado", 1.0, "", 0, 0, "General"));
                Log.d(TAG, "Producto vacío creado como fallback");
            }
        }

        // Mostrar modal con productos detectados (SIEMPRE con lista)
        Log.d(TAG, "Mostrando modal con " + detectedProducts.size() + " producto(s)");
        
        // Si es modo re-stock, buscar coincidencias
        if (modoRestock) {
            procesarRestock(detectedProducts);
        } else {
            ProductFormDialog.showMultiple(
                    this,
                    detectedProducts,
                    dbHelper,
                    syncManager,
                    empresaId,
                    () -> {
                        Toast.makeText(this, "Productos agregados al inventario", Toast.LENGTH_SHORT).show();
                        finish();
                    }
            );
        }
    }

    private void procesarRestock(List<Product> detectedProducts) {
        // TODO: Implementar lógica de re-stock
        // Por ahora, mostrar el diálogo normal
        ProductFormDialog.showMultiple(
                this,
                detectedProducts,
                dbHelper,
                syncManager,
                empresaId,
                () -> {
                    Toast.makeText(this, "Stock actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) cameraExecutor.shutdown();
    }
}

