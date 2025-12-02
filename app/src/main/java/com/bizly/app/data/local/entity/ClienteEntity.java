package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;
import com.bizly.app.core.database.TypeConverters;
import java.util.Date;

/**
 * Entidad Room para la tabla clientes
 * RF-26, RF-29, RF-40
 */
@Entity(
    tableName = "clientes",
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
public class ClienteEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "sucursal_id")
    public Integer sucursalId;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "nit")
    public Integer nit;

    @ColumnInfo(name = "telefono")
    public String telefono;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "direccion")
    public String direccion;

    @ColumnInfo(name = "created_at")
    public Date createdAt;
}

