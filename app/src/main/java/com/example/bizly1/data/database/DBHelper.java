package com.example.bizly1.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bizly1.models.Insumo;
import com.example.bizly1.models.ProductoVenta;
import com.example.bizly1.models.ProductoVentaInsumo;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bizlyDB.db";
    private static final int DATABASE_VERSION = 2;

    // Tablas
    private static final String TABLE_INSUMOS = "insumos";
    private static final String TABLE_PRODUCTOS_VENTA = "productos_venta";
    private static final String TABLE_PRODUCTO_VENTA_INSUMO = "producto_venta_insumo";
    private static final String TABLE_SYNC_QUEUE = "sync_queue";

    // Columnas Insumos
    private static final String COL_INSUMO_ID = "id";
    private static final String COL_INSUMO_EMPRESA_ID = "empresa_id";
    private static final String COL_INSUMO_NOMBRE = "nombre";
    private static final String COL_INSUMO_DESCRIPCION = "descripcion";
    private static final String COL_INSUMO_CANTIDAD = "cantidad";
    private static final String COL_INSUMO_UNIDAD_MEDIDA = "unidad_medida";
    private static final String COL_INSUMO_PRECIO_UNITARIO = "precio_unitario";
    private static final String COL_INSUMO_PRECIO_TOTAL = "precio_total";
    private static final String COL_INSUMO_CATEGORIA = "categoria";
    private static final String COL_INSUMO_STOCK_MINIMO = "stock_minimo";
    private static final String COL_INSUMO_CREATED_AT = "created_at";
    private static final String COL_INSUMO_UPDATED_AT = "updated_at";
    private static final String COL_INSUMO_SYNC_STATUS = "sync_status";
    private static final String COL_INSUMO_SERVER_ID = "server_id";

    // Columnas ProductosVenta
    private static final String COL_PV_ID = "id";
    private static final String COL_PV_EMPRESA_ID = "empresa_id";
    private static final String COL_PV_NOMBRE = "nombre";
    private static final String COL_PV_DESCRIPCION = "descripcion";
    private static final String COL_PV_PRECIO_VENTA = "precio_venta";
    private static final String COL_PV_COSTO_TOTAL = "costo_total";
    private static final String COL_PV_MARGEN_GANANCIA = "margen_ganancia";
    private static final String COL_PV_ACTIVO = "activo";
    private static final String COL_PV_CREATED_AT = "created_at";
    private static final String COL_PV_UPDATED_AT = "updated_at";
    private static final String COL_PV_SYNC_STATUS = "sync_status";
    private static final String COL_PV_SERVER_ID = "server_id";

    // Columnas ProductoVentaInsumo
    private static final String COL_PVI_ID = "id";
    private static final String COL_PVI_PRODUCTO_VENTA_ID = "producto_venta_id";
    private static final String COL_PVI_INSUMO_ID = "insumo_id";
    private static final String COL_PVI_CANTIDAD_USADA = "cantidad_usada";

    // Columnas SyncQueue
    private static final String COL_SYNC_ID = "id";
    private static final String COL_SYNC_TABLE_NAME = "table_name";
    private static final String COL_SYNC_OPERATION = "operation";
    private static final String COL_SYNC_RECORD_ID = "record_id";
    private static final String COL_SYNC_JSON_DATA = "json_data";
    private static final String COL_SYNC_CREATED_AT = "created_at";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla Insumos
        String createInsumosTable = "CREATE TABLE " + TABLE_INSUMOS + " (" +
                COL_INSUMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_INSUMO_EMPRESA_ID + " INTEGER, " +
                COL_INSUMO_NOMBRE + " TEXT NOT NULL, " +
                COL_INSUMO_DESCRIPCION + " TEXT, " +
                COL_INSUMO_CANTIDAD + " REAL NOT NULL, " +
                COL_INSUMO_UNIDAD_MEDIDA + " TEXT, " +
                COL_INSUMO_PRECIO_UNITARIO + " REAL, " +
                COL_INSUMO_PRECIO_TOTAL + " REAL, " +
                COL_INSUMO_CATEGORIA + " TEXT, " +
                COL_INSUMO_STOCK_MINIMO + " REAL, " +
                COL_INSUMO_CREATED_AT + " TEXT, " +
                COL_INSUMO_UPDATED_AT + " TEXT, " +
                COL_INSUMO_SYNC_STATUS + " TEXT DEFAULT 'pending', " +
                COL_INSUMO_SERVER_ID + " INTEGER" +
                ")";

        db.execSQL(createInsumosTable);

        // Tabla ProductosVenta
        String createProductosVentaTable = "CREATE TABLE " + TABLE_PRODUCTOS_VENTA + " (" +
                COL_PV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PV_EMPRESA_ID + " INTEGER, " +
                COL_PV_NOMBRE + " TEXT NOT NULL, " +
                COL_PV_DESCRIPCION + " TEXT, " +
                COL_PV_PRECIO_VENTA + " REAL, " +
                COL_PV_COSTO_TOTAL + " REAL, " +
                COL_PV_MARGEN_GANANCIA + " REAL, " +
                COL_PV_ACTIVO + " INTEGER DEFAULT 1, " +
                COL_PV_CREATED_AT + " TEXT, " +
                COL_PV_UPDATED_AT + " TEXT, " +
                COL_PV_SYNC_STATUS + " TEXT DEFAULT 'pending', " +
                COL_PV_SERVER_ID + " INTEGER" +
                ")";

        db.execSQL(createProductosVentaTable);

        // Tabla ProductoVentaInsumo
        String createProductoVentaInsumoTable = "CREATE TABLE " + TABLE_PRODUCTO_VENTA_INSUMO + " (" +
                COL_PVI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PVI_PRODUCTO_VENTA_ID + " INTEGER NOT NULL, " +
                COL_PVI_INSUMO_ID + " INTEGER NOT NULL, " +
                COL_PVI_CANTIDAD_USADA + " REAL NOT NULL, " +
                "FOREIGN KEY(" + COL_PVI_PRODUCTO_VENTA_ID + ") REFERENCES " + TABLE_PRODUCTOS_VENTA + "(" + COL_PV_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COL_PVI_INSUMO_ID + ") REFERENCES " + TABLE_INSUMOS + "(" + COL_INSUMO_ID + ") ON DELETE CASCADE" +
                ")";

        db.execSQL(createProductoVentaInsumoTable);

        // Tabla SyncQueue
        String createSyncQueueTable = "CREATE TABLE " + TABLE_SYNC_QUEUE + " (" +
                COL_SYNC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SYNC_TABLE_NAME + " TEXT NOT NULL, " +
                COL_SYNC_OPERATION + " TEXT NOT NULL, " +
                COL_SYNC_RECORD_ID + " INTEGER, " +
                COL_SYNC_JSON_DATA + " TEXT, " +
                COL_SYNC_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP" +
                ")";

        db.execSQL(createSyncQueueTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Crear nuevas tablas para productos de venta
            String createProductosVentaTable = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTOS_VENTA + " (" +
                    COL_PV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_PV_EMPRESA_ID + " INTEGER, " +
                    COL_PV_NOMBRE + " TEXT NOT NULL, " +
                    COL_PV_DESCRIPCION + " TEXT, " +
                    COL_PV_PRECIO_VENTA + " REAL, " +
                    COL_PV_COSTO_TOTAL + " REAL, " +
                    COL_PV_MARGEN_GANANCIA + " REAL, " +
                    COL_PV_ACTIVO + " INTEGER DEFAULT 1, " +
                    COL_PV_CREATED_AT + " TEXT, " +
                    COL_PV_UPDATED_AT + " TEXT, " +
                    COL_PV_SYNC_STATUS + " TEXT DEFAULT 'pending', " +
                    COL_PV_SERVER_ID + " INTEGER" +
                    ")";

            db.execSQL(createProductosVentaTable);

            String createProductoVentaInsumoTable = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTO_VENTA_INSUMO + " (" +
                    COL_PVI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_PVI_PRODUCTO_VENTA_ID + " INTEGER NOT NULL, " +
                    COL_PVI_INSUMO_ID + " INTEGER NOT NULL, " +
                    COL_PVI_CANTIDAD_USADA + " REAL NOT NULL, " +
                    "FOREIGN KEY(" + COL_PVI_PRODUCTO_VENTA_ID + ") REFERENCES " + TABLE_PRODUCTOS_VENTA + "(" + COL_PV_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + COL_PVI_INSUMO_ID + ") REFERENCES " + TABLE_INSUMOS + "(" + COL_INSUMO_ID + ") ON DELETE CASCADE" +
                    ")";

            db.execSQL(createProductoVentaInsumoTable);
        }
    }

    // ==================== MÉTODOS CRUD INSUMOS ====================

    public long insertarInsumo(Insumo insumo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (insumo.getId() != null) {
            values.put(COL_INSUMO_ID, insumo.getId());
        }
        if (insumo.getEmpresaId() != null) {
            values.put(COL_INSUMO_EMPRESA_ID, insumo.getEmpresaId());
        }
        values.put(COL_INSUMO_NOMBRE, insumo.getNombre());
        values.put(COL_INSUMO_DESCRIPCION, insumo.getDescripcion());
        values.put(COL_INSUMO_CANTIDAD, insumo.getCantidad());
        values.put(COL_INSUMO_UNIDAD_MEDIDA, insumo.getUnidadMedida());
        values.put(COL_INSUMO_PRECIO_UNITARIO, insumo.getPrecioUnitario());
        values.put(COL_INSUMO_PRECIO_TOTAL, insumo.getPrecioTotal());
        values.put(COL_INSUMO_CATEGORIA, insumo.getCategoria());
        values.put(COL_INSUMO_STOCK_MINIMO, insumo.getStockMinimo());
        values.put(COL_INSUMO_CREATED_AT, insumo.getCreatedAt());
        values.put(COL_INSUMO_UPDATED_AT, insumo.getUpdatedAt());
        values.put(COL_INSUMO_SYNC_STATUS, insumo.getSyncStatus() != null ? insumo.getSyncStatus() : "pending");
        if (insumo.getServerId() != null) {
            values.put(COL_INSUMO_SERVER_ID, insumo.getServerId());
        }

        long id = db.insert(TABLE_INSUMOS, null, values);
        db.close();
        return id;
    }

    public boolean actualizarInsumo(Insumo insumo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (insumo.getEmpresaId() != null) {
            values.put(COL_INSUMO_EMPRESA_ID, insumo.getEmpresaId());
        }
        values.put(COL_INSUMO_NOMBRE, insumo.getNombre());
        values.put(COL_INSUMO_DESCRIPCION, insumo.getDescripcion());
        values.put(COL_INSUMO_CANTIDAD, insumo.getCantidad());
        values.put(COL_INSUMO_UNIDAD_MEDIDA, insumo.getUnidadMedida());
        values.put(COL_INSUMO_PRECIO_UNITARIO, insumo.getPrecioUnitario());
        values.put(COL_INSUMO_PRECIO_TOTAL, insumo.getPrecioTotal());
        values.put(COL_INSUMO_CATEGORIA, insumo.getCategoria());
        values.put(COL_INSUMO_STOCK_MINIMO, insumo.getStockMinimo());
        values.put(COL_INSUMO_UPDATED_AT, insumo.getUpdatedAt());
        values.put(COL_INSUMO_SYNC_STATUS, insumo.getSyncStatus() != null ? insumo.getSyncStatus() : "pending");
        if (insumo.getServerId() != null) {
            values.put(COL_INSUMO_SERVER_ID, insumo.getServerId());
        }

        int rowsAffected = db.update(TABLE_INSUMOS, values, COL_INSUMO_ID + " = ?",
                new String[]{String.valueOf(insumo.getId())});
        db.close();
        return rowsAffected > 0;
    }

    public boolean eliminarInsumo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_INSUMOS, COL_INSUMO_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public Insumo obtenerInsumo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INSUMOS, null, COL_INSUMO_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Insumo insumo = null;
        if (cursor.moveToFirst()) {
            insumo = cursorToInsumo(cursor);
        }

        cursor.close();
        db.close();
        return insumo;
    }

    public List<Insumo> obtenerTodosInsumos() {
        List<Insumo> insumos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INSUMOS, null, null, null, null, null, COL_INSUMO_NOMBRE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                insumos.add(cursorToInsumo(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return insumos;
    }

    public List<Insumo> buscarInsumoPorNombre(String nombre) {
        List<Insumo> insumos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_INSUMO_NOMBRE + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + nombre + "%"};
        
        Cursor cursor = db.query(TABLE_INSUMOS, null, selection, selectionArgs, null, null, COL_INSUMO_NOMBRE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                insumos.add(cursorToInsumo(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return insumos;
    }

    public List<Insumo> obtenerInsumosPorCategoria(String categoria) {
        List<Insumo> insumos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INSUMOS, null, COL_INSUMO_CATEGORIA + " = ?",
                new String[]{categoria}, null, null, COL_INSUMO_NOMBRE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                insumos.add(cursorToInsumo(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return insumos;
    }

    public List<Insumo> obtenerInsumosStockBajo() {
        List<Insumo> insumos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_INSUMO_STOCK_MINIMO + " IS NOT NULL AND " + 
                          COL_INSUMO_CANTIDAD + " < " + COL_INSUMO_STOCK_MINIMO;
        
        Cursor cursor = db.query(TABLE_INSUMOS, null, selection, null, null, null, COL_INSUMO_NOMBRE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                insumos.add(cursorToInsumo(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return insumos;
    }

    private Insumo cursorToInsumo(Cursor cursor) {
        Insumo insumo = new Insumo();
        
        int idIndex = cursor.getColumnIndex(COL_INSUMO_ID);
        int empresaIdIndex = cursor.getColumnIndex(COL_INSUMO_EMPRESA_ID);
        int nombreIndex = cursor.getColumnIndex(COL_INSUMO_NOMBRE);
        int descripcionIndex = cursor.getColumnIndex(COL_INSUMO_DESCRIPCION);
        int cantidadIndex = cursor.getColumnIndex(COL_INSUMO_CANTIDAD);
        int unidadMedidaIndex = cursor.getColumnIndex(COL_INSUMO_UNIDAD_MEDIDA);
        int precioUnitarioIndex = cursor.getColumnIndex(COL_INSUMO_PRECIO_UNITARIO);
        int precioTotalIndex = cursor.getColumnIndex(COL_INSUMO_PRECIO_TOTAL);
        int categoriaIndex = cursor.getColumnIndex(COL_INSUMO_CATEGORIA);
        int stockMinimoIndex = cursor.getColumnIndex(COL_INSUMO_STOCK_MINIMO);
        int createdAtIndex = cursor.getColumnIndex(COL_INSUMO_CREATED_AT);
        int updatedAtIndex = cursor.getColumnIndex(COL_INSUMO_UPDATED_AT);
        int syncStatusIndex = cursor.getColumnIndex(COL_INSUMO_SYNC_STATUS);
        int serverIdIndex = cursor.getColumnIndex(COL_INSUMO_SERVER_ID);

        if (idIndex >= 0) insumo.setId(cursor.getInt(idIndex));
        if (empresaIdIndex >= 0 && !cursor.isNull(empresaIdIndex)) {
            insumo.setEmpresaId(cursor.getInt(empresaIdIndex));
        }
        if (nombreIndex >= 0) insumo.setNombre(cursor.getString(nombreIndex));
        if (descripcionIndex >= 0) insumo.setDescripcion(cursor.getString(descripcionIndex));
        if (cantidadIndex >= 0) insumo.setCantidad(cursor.getDouble(cantidadIndex));
        if (unidadMedidaIndex >= 0) insumo.setUnidadMedida(cursor.getString(unidadMedidaIndex));
        if (precioUnitarioIndex >= 0 && !cursor.isNull(precioUnitarioIndex)) {
            insumo.setPrecioUnitario(cursor.getDouble(precioUnitarioIndex));
        }
        if (precioTotalIndex >= 0 && !cursor.isNull(precioTotalIndex)) {
            insumo.setPrecioTotal(cursor.getDouble(precioTotalIndex));
        }
        if (categoriaIndex >= 0) insumo.setCategoria(cursor.getString(categoriaIndex));
        if (stockMinimoIndex >= 0 && !cursor.isNull(stockMinimoIndex)) {
            insumo.setStockMinimo(cursor.getDouble(stockMinimoIndex));
        }
        if (createdAtIndex >= 0) insumo.setCreatedAt(cursor.getString(createdAtIndex));
        if (updatedAtIndex >= 0) insumo.setUpdatedAt(cursor.getString(updatedAtIndex));
        if (syncStatusIndex >= 0) insumo.setSyncStatus(cursor.getString(syncStatusIndex));
        if (serverIdIndex >= 0 && !cursor.isNull(serverIdIndex)) {
            insumo.setServerId(cursor.getInt(serverIdIndex));
        }

        return insumo;
    }

    // ==================== MÉTODOS SYNC_QUEUE ====================

    public long agregarASyncQueue(String tableName, String operation, Integer recordId, String jsonData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SYNC_TABLE_NAME, tableName);
        values.put(COL_SYNC_OPERATION, operation);
        values.put(COL_SYNC_RECORD_ID, recordId);
        values.put(COL_SYNC_JSON_DATA, jsonData);

        long id = db.insert(TABLE_SYNC_QUEUE, null, values);
        db.close();
        return id;
    }

    public boolean eliminarSyncQueue(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_SYNC_QUEUE, COL_SYNC_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public List<SyncQueueItem> obtenerTodosSyncQueue() {
        List<SyncQueueItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SYNC_QUEUE, null, null, null, null, null, COL_SYNC_CREATED_AT + " ASC");

        if (cursor.moveToFirst()) {
            do {
                SyncQueueItem item = new SyncQueueItem();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SYNC_ID)));
                item.setTableName(cursor.getString(cursor.getColumnIndexOrThrow(COL_SYNC_TABLE_NAME)));
                item.setOperation(cursor.getString(cursor.getColumnIndexOrThrow(COL_SYNC_OPERATION)));
                int recordIdIndex = cursor.getColumnIndex(COL_SYNC_RECORD_ID);
                if (recordIdIndex >= 0 && !cursor.isNull(recordIdIndex)) {
                    item.setRecordId(cursor.getInt(recordIdIndex));
                }
                item.setJsonData(cursor.getString(cursor.getColumnIndexOrThrow(COL_SYNC_JSON_DATA)));
                item.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COL_SYNC_CREATED_AT)));
                items.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return items;
    }

    // ==================== MÉTODOS CRUD PRODUCTOS VENTA ====================

    public long insertarProductoVenta(ProductoVenta productoVenta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (productoVenta.getId() != null) {
            values.put(COL_PV_ID, productoVenta.getId());
        }
        if (productoVenta.getEmpresaId() != null) {
            values.put(COL_PV_EMPRESA_ID, productoVenta.getEmpresaId());
        }
        values.put(COL_PV_NOMBRE, productoVenta.getNombre());
        values.put(COL_PV_DESCRIPCION, productoVenta.getDescripcion());
        values.put(COL_PV_PRECIO_VENTA, productoVenta.getPrecioVenta());
        values.put(COL_PV_COSTO_TOTAL, productoVenta.getCostoTotal());
        values.put(COL_PV_MARGEN_GANANCIA, productoVenta.getMargenGanancia());
        values.put(COL_PV_ACTIVO, productoVenta.getActivo() != null && productoVenta.getActivo() ? 1 : 0);
        values.put(COL_PV_CREATED_AT, productoVenta.getCreatedAt());
        values.put(COL_PV_UPDATED_AT, productoVenta.getUpdatedAt());
        values.put(COL_PV_SYNC_STATUS, productoVenta.getSyncStatus() != null ? productoVenta.getSyncStatus() : "pending");
        if (productoVenta.getServerId() != null) {
            values.put(COL_PV_SERVER_ID, productoVenta.getServerId());
        }

        long id = db.insert(TABLE_PRODUCTOS_VENTA, null, values);
        db.close();
        return id;
    }

    public boolean actualizarProductoVenta(ProductoVenta productoVenta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (productoVenta.getEmpresaId() != null) {
            values.put(COL_PV_EMPRESA_ID, productoVenta.getEmpresaId());
        }
        values.put(COL_PV_NOMBRE, productoVenta.getNombre());
        values.put(COL_PV_DESCRIPCION, productoVenta.getDescripcion());
        values.put(COL_PV_PRECIO_VENTA, productoVenta.getPrecioVenta());
        values.put(COL_PV_COSTO_TOTAL, productoVenta.getCostoTotal());
        values.put(COL_PV_MARGEN_GANANCIA, productoVenta.getMargenGanancia());
        values.put(COL_PV_ACTIVO, productoVenta.getActivo() != null && productoVenta.getActivo() ? 1 : 0);
        values.put(COL_PV_UPDATED_AT, productoVenta.getUpdatedAt());
        values.put(COL_PV_SYNC_STATUS, productoVenta.getSyncStatus() != null ? productoVenta.getSyncStatus() : "pending");
        if (productoVenta.getServerId() != null) {
            values.put(COL_PV_SERVER_ID, productoVenta.getServerId());
        }

        int rowsAffected = db.update(TABLE_PRODUCTOS_VENTA, values, COL_PV_ID + " = ?",
                new String[]{String.valueOf(productoVenta.getId())});
        db.close();
        return rowsAffected > 0;
    }

    public boolean eliminarProductoVenta(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Eliminar primero los insumos asociados (CASCADE debería hacerlo, pero por seguridad)
        db.delete(TABLE_PRODUCTO_VENTA_INSUMO, COL_PVI_PRODUCTO_VENTA_ID + " = ?",
                new String[]{String.valueOf(id)});
        // Eliminar el producto
        int rowsAffected = db.delete(TABLE_PRODUCTOS_VENTA, COL_PV_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public ProductoVenta obtenerProductoVenta(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTOS_VENTA, null, COL_PV_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        ProductoVenta productoVenta = null;
        if (cursor.moveToFirst()) {
            productoVenta = cursorToProductoVenta(cursor);
            // Cargar insumos asociados
            productoVenta.setInsumos(obtenerInsumosDeProductoVenta(id));
        }

        cursor.close();
        db.close();
        return productoVenta;
    }

    public List<ProductoVenta> obtenerTodosProductosVenta() {
        List<ProductoVenta> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTOS_VENTA, null, null, null, null, null, COL_PV_NOMBRE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                ProductoVenta pv = cursorToProductoVenta(cursor);
                // Cargar insumos asociados
                pv.setInsumos(obtenerInsumosDeProductoVenta(pv.getId()));
                productos.add(pv);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productos;
    }

    public List<ProductoVenta> obtenerProductosVentaActivos() {
        List<ProductoVenta> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTOS_VENTA, null, COL_PV_ACTIVO + " = 1", null, null, null, COL_PV_NOMBRE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                ProductoVenta pv = cursorToProductoVenta(cursor);
                pv.setInsumos(obtenerInsumosDeProductoVenta(pv.getId()));
                productos.add(pv);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productos;
    }

    private ProductoVenta cursorToProductoVenta(Cursor cursor) {
        ProductoVenta pv = new ProductoVenta();
        
        int idIndex = cursor.getColumnIndex(COL_PV_ID);
        int empresaIdIndex = cursor.getColumnIndex(COL_PV_EMPRESA_ID);
        int nombreIndex = cursor.getColumnIndex(COL_PV_NOMBRE);
        int descripcionIndex = cursor.getColumnIndex(COL_PV_DESCRIPCION);
        int precioVentaIndex = cursor.getColumnIndex(COL_PV_PRECIO_VENTA);
        int costoTotalIndex = cursor.getColumnIndex(COL_PV_COSTO_TOTAL);
        int margenGananciaIndex = cursor.getColumnIndex(COL_PV_MARGEN_GANANCIA);
        int activoIndex = cursor.getColumnIndex(COL_PV_ACTIVO);
        int createdAtIndex = cursor.getColumnIndex(COL_PV_CREATED_AT);
        int updatedAtIndex = cursor.getColumnIndex(COL_PV_UPDATED_AT);
        int syncStatusIndex = cursor.getColumnIndex(COL_PV_SYNC_STATUS);
        int serverIdIndex = cursor.getColumnIndex(COL_PV_SERVER_ID);

        if (idIndex >= 0) pv.setId(cursor.getInt(idIndex));
        if (empresaIdIndex >= 0 && !cursor.isNull(empresaIdIndex)) {
            pv.setEmpresaId(cursor.getInt(empresaIdIndex));
        }
        if (nombreIndex >= 0) pv.setNombre(cursor.getString(nombreIndex));
        if (descripcionIndex >= 0) pv.setDescripcion(cursor.getString(descripcionIndex));
        if (precioVentaIndex >= 0 && !cursor.isNull(precioVentaIndex)) {
            pv.setPrecioVenta(cursor.getDouble(precioVentaIndex));
        }
        if (costoTotalIndex >= 0 && !cursor.isNull(costoTotalIndex)) {
            pv.setCostoTotal(cursor.getDouble(costoTotalIndex));
        }
        if (margenGananciaIndex >= 0 && !cursor.isNull(margenGananciaIndex)) {
            pv.setMargenGanancia(cursor.getDouble(margenGananciaIndex));
        }
        if (activoIndex >= 0) {
            pv.setActivo(cursor.getInt(activoIndex) == 1);
        }
        if (createdAtIndex >= 0) pv.setCreatedAt(cursor.getString(createdAtIndex));
        if (updatedAtIndex >= 0) pv.setUpdatedAt(cursor.getString(updatedAtIndex));
        if (syncStatusIndex >= 0) pv.setSyncStatus(cursor.getString(syncStatusIndex));
        if (serverIdIndex >= 0 && !cursor.isNull(serverIdIndex)) {
            pv.setServerId(cursor.getInt(serverIdIndex));
        }

        return pv;
    }

    // ==================== MÉTODOS CRUD PRODUCTO VENTA INSUMO ====================

    public long insertarProductoVentaInsumo(ProductoVentaInsumo pvi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (pvi.getId() != null) {
            values.put(COL_PVI_ID, pvi.getId());
        }
        values.put(COL_PVI_PRODUCTO_VENTA_ID, pvi.getProductoVentaId());
        values.put(COL_PVI_INSUMO_ID, pvi.getInsumoId());
        values.put(COL_PVI_CANTIDAD_USADA, pvi.getCantidadUsada());

        long id = db.insert(TABLE_PRODUCTO_VENTA_INSUMO, null, values);
        db.close();
        return id;
    }

    public boolean eliminarProductoVentaInsumo(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_PRODUCTO_VENTA_INSUMO, COL_PVI_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean eliminarInsumosDeProductoVenta(int productoVentaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_PRODUCTO_VENTA_INSUMO, COL_PVI_PRODUCTO_VENTA_ID + " = ?",
                new String[]{String.valueOf(productoVentaId)});
        db.close();
        return rowsAffected > 0;
    }

    public List<ProductoVentaInsumo> obtenerInsumosDeProductoVenta(int productoVentaId) {
        List<ProductoVentaInsumo> insumos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT pvi.*, i.* FROM " + TABLE_PRODUCTO_VENTA_INSUMO + " pvi " +
                      "INNER JOIN " + TABLE_INSUMOS + " i ON pvi." + COL_PVI_INSUMO_ID + " = i." + COL_INSUMO_ID + " " +
                      "WHERE pvi." + COL_PVI_PRODUCTO_VENTA_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(productoVentaId)});

        if (cursor.moveToFirst()) {
            do {
                ProductoVentaInsumo pvi = new ProductoVentaInsumo();
                pvi.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PVI_ID)));
                pvi.setProductoVentaId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PVI_PRODUCTO_VENTA_ID)));
                pvi.setInsumoId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_PVI_INSUMO_ID)));
                pvi.setCantidadUsada(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PVI_CANTIDAD_USADA)));
                
                // Cargar datos del insumo
                Insumo insumo = cursorToInsumo(cursor);
                pvi.setInsumo(insumo);
                
                insumos.add(pvi);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return insumos;
    }

    // Clase auxiliar para SyncQueue
    public static class SyncQueueItem {
        private int id;
        private String tableName;
        private String operation;
        private Integer recordId;
        private String jsonData;
        private String createdAt;

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getTableName() { return tableName; }
        public void setTableName(String tableName) { this.tableName = tableName; }
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        public Integer getRecordId() { return recordId; }
        public void setRecordId(Integer recordId) { this.recordId = recordId; }
        public String getJsonData() { return jsonData; }
        public void setJsonData(String jsonData) { this.jsonData = jsonData; }
        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }
}

