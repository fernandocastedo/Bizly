package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.TypeConverters;
import com.bizly.app.core.database.AppTypeConverters;
import java.util.Date;

/**
 * Entidad Room para la tabla usuarios
 * RF-01, RF-02, RF-49
 */
@Entity(
    tableName = "usuarios",
    indices = {@Index(value = "email", unique = true)},
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
        ),
        @ForeignKey(
            entity = TrabajadorEntity.class,
            parentColumns = "id",
            childColumns = "trabajador_id",
            onDelete = ForeignKey.SET_NULL
        )
    }
)
@TypeConverters(AppTypeConverters.class)
public class UsuarioEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "sucursal_id")
    public Integer sucursalId;

    @ColumnInfo(name = "trabajador_id")
    public Integer trabajadorId;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "tipo_usuario")
    public String tipoUsuario;  // EMPRENDEDOR / TRABAJADOR

    @ColumnInfo(name = "activo")
    public boolean activo;

    @ColumnInfo(name = "created_at")
    public Date createdAt;

    @ColumnInfo(name = "updated_at")
    public Date updatedAt;
}

