package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;
import com.bizly.app.core.database.AppTypeConverters;
import java.util.Date;

/**
 * Entidad Room para la tabla categorias
 */
@Entity(
    tableName = "categorias",
    foreignKeys = @ForeignKey(
        entity = EmpresaEntity.class,
        parentColumns = "id",
        childColumns = "empresa_id",
        onDelete = ForeignKey.CASCADE
    )
)
@TypeConverters(AppTypeConverters.class)
public class CategoriaEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "created_at")
    public Date createdAt;
}

