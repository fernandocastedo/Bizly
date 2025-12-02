package com.example.bizly1.data.utils;

import android.content.Context;
import android.util.Log;

import com.example.bizly1.data.database.DBHelper;
import com.example.bizly1.data.network.ApiClient;
import com.example.bizly1.data.network.ApiService;
import com.example.bizly1.data.network.NetworkUtils;
import com.example.bizly1.models.Insumo;
import com.example.bizly1.models.ProductoVenta;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncManager {
    private static final String TAG = "SyncManager";
    private Context context;
    private DBHelper dbHelper;
    private ApiService apiService;
    private Gson gson;
    
    public SyncManager(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
        this.apiService = ApiClient.getApiService();
        this.gson = new Gson();
    }
    
    /**
     * Sincroniza todos los datos desde la API cuando hay conexión
     */
    public void sincronizarTodo() {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Log.d(TAG, "No hay conexión a internet");
            return;
        }
        
        Log.d(TAG, "Iniciando sincronización completa...");
        sincronizarInsumos();
        sincronizarProductosVenta();
    }
    
    /**
     * Sincroniza insumos desde la API (pull)
     */
    public void sincronizarInsumos() {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Log.d(TAG, "No hay conexión para sincronizar insumos");
            return;
        }
        
        apiService.obtenerTodosInsumos().enqueue(new Callback<List<Insumo>>() {
            @Override
            public void onResponse(Call<List<Insumo>> call, Response<List<Insumo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Insumo insumo : response.body()) {
                        // Verificar si existe localmente por server_id o nombre
                        Insumo local = null;
                        if (insumo.getId() != null) {
                            // Buscar por server_id si existe
                            List<Insumo> todos = dbHelper.obtenerTodosInsumos();
                            for (Insumo i : todos) {
                                if (i.getServerId() != null && i.getServerId().equals(insumo.getId())) {
                                    local = i;
                                    break;
                                }
                            }
                        }
                        
                        if (local == null) {
                            // Nuevo insumo, insertar
                            insumo.setServerId(insumo.getId());
                            insumo.setSyncStatus("synced");
                            dbHelper.insertarInsumo(insumo);
                            Log.d(TAG, "Insumo insertado: " + insumo.getNombre());
                        } else {
                            // Actualizar insumo existente
                            insumo.setId(local.getId());
                            insumo.setServerId(insumo.getId());
                            insumo.setSyncStatus("synced");
                            dbHelper.actualizarInsumo(insumo);
                            Log.d(TAG, "Insumo actualizado: " + insumo.getNombre());
                        }
                    }
                    Log.d(TAG, "Insumos sincronizados desde API");
                    // Después de sincronizar todo, procesar la cola pendiente
                    sincronizarPendientes();
                } else {
                    Log.e(TAG, "Error al sincronizar insumos: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<List<Insumo>> call, Throwable t) {
                Log.e(TAG, "Error de red al sincronizar insumos", t);
            }
        });
    }
    
    /**
     * Procesa la cola de sincronización pendiente
     */
    public void sincronizarPendientes() {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Log.d(TAG, "No hay conexión para sincronizar pendientes");
            return;
        }
        
        List<DBHelper.SyncQueueItem> pendientes = dbHelper.obtenerTodosSyncQueue();
        Log.d(TAG, "Procesando " + pendientes.size() + " elementos pendientes");
        
        for (DBHelper.SyncQueueItem item : pendientes) {
            procesarItemSyncQueue(item);
        }
    }
    
    private void procesarItemSyncQueue(DBHelper.SyncQueueItem item) {
        try {
            if ("Insumo".equals(item.getTableName())) {
                procesarInsumoSync(item);
            } else if ("ProductoVenta".equals(item.getTableName())) {
                procesarProductoVentaSync(item);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al procesar item de sync queue", e);
        }
    }
    
    private void procesarInsumoSync(DBHelper.SyncQueueItem item) {
        Insumo insumo = gson.fromJson(item.getJsonData(), Insumo.class);
        
        if ("DELETE".equals(item.getOperation())) {
            // DELETE retorna Call<Void>
            if (insumo.getServerId() != null) {
                Call<Void> call = apiService.eliminarInsumo(insumo.getServerId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            dbHelper.eliminarSyncQueue(item.getId());
                            Log.d(TAG, "Insumo eliminado en servidor: " + item.getOperation());
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, "Error al sincronizar eliminación de insumo", t);
                    }
                });
            } else {
                // Si no tiene server_id, eliminar de la cola
                dbHelper.eliminarSyncQueue(item.getId());
            }
        } else {
            // INSERT y UPDATE retornan Call<Insumo>
            Call<Insumo> call = null;
            switch (item.getOperation()) {
                case "INSERT":
                    call = apiService.crearInsumo(insumo);
                    break;
                case "UPDATE":
                    if (insumo.getServerId() != null) {
                        call = apiService.actualizarInsumo(insumo.getServerId(), insumo);
                    } else {
                        // Si no tiene server_id, intentar como INSERT
                        call = apiService.crearInsumo(insumo);
                    }
                    break;
            }
            
            if (call != null) {
                call.enqueue(new Callback<Insumo>() {
                    @Override
                    public void onResponse(Call<Insumo> call, Response<Insumo> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Actualizar con datos del servidor
                            Insumo insumoServer = response.body();
                            insumoServer.setId(insumo.getId()); // Mantener ID local
                            insumoServer.setServerId(insumoServer.getId());
                            insumoServer.setSyncStatus("synced");
                            dbHelper.actualizarInsumo(insumoServer);
                            dbHelper.eliminarSyncQueue(item.getId());
                            Log.d(TAG, "Insumo sincronizado: " + item.getOperation());
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<Insumo> call, Throwable t) {
                        Log.e(TAG, "Error al sincronizar insumo", t);
                    }
                });
            }
        }
    }
    
    /**
     * Sincroniza productos de venta desde la API (pull)
     */
    public void sincronizarProductosVenta() {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Log.d(TAG, "No hay conexión para sincronizar productos de venta");
            return;
        }
        
        apiService.obtenerTodosProductosVenta().enqueue(new Callback<List<ProductoVenta>>() {
            @Override
            public void onResponse(Call<List<ProductoVenta>> call, Response<List<ProductoVenta>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (ProductoVenta pv : response.body()) {
                        // Verificar si existe localmente por server_id
                        ProductoVenta local = null;
                        if (pv.getId() != null) {
                            List<ProductoVenta> todos = dbHelper.obtenerTodosProductosVenta();
                            for (ProductoVenta p : todos) {
                                if (p.getServerId() != null && p.getServerId().equals(pv.getId())) {
                                    local = p;
                                    break;
                                }
                            }
                        }
                        
                        if (local == null) {
                            // Nuevo producto, insertar
                            pv.setServerId(pv.getId());
                            pv.setSyncStatus("synced");
                            dbHelper.insertarProductoVenta(pv);
                            Log.d(TAG, "ProductoVenta insertado: " + pv.getNombre());
                        } else {
                            // Actualizar producto existente
                            pv.setId(local.getId());
                            pv.setServerId(pv.getId());
                            pv.setSyncStatus("synced");
                            dbHelper.actualizarProductoVenta(pv);
                            Log.d(TAG, "ProductoVenta actualizado: " + pv.getNombre());
                        }
                    }
                    Log.d(TAG, "Productos de venta sincronizados desde API");
                } else {
                    Log.e(TAG, "Error al sincronizar productos de venta: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<List<ProductoVenta>> call, Throwable t) {
                Log.e(TAG, "Error de red al sincronizar productos de venta", t);
            }
        });
    }
    
    private void procesarProductoVentaSync(DBHelper.SyncQueueItem item) {
        ProductoVenta pv = gson.fromJson(item.getJsonData(), ProductoVenta.class);
        
        if ("DELETE".equals(item.getOperation())) {
            if (pv.getServerId() != null) {
                Call<Void> call = apiService.eliminarProductoVenta(pv.getServerId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            dbHelper.eliminarSyncQueue(item.getId());
                            Log.d(TAG, "ProductoVenta eliminado en servidor: " + item.getOperation());
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG, "Error al sincronizar eliminación de ProductoVenta", t);
                    }
                });
            } else {
                dbHelper.eliminarSyncQueue(item.getId());
            }
        } else {
            Call<ProductoVenta> call = null;
            switch (item.getOperation()) {
                case "INSERT":
                    call = apiService.crearProductoVenta(pv);
                    break;
                case "UPDATE":
                    if (pv.getServerId() != null) {
                        call = apiService.actualizarProductoVenta(pv.getServerId(), pv);
                    } else {
                        call = apiService.crearProductoVenta(pv);
                    }
                    break;
            }
            
            if (call != null) {
                call.enqueue(new Callback<ProductoVenta>() {
                    @Override
                    public void onResponse(Call<ProductoVenta> call, Response<ProductoVenta> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ProductoVenta pvServer = response.body();
                            pvServer.setId(pv.getId());
                            pvServer.setServerId(pvServer.getId());
                            pvServer.setSyncStatus("synced");
                            dbHelper.actualizarProductoVenta(pvServer);
                            dbHelper.eliminarSyncQueue(item.getId());
                            Log.d(TAG, "ProductoVenta sincronizado: " + item.getOperation());
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<ProductoVenta> call, Throwable t) {
                        Log.e(TAG, "Error al sincronizar ProductoVenta", t);
                    }
                });
            }
        }
    }
    
    /**
     * Agrega una operación a la cola de sincronización
     */
    public void agregarASyncQueue(String tableName, String operation, Integer recordId, Object object) {
        String jsonData = gson.toJson(object);
        dbHelper.agregarASyncQueue(tableName, operation, recordId, jsonData);
        Log.d(TAG, "Agregado a sync queue: " + tableName + " - " + operation);
    }
}

