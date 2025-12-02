package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;
import com.bizly.app.core.database.TypeConverters;
import java.util.Date;

/**
 * Entidad Room para la tabla trabajadores
 * RF-46, RF-47
 */
@Entity(
    tableName = "trabajadores",
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
            onDelete = ForeignKey.SET_NULL
        )
    }
)
@TypeConverters(TypeConverters.class)
public class TrabajadorEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "sucursal_id")
    public Integer sucursalId;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "cargo")
    public String cargo;

    @ColumnInfo(name = "sueldo_mensual")
    public double sueldoMensual;

    @ColumnInfo(name = "tipo_gasto")
    public String tipoGasto;  // fijo / variable

    @ColumnInfo(name = "created_at")
    public Date createdAt;

    @ColumnInfo(name = "updated_at")
    public Date updatedAt;
}

