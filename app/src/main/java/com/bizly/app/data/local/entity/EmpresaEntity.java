package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;
import com.bizly.app.core.database.TypeConverters;
import java.util.Date;

/**
 * Entidad Room para la tabla empresas
 */
@Entity(tableName = "empresas")
@TypeConverters(TypeConverters.class)
public class EmpresaEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "rubro")
    public String rubro;

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "margen_ganancia")
    public double margenGanancia;

    @ColumnInfo(name = "logo_url")
    public String logoUrl;

    @ColumnInfo(name = "created_at")
    public Date createdAt;

    @ColumnInfo(name = "updated_at")
    public Date updatedAt;
}

