package com.bizly.app.data.repository.impl;

import android.content.Context;
import com.bizly.app.core.database.DatabaseHelper;
import com.bizly.app.core.mapper.VentaMapper;
import com.bizly.app.data.local.dao.VentaDao;
import com.bizly.app.data.local.entity.VentaEntity;
import com.bizly.app.data.repository.VentaRepository;
import com.bizly.app.domain.model.Venta;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación local del repositorio de ventas usando Room
 * RF-22, RF-24, RF-25, RF-26, RF-27, RF-28, RF-29, RF-30, RF-31
 */
public class VentaRepositoryLocal implements VentaRepository {
    
    private final VentaDao ventaDao;
    
    public VentaRepositoryLocal(Context context) {
        this.ventaDao = DatabaseHelper.getDatabase(context).ventaDao();
    }
    
    @Override
    public Venta registrarVenta(Venta venta) {
        // Establecer fecha si no está establecida
        if (venta.getFecha() == null) {
            venta.setFecha(new Date());
        }
        if (venta.getCreatedAt() == null) {
            venta.setCreatedAt(new Date());
        }
        
        // Establecer estados por defecto si no están establecidos
        if (venta.getEstadoPago() == null || venta.getEstadoPago().isEmpty()) {
            venta.setEstadoPago("pagado");
        }
        if (venta.getEstadoPedido() == null || venta.getEstadoPedido().isEmpty()) {
            venta.setEstadoPedido(venta.isEnvio() ? "pendiente" : "completado");
        }
        
        VentaEntity entity = VentaMapper.toEntity(venta);
        long id = ventaDao.insertar(entity);
        venta.setId((int) id);
        return venta;
    }
    
    @Override
    public List<Venta> obtenerVentas(int empresaId, int sucursalId) {
        List<VentaEntity> entities = ventaDao.obtenerTodas(empresaId, sucursalId);
        return convertirAListaModel(entities);
    }
    
    @Override
    public List<Venta> obtenerVentasFiltradas(int empresaId, int sucursalId, Date fechaInicio, Date fechaFin, Integer usuarioId) {
        List<VentaEntity> entities;
        
        if (usuarioId != null) {
            entities = ventaDao.obtenerPorUsuario(empresaId, sucursalId, usuarioId);
        } else if (fechaInicio != null && fechaFin != null) {
            entities = ventaDao.obtenerPorRangoFechas(empresaId, sucursalId, fechaInicio, fechaFin);
        } else {
            entities = ventaDao.obtenerTodas(empresaId, sucursalId);
        }
        
        return convertirAListaModel(entities);
    }
    
    @Override
    public List<Object[]> obtenerTopVendedores(int empresaId, int sucursalId, Date fechaInicio, Date fechaFin) {
        // Convertir TopVendedorResult a Object[] para mantener compatibilidad con la interfaz
        List<com.bizly.app.data.local.entity.TopVendedorResult> resultados = ventaDao.obtenerTopVendedores(empresaId, sucursalId, fechaInicio, fechaFin);
        List<Object[]> lista = new ArrayList<>();
        for (com.bizly.app.data.local.entity.TopVendedorResult resultado : resultados) {
            lista.add(new Object[]{resultado.usuarioId, resultado.totalVentas, resultado.totalMonto});
        }
        return lista;
    }
    
    @Override
    public boolean cancelarVenta(int ventaId) {
        try {
            VentaEntity venta = ventaDao.obtenerPorId(ventaId);
            if (venta != null) {
                venta.estadoPedido = "cancelado";
                ventaDao.actualizar(venta);
                
                // TODO: Restaurar stock de insumos (RF-28)
                // Esto se implementará cuando se cree el servicio de descuento de inventario
                
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Venta> obtenerPedidosPendientes(int empresaId, int sucursalId) {
        List<VentaEntity> entities = ventaDao.obtenerPedidosPendientes(empresaId, sucursalId);
        return convertirAListaModel(entities);
    }
    
    @Override
    public boolean actualizarEstadoPedido(int ventaId, String estadoPedido, String estadoPago) {
        try {
            ventaDao.actualizarEstado(ventaId, estadoPedido, estadoPago);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Venta obtenerVentaPorId(int id) {
        VentaEntity entity = ventaDao.obtenerPorId(id);
        return VentaMapper.toModel(entity);
    }
    
    @Override
    public double calcularTotalVentas(int empresaId, int sucursalId, Date fechaInicio, Date fechaFin) {
        Double total = ventaDao.calcularTotalVentas(empresaId, sucursalId, fechaInicio, fechaFin);
        return total != null ? total : 0.0;
    }
    
    /**
     * Convierte una lista de entidades a una lista de modelos
     */
    private List<Venta> convertirAListaModel(List<VentaEntity> entities) {
        List<Venta> ventas = new ArrayList<>();
        if (entities != null) {
            for (VentaEntity entity : entities) {
                ventas.add(VentaMapper.toModel(entity));
            }
        }
        return ventas;
    }
}

