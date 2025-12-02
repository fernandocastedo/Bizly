package com.bizly.app.data.repository.impl;

import android.content.Context;
import com.bizly.app.core.database.DatabaseHelper;
import com.bizly.app.core.mapper.InsumoMapper;
import com.bizly.app.data.local.dao.InsumoDao;
import com.bizly.app.data.local.entity.InsumoEntity;
import com.bizly.app.data.repository.InventarioRepository;
import com.bizly.app.domain.model.Insumo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación local del repositorio de inventario usando Room
 * RF-08, RF-09, RF-11, RF-12, RF-13, RF-14
 */
public class InventarioRepositoryLocal implements InventarioRepository {
    
    private final InsumoDao insumoDao;
    
    public InventarioRepositoryLocal(Context context) {
        this.insumoDao = DatabaseHelper.getDatabase(context).insumoDao();
    }
    
    @Override
    public Insumo registrarInsumo(Insumo insumo) {
        // Establecer fechas si no están establecidas
        if (insumo.getCreatedAt() == null) {
            insumo.setCreatedAt(new Date());
        }
        if (insumo.getUpdatedAt() == null) {
            insumo.setUpdatedAt(new Date());
        }
        
        InsumoEntity entity = InsumoMapper.toEntity(insumo);
        long id = insumoDao.insertar(entity);
        insumo.setId((int) id);
        return insumo;
    }
    
    @Override
    public List<Insumo> registrarInsumosDesdeFactura(List<Insumo> insumos) {
        List<Insumo> insumosRegistrados = new ArrayList<>();
        Date now = new Date();
        
        for (Insumo insumo : insumos) {
            if (insumo.getCreatedAt() == null) {
                insumo.setCreatedAt(now);
            }
            if (insumo.getUpdatedAt() == null) {
                insumo.setUpdatedAt(now);
            }
            
            InsumoEntity entity = InsumoMapper.toEntity(insumo);
            long id = insumoDao.insertar(entity);
            insumo.setId((int) id);
            insumosRegistrados.add(insumo);
        }
        
        return insumosRegistrados;
    }
    
    @Override
    public List<Insumo> obtenerInsumos(int empresaId, int sucursalId) {
        List<InsumoEntity> entities = insumoDao.obtenerTodos(empresaId, sucursalId);
        return convertirAListaModel(entities);
    }
    
    @Override
    public List<Insumo> obtenerInsumosFiltrados(int empresaId, int sucursalId, String nombre, Integer categoriaId) {
        List<InsumoEntity> entities;
        
        if (nombre != null && !nombre.isEmpty()) {
            entities = insumoDao.buscarPorNombre(empresaId, sucursalId, nombre);
        } else if (categoriaId != null) {
            entities = insumoDao.obtenerPorCategoria(empresaId, sucursalId, categoriaId);
        } else {
            entities = insumoDao.obtenerTodos(empresaId, sucursalId);
        }
        
        return convertirAListaModel(entities);
    }
    
    @Override
    public boolean actualizarStock(int insumoId, double nuevaCantidad, String motivo) {
        try {
            Date now = new Date();
            insumoDao.actualizarCantidad(insumoId, nuevaCantidad, now);
            
            // TODO: Registrar movimiento en RegistroInventarioDao (RF-12)
            // Esto se implementará cuando se cree el repositorio de registros
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean eliminarInsumo(int insumoId) {
        try {
            insumoDao.desactivar(insumoId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Insumo> obtenerInsumosStockBajo(int empresaId, int sucursalId) {
        List<InsumoEntity> entities = insumoDao.obtenerStockBajo(empresaId, sucursalId);
        return convertirAListaModel(entities);
    }
    
    @Override
    public Insumo obtenerInsumoPorId(int id) {
        InsumoEntity entity = insumoDao.obtenerPorId(id);
        return InsumoMapper.toModel(entity);
    }
    
    @Override
    public Insumo buscarInsumoPorNombre(int empresaId, int sucursalId, String nombre) {
        InsumoEntity entity = insumoDao.buscarPorNombreExacto(empresaId, sucursalId, nombre);
        return InsumoMapper.toModel(entity);
    }
    
    /**
     * Convierte una lista de entidades a una lista de modelos
     */
    private List<Insumo> convertirAListaModel(List<InsumoEntity> entities) {
        List<Insumo> insumos = new ArrayList<>();
        if (entities != null) {
            for (InsumoEntity entity : entities) {
                insumos.add(InsumoMapper.toModel(entity));
            }
        }
        return insumos;
    }
}

