package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;
import com.bizly.app.core.database.TypeConverters;
import java.util.Date;

/**
 * Entidad Room para la tabla registros_inventario
 * RF-12 - Historial de movimientos de inventario
 */
@Entity(
    tableName = "registros_inventario",
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
            entity = UsuarioEntity.class,
            parentColumns = "id",
            childColumns = "usuario_id",
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
@TypeConverters(TypeConverters.class)
public class RegistroInventarioEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "sucursal_id")
    public int sucursalId;

    @ColumnInfo(name = "usuario_id")
    public int usuarioId;

    @ColumnInfo(name = "insumo_id")
    public int insumoId;

    @ColumnInfo(name = "tipo_movimiento")
    public String tipoMovimiento;  // entrada / salida / ajuste

    @ColumnInfo(name = "cantidad_anterior")
    public double cantidadAnterior;

    @ColumnInfo(name = "cantidad_nueva")
    public double cantidadNueva;

    @ColumnInfo(name = "motivo")
    public String motivo;

    @ColumnInfo(name = "created_at")
    public Date createdAt;
}

