package com.bizly.app.data.repository.impl;

import android.content.Context;
import com.bizly.app.core.database.DatabaseHelper;
import com.bizly.app.core.mapper.SucursalMapper;
import com.bizly.app.data.local.dao.SucursalDao;
import com.bizly.app.data.local.entity.SucursalEntity;
import com.bizly.app.data.repository.SucursalRepository;
import com.bizly.app.domain.model.Sucursal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación local del repositorio de sucursales usando Room
 */
public class SucursalRepositoryLocal implements SucursalRepository {
    
    private final SucursalDao sucursalDao;
    
    public SucursalRepositoryLocal(Context context) {
        this.sucursalDao = DatabaseHelper.getDatabase(context).sucursalDao();
    }
    
    @Override
    public Sucursal registrarSucursal(Sucursal sucursal) {
        // Establecer fechas si no están establecidas
        if (sucursal.getCreatedAt() == null) {
            sucursal.setCreatedAt(new Date());
        }
        if (sucursal.getUpdatedAt() == null) {
            sucursal.setUpdatedAt(new Date());
        }
        
        SucursalEntity entity = SucursalMapper.toEntity(sucursal);
        long id = sucursalDao.insertar(entity);
        sucursal.setId((int) id);
        return sucursal;
    }
    
    @Override
    public Sucursal obtenerSucursalPorId(int id) {
        SucursalEntity entity = sucursalDao.obtenerPorId(id);
        return SucursalMapper.toModel(entity);
    }
    
    @Override
    public List<Sucursal> obtenerSucursalesPorEmpresa(int empresaId) {
        List<SucursalEntity> entities = sucursalDao.obtenerPorEmpresa(empresaId);
        return convertirAListaModel(entities);
    }
    
    @Override
    public boolean actualizarSucursal(Sucursal sucursal) {
        try {
            sucursal.setUpdatedAt(new Date());
            SucursalEntity entity = SucursalMapper.toEntity(sucursal);
            sucursalDao.actualizar(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean eliminarSucursal(int id) {
        try {
            sucursalDao.eliminarPorId(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Convierte una lista de entidades a una lista de modelos
     */
    private List<Sucursal> convertirAListaModel(List<SucursalEntity> entities) {
        List<Sucursal> sucursales = new ArrayList<>();
        if (entities != null) {
            for (SucursalEntity entity : entities) {
                sucursales.add(SucursalMapper.toModel(entity));
            }
        }
        return sucursales;
    }
}

