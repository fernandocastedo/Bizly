package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;
import com.bizly.app.core.database.TypeConverters;
import java.util.Date;

/**
 * Entidad Room para la tabla costos_gastos
 * RF-32, RF-33, RF-34, RF-36, RF-37, RF-46, RF-47
 */
@Entity(
    tableName = "costos_gastos",
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
            entity = UsuarioEntity.class,
            parentColumns = "id",
            childColumns = "usuario_id",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = InsumoEntity.class,
            parentColumns = "id",
            childColumns = "insumo_id",
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
@TypeConverters(TypeConverters.class)
public class CostoGastoEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "sucursal_id")
    public Integer sucursalId;

    @ColumnInfo(name = "usuario_id")
    public int usuarioId;

    @ColumnInfo(name = "categoria_financiera")
    public String categoriaFinanciera;  // DIRECTO / ADMINISTRATIVO

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "monto")
    public double monto;

    @ColumnInfo(name = "fecha")
    public Date fecha;

    @ColumnInfo(name = "clasificacion")
    public String clasificacion;  // FIJO / VARIABLE

    @ColumnInfo(name = "insumo_id")
    public Integer insumoId;

    @ColumnInfo(name = "trabajador_id")
    public Integer trabajadorId;

    @ColumnInfo(name = "created_at")
    public Date createdAt;
}

