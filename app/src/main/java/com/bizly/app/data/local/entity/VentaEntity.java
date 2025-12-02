package com.bizly.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;
import com.bizly.app.core.database.TypeConverters;
import java.util.Date;

/**
 * Entidad Room para la tabla ventas
 * RF-22, RF-24, RF-25, RF-26, RF-29, RF-30, RF-31
 */
@Entity(
    tableName = "ventas",
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
            entity = ClienteEntity.class,
            parentColumns = "id",
            childColumns = "cliente_id",
            onDelete = ForeignKey.SET_NULL
        )
    }
)
@TypeConverters(TypeConverters.class)
public class VentaEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "sucursal_id")
    public int sucursalId;

    @ColumnInfo(name = "usuario_id")
    public int usuarioId;

    @ColumnInfo(name = "cliente_id")
    public Integer clienteId;

    @ColumnInfo(name = "fecha")
    public Date fecha;

    @ColumnInfo(name = "metodo_pago")
    public String metodoPago;

    @ColumnInfo(name = "total")
    public double total;

    @ColumnInfo(name = "es_envio")
    public boolean esEnvio;

    @ColumnInfo(name = "estado_pago")
    public String estadoPago;  // pagado / pendiente

    @ColumnInfo(name = "estado_pedido")
    public String estadoPedido;  // pendiente / completado / cancelado

    @ColumnInfo(name = "created_at")
    public Date createdAt;
}

