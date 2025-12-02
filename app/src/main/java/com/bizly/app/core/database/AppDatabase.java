package com.bizly.app.core.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.bizly.app.data.local.entity.*;
import com.bizly.app.data.local.dao.*;
import com.bizly.app.core.database.AppTypeConverters;

/**
 * Base de datos principal de la aplicación usando Room
 * Versión 1 - Primera versión del esquema
 */
@Database(
    entities = {
        EmpresaEntity.class,
        SucursalEntity.class,
        UsuarioEntity.class,
        TrabajadorEntity.class,
        CategoriaEntity.class,
        InsumoEntity.class,
        RegistroInventarioEntity.class,
        ProductoVentaEntity.class,
        InsumoProductoVentaEntity.class,
        ClienteEntity.class,
        VentaEntity.class,
        DetalleVentaEntity.class,
        CostoGastoEntity.class
    },
    version = 1,
    exportSchema = false
)
@TypeConverters(AppTypeConverters.class)
public abstract class AppDatabase extends RoomDatabase {
    
    // DAOs
    public abstract EmpresaDao empresaDao();
    public abstract SucursalDao sucursalDao();
    public abstract UsuarioDao usuarioDao();
    public abstract TrabajadorDao trabajadorDao();
    public abstract CategoriaDao categoriaDao();
    public abstract InsumoDao insumoDao();
    public abstract RegistroInventarioDao registroInventarioDao();
    public abstract ProductoVentaDao productoVentaDao();
    public abstract InsumoProductoVentaDao insumoProductoVentaDao();
    public abstract ClienteDao clienteDao();
    public abstract VentaDao ventaDao();
    public abstract DetalleVentaDao detalleVentaDao();
    public abstract CostoGastoDao costoGastoDao();
}

