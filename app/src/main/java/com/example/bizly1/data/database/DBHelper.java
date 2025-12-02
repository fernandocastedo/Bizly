package com.example.bizly1.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bizly1.models.Cliente;
import com.example.bizly1.models.CostoGasto;
import com.example.bizly1.models.Insumo;
import com.example.bizly1.models.ProductoVenta;
import com.example.bizly1.models.ProductoVentaInsumo;
import com.example.bizly1.models.Sucursal;
import com.example.bizly1.models.Venta;
import com.example.bizly1.models.VentaProducto;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bizlyDB.db";
    private static final int DATABASE_VERSION = 5;

    // Tablas
    private static final String TABLE_INSUMOS = "insumos";
    private static final String TABLE_PRODUCTOS_VENTA = "productos_venta";
    private static final String TABLE_PRODUCTO_VENTA_INSUMO = "producto_venta_insumo";
    private static final String TABLE_CLIENTES = "clientes";
    private static final String TABLE_VENTAS = "ventas";
    private static final String TABLE_VENTA_PRODUCTO = "venta_producto";
    private static final String TABLE_SUCURSALES = "sucursales";
    private static final String TABLE_COSTOS_GASTOS = "costos_gastos";
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

    // Columnas Clientes
    private static final String COL_CLIENTE_ID = "id";
    private static final String COL_CLIENTE_EMPRESA_ID = "empresa_id";
    private static final String COL_CLIENTE_SUCURSAL_ID = "sucursal_id";
    private static final String COL_CLIENTE_NOMBRE = "nombre";
    private static final String COL_CLIENTE_NIT = "nit";
    private static final String COL_CLIENTE_TELEFONO = "telefono";
    private static final String COL_CLIENTE_EMAIL = "email";
    private static final String COL_CLIENTE_DIRECCION = "direccion";
    private static final String COL_CLIENTE_CREATED_AT = "created_at";
    private static final String COL_CLIENTE_SYNC_STATUS = "sync_status";
    private static final String COL_CLIENTE_SERVER_ID = "server_id";

    // Columnas Ventas
    private static final String COL_VENTA_ID = "id";
    private static final String COL_VENTA_EMPRESA_ID = "empresa_id";
    private static final String COL_VENTA_SUCURSAL_ID = "sucursal_id";
    private static final String COL_VENTA_USUARIO_ID = "usuario_id";
    private static final String COL_VENTA_CLIENTE_ID = "cliente_id";
    private static final String COL_VENTA_FECHA = "fecha";
    private static final String COL_VENTA_METODO_PAGO = "metodo_pago";
    private static final String COL_VENTA_TOTAL = "total";
    private static final String COL_VENTA_ES_ENVIO = "es_envio";
    private static final String COL_VENTA_ESTADO_PAGO = "estado_pago";
    private static final String COL_VENTA_ESTADO_PEDIDO = "estado_pedido";
    private static final String COL_VENTA_CREATED_AT = "created_at";
    private static final String COL_VENTA_SYNC_STATUS = "sync_status";
    private static final String COL_VENTA_SERVER_ID = "server_id";

    // Columnas VentaProducto
    private static final String COL_VP_ID = "id";
    private static final String COL_VP_VENTA_ID = "venta_id";
    private static final String COL_VP_PRODUCTO_VENTA_ID = "producto_venta_id";
    private static final String COL_VP_CANTIDAD = "cantidad";
    private static final String COL_VP_PRECIO_UNITARIO = "precio_unitario";
    private static final String COL_VP_SUBTOTAL = "subtotal";

    // Columnas Sucursales
    private static final String COL_SUCURSAL_ID = "id";
    private static final String COL_SUCURSAL_EMPRESA_ID = "empresa_id";
    private static final String COL_SUCURSAL_NOMBRE = "nombre";
    private static final String COL_SUCURSAL_DIRECCION = "direccion";
    private static final String COL_SUCURSAL_CIUDAD = "ciudad";
    private static final String COL_SUCURSAL_LATITUD = "latitud";
    private static final String COL_SUCURSAL_LONGITUD = "longitud";
    private static final String COL_SUCURSAL_DEPARTAMENTO = "departamento";
    private static final String COL_SUCURSAL_TELEFONO = "telefono";
    private static final String COL_SUCURSAL_CREATED_AT = "created_at";
    private static final String COL_SUCURSAL_UPDATED_AT = "updated_at";
    private static final String COL_SUCURSAL_SYNC_STATUS = "sync_status";
    private static final String COL_SUCURSAL_SERVER_ID = "server_id";

    // Columnas CostosGastos
    private static final String COL_CG_ID = "id";
    private static final String COL_CG_EMPRESA_ID = "empresa_id";
    private static final String COL_CG_SUCURSAL_ID = "sucursal_id";
    private static final String COL_CG_TIPO = "tipo";
    private static final String COL_CG_CATEGORIA = "categoria";
    private static final String COL_CG_DESCRIPCION = "descripcion";
    private static final String COL_CG_MONTO = "monto";
    private static final String COL_CG_FECHA = "fecha";
    private static final String COL_CG_TRABAJADOR_ID = "trabajador_id";
    private static final String COL_CG_METODO_PAGO = "metodo_pago";
    private static final String COL_CG_COMPROBANTE = "comprobante";
    private static final String COL_CG_CREATED_AT = "created_at";
    private static final String COL_CG_UPDATED_AT = "updated_at";
    private static final String COL_CG_SYNC_STATUS = "sync_status";
    private static final String COL_CG_SERVER_ID = "server_id";

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

        // Tabla Clientes
        String createClientesTable = "CREATE TABLE " + TABLE_CLIENTES + " (" +
                COL_CLIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CLIENTE_EMPRESA_ID + " INTEGER, " +
                COL_CLIENTE_SUCURSAL_ID + " INTEGER, " +
                COL_CLIENTE_NOMBRE + " TEXT NOT NULL, " +
                COL_CLIENTE_NIT + " INTEGER, " +
                COL_CLIENTE_TELEFONO + " TEXT, " +
                COL_CLIENTE_EMAIL + " TEXT, " +
                COL_CLIENTE_DIRECCION + " TEXT, " +
                COL_CLIENTE_CREATED_AT + " TEXT, " +
                COL_CLIENTE_SYNC_STATUS + " TEXT DEFAULT 'pending', " +
                COL_CLIENTE_SERVER_ID + " INTEGER" +
                ")";

        db.execSQL(createClientesTable);

        // Tabla Ventas
        String createVentasTable = "CREATE TABLE " + TABLE_VENTAS + " (" +
                COL_VENTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_VENTA_EMPRESA_ID + " INTEGER, " +
                COL_VENTA_SUCURSAL_ID + " INTEGER, " +
                COL_VENTA_USUARIO_ID + " INTEGER, " +
                COL_VENTA_CLIENTE_ID + " INTEGER, " +
                COL_VENTA_FECHA + " TEXT, " +
                COL_VENTA_METODO_PAGO + " TEXT, " +
                COL_VENTA_TOTAL + " REAL, " +
                COL_VENTA_ES_ENVIO + " INTEGER DEFAULT 0, " +
                COL_VENTA_ESTADO_PAGO + " TEXT DEFAULT 'pagado', " +
                COL_VENTA_ESTADO_PEDIDO + " TEXT DEFAULT 'completado', " +
                COL_VENTA_CREATED_AT + " TEXT, " +
                COL_VENTA_SYNC_STATUS + " TEXT DEFAULT 'pending', " +
                COL_VENTA_SERVER_ID + " INTEGER" +
                ")";

        db.execSQL(createVentasTable);

        // Tabla VentaProducto
        String createVentaProductoTable = "CREATE TABLE " + TABLE_VENTA_PRODUCTO + " (" +
                COL_VP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_VP_VENTA_ID + " INTEGER NOT NULL, " +
                COL_VP_PRODUCTO_VENTA_ID + " INTEGER NOT NULL, " +
                COL_VP_CANTIDAD + " REAL NOT NULL, " +
                COL_VP_PRECIO_UNITARIO + " REAL NOT NULL, " +
                COL_VP_SUBTOTAL + " REAL NOT NULL, " +
                "FOREIGN KEY(" + COL_VP_VENTA_ID + ") REFERENCES " + TABLE_VENTAS + "(" + COL_VENTA_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COL_VP_PRODUCTO_VENTA_ID + ") REFERENCES " + TABLE_PRODUCTOS_VENTA + "(" + COL_PV_ID + ") ON DELETE CASCADE" +
                ")";

        db.execSQL(createVentaProductoTable);

        // Tabla Sucursales
        String createSucursalesTable = "CREATE TABLE " + TABLE_SUCURSALES + " (" +
                COL_SUCURSAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SUCURSAL_EMPRESA_ID + " INTEGER, " +
                COL_SUCURSAL_NOMBRE + " TEXT NOT NULL, " +
                COL_SUCURSAL_DIRECCION + " TEXT, " +
                COL_SUCURSAL_CIUDAD + " TEXT, " +
                COL_SUCURSAL_LATITUD + " REAL, " +
                COL_SUCURSAL_LONGITUD + " REAL, " +
                COL_SUCURSAL_DEPARTAMENTO + " TEXT, " +
                COL_SUCURSAL_TELEFONO + " TEXT, " +
                COL_SUCURSAL_CREATED_AT + " TEXT, " +
                COL_SUCURSAL_UPDATED_AT + " TEXT, " +
                COL_SUCURSAL_SYNC_STATUS + " TEXT DEFAULT 'pending', " +
                COL_SUCURSAL_SERVER_ID + " INTEGER" +
                ")";

        db.execSQL(createSucursalesTable);

        // Tabla CostosGastos
        String createCostosGastosTable = "CREATE TABLE " + TABLE_COSTOS_GASTOS + " (" +
                COL_CG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CG_EMPRESA_ID + " INTEGER, " +
                COL_CG_SUCURSAL_ID + " INTEGER, " +
                COL_CG_TIPO + " TEXT NOT NULL, " +
                COL_CG_CATEGORIA + " TEXT, " +
                COL_CG_DESCRIPCION + " TEXT, " +
                COL_CG_MONTO + " REAL NOT NULL, " +
                COL_CG_FECHA + " TEXT, " +
                COL_CG_TRABAJADOR_ID + " INTEGER, " +
                COL_CG_METODO_PAGO + " TEXT, " +
                COL_CG_COMPROBANTE + " TEXT, " +
                COL_CG_CREATED_AT + " TEXT, " +
                COL_CG_UPDATED_AT + " TEXT, " +
                COL_CG_SYNC_STATUS + " TEXT DEFAULT 'pending', " +
                COL_CG_SERVER_ID + " INTEGER" +
                ")";

        db.execSQL(createCostosGastosTable);

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
        if (oldVersion < 3) {
            // Crear nuevas tablas para ventas
            String createClientesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENTES + " (" +
                    COL_CLIENTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_CLIENTE_EMPRESA_ID + " INTEGER, " +
                    COL_CLIENTE_SUCURSAL_ID + " INTEGER, " +
                    COL_CLIENTE_NOMBRE + " TEXT NOT NULL, " +
                    COL_CLIENTE_NIT + " INTEGER, " +
                    COL_CLIENTE_TELEFONO + " TEXT, " +
                    COL_CLIENTE_EMAIL + " TEXT, " +
                    COL_CLIENTE_DIRECCION + " TEXT, " +
                    COL_CLIENTE_CREATED_AT + " TEXT, " +
                    COL_CLIENTE_SYNC_STATUS + " TEXT DEFAULT 'pending', " +
                    COL_CLIENTE_SERVER_ID + " INTEGER" +
                    ")";

            db.execSQL(createClientesTable);

            String createVentasTable = "CREATE TABLE IF NOT EXISTS " + TABLE_VENTAS + " (" +
                    COL_VENTA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_VENTA_EMPRESA_ID + " INTEGER, " +
                    COL_VENTA_SUCURSAL_ID + " INTEGER, " +
                    COL_VENTA_USUARIO_ID + " INTEGER, " +
                    COL_VENTA_CLIENTE_ID + " INTEGER, " +
                    COL_VENTA_FECHA + " TEXT, " +
                    COL_VENTA_METODO_PAGO + " TEXT, " +
                    COL_VENTA_TOTAL + " REAL, " +
                    COL_VENTA_ES_ENVIO + " INTEGER DEFAULT 0, " +
                    COL_VENTA_ESTADO_PAGO + " TEXT DEFAULT 'pagado', " +
                    COL_VENTA_ESTADO_PEDIDO + " TEXT DEFAULT 'completado', " +
                    COL_VENTA_CREATED_AT + " TEXT, " +
                    COL_VENTA_SYNC_STATUS + " TEXT DEFAULT 'pending', " +
                    COL_VENTA_SERVER_ID + " INTEGER" +
                    ")";

            db.execSQL(createVentasTable);

            String createVentaProductoTable = "CREATE TABLE IF NOT EXISTS " + TABLE_VENTA_PRODUCTO + " (" +
                    COL_VP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_VP_VENTA_ID + " INTEGER NOT NULL, " +
                    COL_VP_PRODUCTO_VENTA_ID + " INTEGER NOT NULL, " +
                    COL_VP_CANTIDAD + " REAL NOT NULL, " +
                    COL_VP_PRECIO_UNITARIO + " REAL NOT NULL, " +
                    COL_VP_SUBTOTAL + " REAL NOT NULL, " +
                    "FOREIGN KEY(" + COL_VP_VENTA_ID + ") REFERENCES " + TABLE_VENTAS + "(" + COL_VENTA_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY(" + COL_VP_PRODUCTO_VENTA_ID + ") REFERENCES " + TABLE_PRODUCTOS_VENTA + "(" + COL_PV_ID + ") ON DELETE CASCADE" +
                    ")";

            db.execSQL(createVentaProductoTable);
        }
        if (oldVersion < 4) {
            // Crear nueva tabla para sucursales
            String createSucursalesTable = "CREATE TABLE IF NOT EXISTS " + TABLE_SUCURSALES + " (" +
                    COL_SUCURSAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_SUCURSAL_EMPRESA_ID + " INTEGER, " +
                    COL_SUCURSAL_NOMBRE + " TEXT NOT NULL, " +
                    COL_SUCURSAL_DIRECCION + " TEXT, " +
                    COL_SUCURSAL_CIUDAD + " TEXT, " +
                    COL_SUCURSAL_LATITUD + " REAL, " +
                    COL_SUCURSAL_LONGITUD + " REAL, " +
                    COL_SUCURSAL_DEPARTAMENTO + " TEXT, " +
                    COL_SUCURSAL_TELEFONO + " TEXT, " +
                    COL_SUCURSAL_CREATED_AT + " TEXT, " +
                    COL_SUCURSAL_UPDATED_AT + " TEXT, " +
                    COL_SUCURSAL_SYNC_STATUS + " TEXT DEFAULT 'pending', " +
                    COL_SUCURSAL_SERVER_ID + " INTEGER" +
                    ")";

            db.execSQL(createSucursalesTable);
        }
        if (oldVersion < 5) {
            // Crear nueva tabla para costos y gastos
            String createCostosGastosTable = "CREATE TABLE IF NOT EXISTS " + TABLE_COSTOS_GASTOS + " (" +
                    COL_CG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_CG_EMPRESA_ID + " INTEGER, " +
                    COL_CG_SUCURSAL_ID + " INTEGER, " +
                    COL_CG_TIPO + " TEXT NOT NULL, " +
                    COL_CG_CATEGORIA + " TEXT, " +
                    COL_CG_DESCRIPCION + " TEXT, " +
                    COL_CG_MONTO + " REAL NOT NULL, " +
                    COL_CG_FECHA + " TEXT, " +
                    COL_CG_TRABAJADOR_ID + " INTEGER, " +
                    COL_CG_METODO_PAGO + " TEXT, " +
                    COL_CG_COMPROBANTE + " TEXT, " +
                    COL_CG_CREATED_AT + " TEXT, " +
                    COL_CG_UPDATED_AT + " TEXT, " +
                    COL_CG_SYNC_STATUS + " TEXT DEFAULT 'pending', " +
                    COL_CG_SERVER_ID + " INTEGER" +
                    ")";

            db.execSQL(createCostosGastosTable);
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

    // ==================== MÉTODOS CRUD CLIENTES ====================

    public long insertarCliente(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (cliente.getId() != null) {
            values.put(COL_CLIENTE_ID, cliente.getId());
        }
        if (cliente.getEmpresaId() != null) {
            values.put(COL_CLIENTE_EMPRESA_ID, cliente.getEmpresaId());
        }
        if (cliente.getSucursalId() != null) {
            values.put(COL_CLIENTE_SUCURSAL_ID, cliente.getSucursalId());
        }
        values.put(COL_CLIENTE_NOMBRE, cliente.getNombre());
        if (cliente.getNit() != null) {
            values.put(COL_CLIENTE_NIT, cliente.getNit());
        }
        values.put(COL_CLIENTE_TELEFONO, cliente.getTelefono());
        values.put(COL_CLIENTE_EMAIL, cliente.getEmail());
        values.put(COL_CLIENTE_DIRECCION, cliente.getDireccion());
        values.put(COL_CLIENTE_CREATED_AT, cliente.getCreatedAt());
        values.put(COL_CLIENTE_SYNC_STATUS, cliente.getSyncStatus() != null ? cliente.getSyncStatus() : "pending");
        if (cliente.getServerId() != null) {
            values.put(COL_CLIENTE_SERVER_ID, cliente.getServerId());
        }

        long id = db.insert(TABLE_CLIENTES, null, values);
        db.close();
        return id;
    }

    public boolean actualizarCliente(Cliente cliente) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (cliente.getEmpresaId() != null) {
            values.put(COL_CLIENTE_EMPRESA_ID, cliente.getEmpresaId());
        }
        if (cliente.getSucursalId() != null) {
            values.put(COL_CLIENTE_SUCURSAL_ID, cliente.getSucursalId());
        }
        values.put(COL_CLIENTE_NOMBRE, cliente.getNombre());
        if (cliente.getNit() != null) {
            values.put(COL_CLIENTE_NIT, cliente.getNit());
        }
        values.put(COL_CLIENTE_TELEFONO, cliente.getTelefono());
        values.put(COL_CLIENTE_EMAIL, cliente.getEmail());
        values.put(COL_CLIENTE_DIRECCION, cliente.getDireccion());
        values.put(COL_CLIENTE_SYNC_STATUS, cliente.getSyncStatus() != null ? cliente.getSyncStatus() : "pending");
        if (cliente.getServerId() != null) {
            values.put(COL_CLIENTE_SERVER_ID, cliente.getServerId());
        }

        int rowsAffected = db.update(TABLE_CLIENTES, values, COL_CLIENTE_ID + " = ?",
                new String[]{String.valueOf(cliente.getId())});
        db.close();
        return rowsAffected > 0;
    }

    public boolean eliminarCliente(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CLIENTES, COL_CLIENTE_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public Cliente obtenerCliente(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLIENTES, null, COL_CLIENTE_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Cliente cliente = null;
        if (cursor.moveToFirst()) {
            cliente = cursorToCliente(cursor);
        }

        cursor.close();
        db.close();
        return cliente;
    }

    public Cliente obtenerClientePorNit(Integer nit) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLIENTES, null, COL_CLIENTE_NIT + " = ?",
                new String[]{String.valueOf(nit)}, null, null, null);

        Cliente cliente = null;
        if (cursor.moveToFirst()) {
            cliente = cursorToCliente(cursor);
        }

        cursor.close();
        db.close();
        return cliente;
    }

    public List<Cliente> obtenerTodosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CLIENTES, null, null, null, null, null, COL_CLIENTE_NOMBRE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                clientes.add(cursorToCliente(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return clientes;
    }

    private Cliente cursorToCliente(Cursor cursor) {
        Cliente cliente = new Cliente();

        int idIndex = cursor.getColumnIndex(COL_CLIENTE_ID);
        int empresaIdIndex = cursor.getColumnIndex(COL_CLIENTE_EMPRESA_ID);
        int sucursalIdIndex = cursor.getColumnIndex(COL_CLIENTE_SUCURSAL_ID);
        int nombreIndex = cursor.getColumnIndex(COL_CLIENTE_NOMBRE);
        int nitIndex = cursor.getColumnIndex(COL_CLIENTE_NIT);
        int telefonoIndex = cursor.getColumnIndex(COL_CLIENTE_TELEFONO);
        int emailIndex = cursor.getColumnIndex(COL_CLIENTE_EMAIL);
        int direccionIndex = cursor.getColumnIndex(COL_CLIENTE_DIRECCION);
        int createdAtIndex = cursor.getColumnIndex(COL_CLIENTE_CREATED_AT);
        int syncStatusIndex = cursor.getColumnIndex(COL_CLIENTE_SYNC_STATUS);
        int serverIdIndex = cursor.getColumnIndex(COL_CLIENTE_SERVER_ID);

        if (idIndex >= 0) cliente.setId(cursor.getInt(idIndex));
        if (empresaIdIndex >= 0 && !cursor.isNull(empresaIdIndex)) {
            cliente.setEmpresaId(cursor.getInt(empresaIdIndex));
        }
        if (sucursalIdIndex >= 0 && !cursor.isNull(sucursalIdIndex)) {
            cliente.setSucursalId(cursor.getInt(sucursalIdIndex));
        }
        if (nombreIndex >= 0) cliente.setNombre(cursor.getString(nombreIndex));
        if (nitIndex >= 0 && !cursor.isNull(nitIndex)) {
            cliente.setNit(cursor.getInt(nitIndex));
        }
        if (telefonoIndex >= 0) cliente.setTelefono(cursor.getString(telefonoIndex));
        if (emailIndex >= 0) cliente.setEmail(cursor.getString(emailIndex));
        if (direccionIndex >= 0) cliente.setDireccion(cursor.getString(direccionIndex));
        if (createdAtIndex >= 0) cliente.setCreatedAt(cursor.getString(createdAtIndex));
        if (syncStatusIndex >= 0) cliente.setSyncStatus(cursor.getString(syncStatusIndex));
        if (serverIdIndex >= 0 && !cursor.isNull(serverIdIndex)) {
            cliente.setServerId(cursor.getInt(serverIdIndex));
        }

        return cliente;
    }

    // ==================== MÉTODOS CRUD VENTAS ====================

    public long insertarVenta(Venta venta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (venta.getId() != null) {
            values.put(COL_VENTA_ID, venta.getId());
        }
        if (venta.getEmpresaId() != null) {
            values.put(COL_VENTA_EMPRESA_ID, venta.getEmpresaId());
        }
        if (venta.getSucursalId() != null) {
            values.put(COL_VENTA_SUCURSAL_ID, venta.getSucursalId());
        }
        if (venta.getUsuarioId() != null) {
            values.put(COL_VENTA_USUARIO_ID, venta.getUsuarioId());
        }
        if (venta.getClienteId() != null) {
            values.put(COL_VENTA_CLIENTE_ID, venta.getClienteId());
        }
        values.put(COL_VENTA_FECHA, venta.getFecha());
        values.put(COL_VENTA_METODO_PAGO, venta.getMetodoPago());
        if (venta.getTotal() != null) {
            values.put(COL_VENTA_TOTAL, venta.getTotal());
        }
        values.put(COL_VENTA_ES_ENVIO, venta.getEsEnvio() != null && venta.getEsEnvio() ? 1 : 0);
        values.put(COL_VENTA_ESTADO_PAGO, venta.getEstadoPago());
        values.put(COL_VENTA_ESTADO_PEDIDO, venta.getEstadoPedido());
        values.put(COL_VENTA_CREATED_AT, venta.getCreatedAt());
        values.put(COL_VENTA_SYNC_STATUS, venta.getSyncStatus() != null ? venta.getSyncStatus() : "pending");
        if (venta.getServerId() != null) {
            values.put(COL_VENTA_SERVER_ID, venta.getServerId());
        }

        long id = db.insert(TABLE_VENTAS, null, values);
        db.close();
        return id;
    }

    public boolean actualizarVenta(Venta venta) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (venta.getEmpresaId() != null) {
            values.put(COL_VENTA_EMPRESA_ID, venta.getEmpresaId());
        }
        if (venta.getSucursalId() != null) {
            values.put(COL_VENTA_SUCURSAL_ID, venta.getSucursalId());
        }
        if (venta.getUsuarioId() != null) {
            values.put(COL_VENTA_USUARIO_ID, venta.getUsuarioId());
        }
        if (venta.getClienteId() != null) {
            values.put(COL_VENTA_CLIENTE_ID, venta.getClienteId());
        }
        values.put(COL_VENTA_FECHA, venta.getFecha());
        values.put(COL_VENTA_METODO_PAGO, venta.getMetodoPago());
        if (venta.getTotal() != null) {
            values.put(COL_VENTA_TOTAL, venta.getTotal());
        }
        values.put(COL_VENTA_ES_ENVIO, venta.getEsEnvio() != null && venta.getEsEnvio() ? 1 : 0);
        values.put(COL_VENTA_ESTADO_PAGO, venta.getEstadoPago());
        values.put(COL_VENTA_ESTADO_PEDIDO, venta.getEstadoPedido());
        values.put(COL_VENTA_SYNC_STATUS, venta.getSyncStatus() != null ? venta.getSyncStatus() : "pending");
        if (venta.getServerId() != null) {
            values.put(COL_VENTA_SERVER_ID, venta.getServerId());
        }

        int rowsAffected = db.update(TABLE_VENTAS, values, COL_VENTA_ID + " = ?",
                new String[]{String.valueOf(venta.getId())});
        db.close();
        return rowsAffected > 0;
    }

    public boolean eliminarVenta(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_VENTAS, COL_VENTA_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public Venta obtenerVenta(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_VENTAS, null, COL_VENTA_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Venta venta = null;
        if (cursor.moveToFirst()) {
            venta = cursorToVenta(cursor);
            venta.setProductos(obtenerProductosDeVenta(id));
            if (venta.getClienteId() != null) {
                venta.setCliente(obtenerCliente(venta.getClienteId()));
            }
        }

        cursor.close();
        db.close();
        return venta;
    }

    public List<Venta> obtenerTodasVentas() {
        List<Venta> ventas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_VENTAS, null, null, null, null, null, COL_VENTA_FECHA + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Venta venta = cursorToVenta(cursor);
                venta.setProductos(obtenerProductosDeVenta(venta.getId()));
                if (venta.getClienteId() != null) {
                    venta.setCliente(obtenerCliente(venta.getClienteId()));
                }
                ventas.add(venta);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return ventas;
    }

    public List<Venta> obtenerVentasPendientes() {
        List<Venta> ventas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause = COL_VENTA_ESTADO_PEDIDO + " = ? OR " + COL_VENTA_ESTADO_PAGO + " = ?";
        String[] whereArgs = new String[]{"pendiente", "pendiente"};
        Cursor cursor = db.query(TABLE_VENTAS, null, whereClause, whereArgs, null, null, COL_VENTA_FECHA + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Venta venta = cursorToVenta(cursor);
                venta.setProductos(obtenerProductosDeVenta(venta.getId()));
                if (venta.getClienteId() != null) {
                    venta.setCliente(obtenerCliente(venta.getClienteId()));
                }
                ventas.add(venta);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return ventas;
    }

    private Venta cursorToVenta(Cursor cursor) {
        Venta venta = new Venta();

        int idIndex = cursor.getColumnIndex(COL_VENTA_ID);
        int empresaIdIndex = cursor.getColumnIndex(COL_VENTA_EMPRESA_ID);
        int sucursalIdIndex = cursor.getColumnIndex(COL_VENTA_SUCURSAL_ID);
        int usuarioIdIndex = cursor.getColumnIndex(COL_VENTA_USUARIO_ID);
        int clienteIdIndex = cursor.getColumnIndex(COL_VENTA_CLIENTE_ID);
        int fechaIndex = cursor.getColumnIndex(COL_VENTA_FECHA);
        int metodoPagoIndex = cursor.getColumnIndex(COL_VENTA_METODO_PAGO);
        int totalIndex = cursor.getColumnIndex(COL_VENTA_TOTAL);
        int esEnvioIndex = cursor.getColumnIndex(COL_VENTA_ES_ENVIO);
        int estadoPagoIndex = cursor.getColumnIndex(COL_VENTA_ESTADO_PAGO);
        int estadoPedidoIndex = cursor.getColumnIndex(COL_VENTA_ESTADO_PEDIDO);
        int createdAtIndex = cursor.getColumnIndex(COL_VENTA_CREATED_AT);
        int syncStatusIndex = cursor.getColumnIndex(COL_VENTA_SYNC_STATUS);
        int serverIdIndex = cursor.getColumnIndex(COL_VENTA_SERVER_ID);

        if (idIndex >= 0) venta.setId(cursor.getInt(idIndex));
        if (empresaIdIndex >= 0 && !cursor.isNull(empresaIdIndex)) {
            venta.setEmpresaId(cursor.getInt(empresaIdIndex));
        }
        if (sucursalIdIndex >= 0 && !cursor.isNull(sucursalIdIndex)) {
            venta.setSucursalId(cursor.getInt(sucursalIdIndex));
        }
        if (usuarioIdIndex >= 0 && !cursor.isNull(usuarioIdIndex)) {
            venta.setUsuarioId(cursor.getInt(usuarioIdIndex));
        }
        if (clienteIdIndex >= 0 && !cursor.isNull(clienteIdIndex)) {
            venta.setClienteId(cursor.getInt(clienteIdIndex));
        }
        if (fechaIndex >= 0) venta.setFecha(cursor.getString(fechaIndex));
        if (metodoPagoIndex >= 0) venta.setMetodoPago(cursor.getString(metodoPagoIndex));
        if (totalIndex >= 0 && !cursor.isNull(totalIndex)) {
            venta.setTotal(cursor.getDouble(totalIndex));
        }
        if (esEnvioIndex >= 0) {
            venta.setEsEnvio(cursor.getInt(esEnvioIndex) == 1);
        }
        if (estadoPagoIndex >= 0) venta.setEstadoPago(cursor.getString(estadoPagoIndex));
        if (estadoPedidoIndex >= 0) venta.setEstadoPedido(cursor.getString(estadoPedidoIndex));
        if (createdAtIndex >= 0) venta.setCreatedAt(cursor.getString(createdAtIndex));
        if (syncStatusIndex >= 0) venta.setSyncStatus(cursor.getString(syncStatusIndex));
        if (serverIdIndex >= 0 && !cursor.isNull(serverIdIndex)) {
            venta.setServerId(cursor.getInt(serverIdIndex));
        }

        return venta;
    }

    // ==================== MÉTODOS CRUD VENTA PRODUCTO ====================

    public long insertarVentaProducto(VentaProducto vp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (vp.getId() != null) {
            values.put(COL_VP_ID, vp.getId());
        }
        values.put(COL_VP_VENTA_ID, vp.getVentaId());
        values.put(COL_VP_PRODUCTO_VENTA_ID, vp.getProductoVentaId());
        values.put(COL_VP_CANTIDAD, vp.getCantidad());
        values.put(COL_VP_PRECIO_UNITARIO, vp.getPrecioUnitario());
        values.put(COL_VP_SUBTOTAL, vp.getSubtotal());

        long id = db.insert(TABLE_VENTA_PRODUCTO, null, values);
        db.close();
        return id;
    }

    public boolean eliminarVentaProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_VENTA_PRODUCTO, COL_VP_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean eliminarProductosDeVenta(int ventaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_VENTA_PRODUCTO, COL_VP_VENTA_ID + " = ?",
                new String[]{String.valueOf(ventaId)});
        db.close();
        return rowsAffected > 0;
    }

    public List<VentaProducto> obtenerProductosDeVenta(int ventaId) {
        List<VentaProducto> productos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String query = "SELECT vp.*, pv.* FROM " + TABLE_VENTA_PRODUCTO + " vp " +
                      "INNER JOIN " + TABLE_PRODUCTOS_VENTA + " pv ON vp." + COL_VP_PRODUCTO_VENTA_ID + " = pv." + COL_PV_ID + " " +
                      "WHERE vp." + COL_VP_VENTA_ID + " = ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(ventaId)});

        if (cursor.moveToFirst()) {
            do {
                VentaProducto vp = new VentaProducto();
                vp.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_VP_ID)));
                vp.setVentaId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_VP_VENTA_ID)));
                vp.setProductoVentaId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_VP_PRODUCTO_VENTA_ID)));
                vp.setCantidad(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_VP_CANTIDAD)));
                vp.setPrecioUnitario(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_VP_PRECIO_UNITARIO)));
                vp.setSubtotal(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_VP_SUBTOTAL)));
                
                // Cargar datos del producto
                ProductoVenta productoVenta = cursorToProductoVenta(cursor);
                vp.setProductoVenta(productoVenta);
                
                productos.add(vp);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productos;
    }

    // ==================== MÉTODOS CRUD SUCURSALES ====================

    public long insertarSucursal(Sucursal sucursal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (sucursal.getId() != null) {
            values.put(COL_SUCURSAL_ID, sucursal.getId());
        }
        if (sucursal.getEmpresaId() != null) {
            values.put(COL_SUCURSAL_EMPRESA_ID, sucursal.getEmpresaId());
        }
        values.put(COL_SUCURSAL_NOMBRE, sucursal.getNombre());
        values.put(COL_SUCURSAL_DIRECCION, sucursal.getDireccion());
        values.put(COL_SUCURSAL_CIUDAD, sucursal.getCiudad());
        if (sucursal.getLatitud() != null) {
            values.put(COL_SUCURSAL_LATITUD, sucursal.getLatitud());
        }
        if (sucursal.getLongitud() != null) {
            values.put(COL_SUCURSAL_LONGITUD, sucursal.getLongitud());
        }
        values.put(COL_SUCURSAL_DEPARTAMENTO, sucursal.getDepartamento());
        values.put(COL_SUCURSAL_TELEFONO, sucursal.getTelefono());
        values.put(COL_SUCURSAL_CREATED_AT, sucursal.getCreatedAt());
        values.put(COL_SUCURSAL_UPDATED_AT, sucursal.getUpdatedAt());
        values.put(COL_SUCURSAL_SYNC_STATUS, sucursal.getSyncStatus() != null ? sucursal.getSyncStatus() : "pending");
        if (sucursal.getServerId() != null) {
            values.put(COL_SUCURSAL_SERVER_ID, sucursal.getServerId());
        }

        long id = db.insert(TABLE_SUCURSALES, null, values);
        db.close();
        return id;
    }

    public boolean actualizarSucursal(Sucursal sucursal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (sucursal.getEmpresaId() != null) {
            values.put(COL_SUCURSAL_EMPRESA_ID, sucursal.getEmpresaId());
        }
        values.put(COL_SUCURSAL_NOMBRE, sucursal.getNombre());
        values.put(COL_SUCURSAL_DIRECCION, sucursal.getDireccion());
        values.put(COL_SUCURSAL_CIUDAD, sucursal.getCiudad());
        if (sucursal.getLatitud() != null) {
            values.put(COL_SUCURSAL_LATITUD, sucursal.getLatitud());
        }
        if (sucursal.getLongitud() != null) {
            values.put(COL_SUCURSAL_LONGITUD, sucursal.getLongitud());
        }
        values.put(COL_SUCURSAL_DEPARTAMENTO, sucursal.getDepartamento());
        values.put(COL_SUCURSAL_TELEFONO, sucursal.getTelefono());
        values.put(COL_SUCURSAL_UPDATED_AT, sucursal.getUpdatedAt());
        values.put(COL_SUCURSAL_SYNC_STATUS, sucursal.getSyncStatus() != null ? sucursal.getSyncStatus() : "pending");
        if (sucursal.getServerId() != null) {
            values.put(COL_SUCURSAL_SERVER_ID, sucursal.getServerId());
        }

        int rowsAffected = db.update(TABLE_SUCURSALES, values, COL_SUCURSAL_ID + " = ?",
                new String[]{String.valueOf(sucursal.getId())});
        db.close();
        return rowsAffected > 0;
    }

    public boolean eliminarSucursal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_SUCURSALES, COL_SUCURSAL_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public Sucursal obtenerSucursal(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SUCURSALES, null, COL_SUCURSAL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Sucursal sucursal = null;
        if (cursor.moveToFirst()) {
            sucursal = cursorToSucursal(cursor);
        }

        cursor.close();
        db.close();
        return sucursal;
    }

    public List<Sucursal> obtenerTodasSucursales() {
        List<Sucursal> sucursales = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SUCURSALES, null, null, null, null, null, COL_SUCURSAL_NOMBRE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                sucursales.add(cursorToSucursal(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return sucursales;
    }

    private Sucursal cursorToSucursal(Cursor cursor) {
        Sucursal sucursal = new Sucursal();

        int idIndex = cursor.getColumnIndex(COL_SUCURSAL_ID);
        int empresaIdIndex = cursor.getColumnIndex(COL_SUCURSAL_EMPRESA_ID);
        int nombreIndex = cursor.getColumnIndex(COL_SUCURSAL_NOMBRE);
        int direccionIndex = cursor.getColumnIndex(COL_SUCURSAL_DIRECCION);
        int ciudadIndex = cursor.getColumnIndex(COL_SUCURSAL_CIUDAD);
        int latitudIndex = cursor.getColumnIndex(COL_SUCURSAL_LATITUD);
        int longitudIndex = cursor.getColumnIndex(COL_SUCURSAL_LONGITUD);
        int departamentoIndex = cursor.getColumnIndex(COL_SUCURSAL_DEPARTAMENTO);
        int telefonoIndex = cursor.getColumnIndex(COL_SUCURSAL_TELEFONO);
        int createdAtIndex = cursor.getColumnIndex(COL_SUCURSAL_CREATED_AT);
        int updatedAtIndex = cursor.getColumnIndex(COL_SUCURSAL_UPDATED_AT);
        int syncStatusIndex = cursor.getColumnIndex(COL_SUCURSAL_SYNC_STATUS);
        int serverIdIndex = cursor.getColumnIndex(COL_SUCURSAL_SERVER_ID);

        if (idIndex >= 0) sucursal.setId(cursor.getInt(idIndex));
        if (empresaIdIndex >= 0 && !cursor.isNull(empresaIdIndex)) {
            sucursal.setEmpresaId(cursor.getInt(empresaIdIndex));
        }
        if (nombreIndex >= 0) sucursal.setNombre(cursor.getString(nombreIndex));
        if (direccionIndex >= 0) sucursal.setDireccion(cursor.getString(direccionIndex));
        if (ciudadIndex >= 0) sucursal.setCiudad(cursor.getString(ciudadIndex));
        if (latitudIndex >= 0 && !cursor.isNull(latitudIndex)) {
            sucursal.setLatitud(cursor.getDouble(latitudIndex));
        }
        if (longitudIndex >= 0 && !cursor.isNull(longitudIndex)) {
            sucursal.setLongitud(cursor.getDouble(longitudIndex));
        }
        if (departamentoIndex >= 0) sucursal.setDepartamento(cursor.getString(departamentoIndex));
        if (telefonoIndex >= 0) sucursal.setTelefono(cursor.getString(telefonoIndex));
        if (createdAtIndex >= 0) sucursal.setCreatedAt(cursor.getString(createdAtIndex));
        if (updatedAtIndex >= 0) sucursal.setUpdatedAt(cursor.getString(updatedAtIndex));
        if (syncStatusIndex >= 0) sucursal.setSyncStatus(cursor.getString(syncStatusIndex));
        if (serverIdIndex >= 0 && !cursor.isNull(serverIdIndex)) {
            sucursal.setServerId(cursor.getInt(serverIdIndex));
        }

        return sucursal;
    }

    // ==================== MÉTODOS CRUD COSTOS GASTOS ====================

    public long insertarCostoGasto(CostoGasto costoGasto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (costoGasto.getId() != null) {
            values.put(COL_CG_ID, costoGasto.getId());
        }
        if (costoGasto.getEmpresaId() != null) {
            values.put(COL_CG_EMPRESA_ID, costoGasto.getEmpresaId());
        }
        if (costoGasto.getSucursalId() != null) {
            values.put(COL_CG_SUCURSAL_ID, costoGasto.getSucursalId());
        }
        values.put(COL_CG_TIPO, costoGasto.getTipo());
        values.put(COL_CG_CATEGORIA, costoGasto.getCategoria());
        values.put(COL_CG_DESCRIPCION, costoGasto.getDescripcion());
        if (costoGasto.getMonto() != null) {
            values.put(COL_CG_MONTO, costoGasto.getMonto());
        }
        values.put(COL_CG_FECHA, costoGasto.getFecha());
        if (costoGasto.getTrabajadorId() != null) {
            values.put(COL_CG_TRABAJADOR_ID, costoGasto.getTrabajadorId());
        }
        values.put(COL_CG_METODO_PAGO, costoGasto.getMetodoPago());
        values.put(COL_CG_COMPROBANTE, costoGasto.getComprobante());
        values.put(COL_CG_CREATED_AT, costoGasto.getCreatedAt());
        values.put(COL_CG_UPDATED_AT, costoGasto.getUpdatedAt());
        values.put(COL_CG_SYNC_STATUS, costoGasto.getSyncStatus() != null ? costoGasto.getSyncStatus() : "pending");
        if (costoGasto.getServerId() != null) {
            values.put(COL_CG_SERVER_ID, costoGasto.getServerId());
        }

        long id = db.insert(TABLE_COSTOS_GASTOS, null, values);
        db.close();
        return id;
    }

    public boolean actualizarCostoGasto(CostoGasto costoGasto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (costoGasto.getEmpresaId() != null) {
            values.put(COL_CG_EMPRESA_ID, costoGasto.getEmpresaId());
        }
        if (costoGasto.getSucursalId() != null) {
            values.put(COL_CG_SUCURSAL_ID, costoGasto.getSucursalId());
        }
        values.put(COL_CG_TIPO, costoGasto.getTipo());
        values.put(COL_CG_CATEGORIA, costoGasto.getCategoria());
        values.put(COL_CG_DESCRIPCION, costoGasto.getDescripcion());
        if (costoGasto.getMonto() != null) {
            values.put(COL_CG_MONTO, costoGasto.getMonto());
        }
        values.put(COL_CG_FECHA, costoGasto.getFecha());
        if (costoGasto.getTrabajadorId() != null) {
            values.put(COL_CG_TRABAJADOR_ID, costoGasto.getTrabajadorId());
        }
        values.put(COL_CG_METODO_PAGO, costoGasto.getMetodoPago());
        values.put(COL_CG_COMPROBANTE, costoGasto.getComprobante());
        values.put(COL_CG_UPDATED_AT, costoGasto.getUpdatedAt());
        values.put(COL_CG_SYNC_STATUS, costoGasto.getSyncStatus() != null ? costoGasto.getSyncStatus() : "pending");
        if (costoGasto.getServerId() != null) {
            values.put(COL_CG_SERVER_ID, costoGasto.getServerId());
        }

        int rowsAffected = db.update(TABLE_COSTOS_GASTOS, values, COL_CG_ID + " = ?",
                new String[]{String.valueOf(costoGasto.getId())});
        db.close();
        return rowsAffected > 0;
    }

    public boolean eliminarCostoGasto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_COSTOS_GASTOS, COL_CG_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public CostoGasto obtenerCostoGasto(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COSTOS_GASTOS, null, COL_CG_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        CostoGasto costoGasto = null;
        if (cursor.moveToFirst()) {
            costoGasto = cursorToCostoGasto(cursor);
        }

        cursor.close();
        db.close();
        return costoGasto;
    }

    public List<CostoGasto> obtenerTodosCostosGastos() {
        List<CostoGasto> costosGastos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COSTOS_GASTOS, null, null, null, null, null, COL_CG_FECHA + " DESC");

        if (cursor.moveToFirst()) {
            do {
                costosGastos.add(cursorToCostoGasto(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return costosGastos;
    }

    public List<CostoGasto> obtenerCostosGastosPorTipo(String tipo) {
        List<CostoGasto> costosGastos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COSTOS_GASTOS, null, COL_CG_TIPO + " = ?",
                new String[]{tipo}, null, null, COL_CG_FECHA + " DESC");

        if (cursor.moveToFirst()) {
            do {
                costosGastos.add(cursorToCostoGasto(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return costosGastos;
    }

    private CostoGasto cursorToCostoGasto(Cursor cursor) {
        CostoGasto costoGasto = new CostoGasto();

        int idIndex = cursor.getColumnIndex(COL_CG_ID);
        int empresaIdIndex = cursor.getColumnIndex(COL_CG_EMPRESA_ID);
        int sucursalIdIndex = cursor.getColumnIndex(COL_CG_SUCURSAL_ID);
        int tipoIndex = cursor.getColumnIndex(COL_CG_TIPO);
        int categoriaIndex = cursor.getColumnIndex(COL_CG_CATEGORIA);
        int descripcionIndex = cursor.getColumnIndex(COL_CG_DESCRIPCION);
        int montoIndex = cursor.getColumnIndex(COL_CG_MONTO);
        int fechaIndex = cursor.getColumnIndex(COL_CG_FECHA);
        int trabajadorIdIndex = cursor.getColumnIndex(COL_CG_TRABAJADOR_ID);
        int metodoPagoIndex = cursor.getColumnIndex(COL_CG_METODO_PAGO);
        int comprobanteIndex = cursor.getColumnIndex(COL_CG_COMPROBANTE);
        int createdAtIndex = cursor.getColumnIndex(COL_CG_CREATED_AT);
        int updatedAtIndex = cursor.getColumnIndex(COL_CG_UPDATED_AT);
        int syncStatusIndex = cursor.getColumnIndex(COL_CG_SYNC_STATUS);
        int serverIdIndex = cursor.getColumnIndex(COL_CG_SERVER_ID);

        if (idIndex >= 0) costoGasto.setId(cursor.getInt(idIndex));
        if (empresaIdIndex >= 0 && !cursor.isNull(empresaIdIndex)) {
            costoGasto.setEmpresaId(cursor.getInt(empresaIdIndex));
        }
        if (sucursalIdIndex >= 0 && !cursor.isNull(sucursalIdIndex)) {
            costoGasto.setSucursalId(cursor.getInt(sucursalIdIndex));
        }
        if (tipoIndex >= 0) costoGasto.setTipo(cursor.getString(tipoIndex));
        if (categoriaIndex >= 0) costoGasto.setCategoria(cursor.getString(categoriaIndex));
        if (descripcionIndex >= 0) costoGasto.setDescripcion(cursor.getString(descripcionIndex));
        if (montoIndex >= 0 && !cursor.isNull(montoIndex)) {
            costoGasto.setMonto(cursor.getDouble(montoIndex));
        }
        if (fechaIndex >= 0) costoGasto.setFecha(cursor.getString(fechaIndex));
        if (trabajadorIdIndex >= 0 && !cursor.isNull(trabajadorIdIndex)) {
            costoGasto.setTrabajadorId(cursor.getInt(trabajadorIdIndex));
        }
        if (metodoPagoIndex >= 0) costoGasto.setMetodoPago(cursor.getString(metodoPagoIndex));
        if (comprobanteIndex >= 0) costoGasto.setComprobante(cursor.getString(comprobanteIndex));
        if (createdAtIndex >= 0) costoGasto.setCreatedAt(cursor.getString(createdAtIndex));
        if (updatedAtIndex >= 0) costoGasto.setUpdatedAt(cursor.getString(updatedAtIndex));
        if (syncStatusIndex >= 0) costoGasto.setSyncStatus(cursor.getString(syncStatusIndex));
        if (serverIdIndex >= 0 && !cursor.isNull(serverIdIndex)) {
            costoGasto.setServerId(cursor.getInt(serverIdIndex));
        }

        return costoGasto;
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

