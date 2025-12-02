package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;
import com.bizly.app.core.database.AppTypeConverters;
import java.util.Date;

/**
 * Entidad Room para la tabla insumo_producto_venta
 * RF-16, RF-21 - Relación entre insumos y productos de venta
 */
@Entity(
    tableName = "insumo_producto_venta",
    foreignKeys = {
        @ForeignKey(
            entity = ProductoVentaEntity.class,
            parentColumns = "id",
            childColumns = "producto_venta_id",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = InsumoEntity.class,
            parentColumns = "id",
            childColumns = "insumo_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
@TypeConverters(AppTypeConverters.class)
public class InsumoProductoVentaEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "producto_venta_id")
    public int productoVentaId;

    @ColumnInfo(name = "insumo_id")
    public int insumoId;

    @ColumnInfo(name = "cantidad_utilizada")
    public double cantidadUtilizada;

    @ColumnInfo(name = "created_at")
    public Date createdAt;
}

