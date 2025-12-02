package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;
import com.bizly.app.core.database.AppTypeConverters;
import java.util.Date;

/**
 * Entidad Room para la tabla productos_venta
 * RF-15, RF-19, RF-20
 */
@Entity(
    tableName = "productos_venta",
    foreignKeys = {
        @ForeignKey(
            entity = EmpresaEntity.class,
            parentColumns = "id",
            childColumns = "empresa_id",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = SucursalEntity.class,
            parentColumns = "id",
            childColumns = "sucursal_id",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = CategoriaEntity.class,
            parentColumns = "id",
            childColumns = "categoria_id",
            onDelete = ForeignKey.SET_NULL
        )
    }
)
@TypeConverters(AppTypeConverters.class)
public class ProductoVentaEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "sucursal_id")
    public int sucursalId;

    @ColumnInfo(name = "categoria_id")
    public Integer categoriaId;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "precio_venta")
    public double precioVenta;

    @ColumnInfo(name = "activo")
    public boolean activo;

    @ColumnInfo(name = "created_at")
    public Date createdAt;

    @ColumnInfo(name = "updated_at")
    public Date updatedAt;
}

