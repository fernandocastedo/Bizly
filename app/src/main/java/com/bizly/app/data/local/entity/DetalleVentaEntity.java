package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

/**
 * Entidad Room para la tabla detalle_ventas
 */
@Entity(
    tableName = "detalle_ventas",
    foreignKeys = {
        @ForeignKey(
            entity = VentaEntity.class,
            parentColumns = "id",
            childColumns = "venta_id",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = ProductoVentaEntity.class,
            parentColumns = "id",
            childColumns = "producto_venta_id",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class DetalleVentaEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "venta_id")
    public int ventaId;

    @ColumnInfo(name = "producto_venta_id")
    public int productoVentaId;

    @ColumnInfo(name = "cantidad")
    public int cantidad;

    @ColumnInfo(name = "precio_unitario")
    public double precioUnitario;

    @ColumnInfo(name = "subtotal")
    public double subtotal;
}

