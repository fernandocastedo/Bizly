package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;
import com.bizly.app.core.database.AppTypeConverters;
import java.util.Date;

/**
 * Entidad Room para la tabla sucursales
 */
@Entity(
    tableName = "sucursales",
    foreignKeys = @ForeignKey(
        entity = EmpresaEntity.class,
        parentColumns = "id",
        childColumns = "empresa_id",
        onDelete = ForeignKey.CASCADE
    )
)
@TypeConverters(AppTypeConverters.class)
public class SucursalEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "direccion")
    public String direccion;

    @ColumnInfo(name = "ciudad")
    public String ciudad;

    @ColumnInfo(name = "latitud")
    public double latitud;

    @ColumnInfo(name = "longitud")
    public double longitud;

    @ColumnInfo(name = "departamento")
    public String departamento;

    @ColumnInfo(name = "telefono")
    public String telefono;

    @ColumnInfo(name = "created_at")
    public Date createdAt;

    @ColumnInfo(name = "updated_at")
    public Date updatedAt;
}

