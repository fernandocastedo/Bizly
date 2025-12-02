package com.bizly.app.data.repository.impl;

import android.content.Context;
import com.bizly.app.core.database.DatabaseHelper;
import com.bizly.app.core.mapper.TrabajadorMapper;
import com.bizly.app.data.local.dao.TrabajadorDao;
import com.bizly.app.data.local.entity.TrabajadorEntity;
import com.bizly.app.data.repository.TrabajadorRepository;
import com.bizly.app.domain.model.Trabajador;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación local del repositorio de trabajadores usando Room
 * RF-46, RF-47, RF-51
 */
public class TrabajadorRepositoryLocal implements TrabajadorRepository {
    
    private final TrabajadorDao trabajadorDao;
    
    public TrabajadorRepositoryLocal(Context context) {
        this.trabajadorDao = DatabaseHelper.getDatabase(context).trabajadorDao();
    }
    
    @Override
    public Trabajador registrarTrabajador(Trabajador trabajador) {
        // Establecer fechas si no están establecidas
        if (trabajador.getCreatedAt() == null) {
            trabajador.setCreatedAt(new Date());
        }
        if (trabajador.getUpdatedAt() == null) {
            trabajador.setUpdatedAt(new Date());
        }
        
        TrabajadorEntity entity = TrabajadorMapper.toEntity(trabajador);
        long id = trabajadorDao.insertar(entity);
        trabajador.setId((int) id);
        return trabajador;
    }
    
    @Override
    public Trabajador obtenerTrabajadorPorId(int id) {
        TrabajadorEntity entity = trabajadorDao.obtenerPorId(id);
        return TrabajadorMapper.toModel(entity);
    }
    
    @Override
    public List<Trabajador> obtenerTrabajadoresPorEmpresa(int empresaId) {
        List<TrabajadorEntity> entities = trabajadorDao.obtenerPorEmpresa(empresaId);
        return convertirAListaModel(entities);
    }
    
    @Override
    public List<Trabajador> obtenerTrabajadoresPorEmpresaYSucursal(int empresaId, int sucursalId) {
        List<TrabajadorEntity> entities = trabajadorDao.obtenerPorEmpresaYSucursal(empresaId, sucursalId);
        return convertirAListaModel(entities);
    }
    
    @Override
    public boolean actualizarTrabajador(Trabajador trabajador) {
        try {
            trabajador.setUpdatedAt(new Date());
            TrabajadorEntity entity = TrabajadorMapper.toEntity(trabajador);
            trabajadorDao.actualizar(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean eliminarTrabajador(int id) {
        try {
            trabajadorDao.eliminarPorId(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Convierte una lista de entidades a una lista de modelos
     */
    private List<Trabajador> convertirAListaModel(List<TrabajadorEntity> entities) {
        List<Trabajador> trabajadores = new ArrayList<>();
        if (entities != null) {
            for (TrabajadorEntity entity : entities) {
                trabajadores.add(TrabajadorMapper.toModel(entity));
            }
        }
        return trabajadores;
    }
}

