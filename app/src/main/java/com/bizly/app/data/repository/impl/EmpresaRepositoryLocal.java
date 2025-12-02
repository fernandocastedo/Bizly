package com.bizly.app.data.repository.impl;

import android.content.Context;
import com.bizly.app.core.database.DatabaseHelper;
import com.bizly.app.core.mapper.EmpresaMapper;
import com.bizly.app.data.local.dao.EmpresaDao;
import com.bizly.app.data.local.entity.EmpresaEntity;
import com.bizly.app.data.repository.EmpresaRepository;
import com.bizly.app.domain.model.Empresa;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación local del repositorio de empresas usando Room
 * RF-03, RF-04, RF-05, RF-06, RF-07
 */
public class EmpresaRepositoryLocal implements EmpresaRepository {
    
    private final EmpresaDao empresaDao;
    
    public EmpresaRepositoryLocal(Context context) {
        this.empresaDao = DatabaseHelper.getDatabase(context).empresaDao();
    }
    
    @Override
    public Empresa registrarEmpresa(Empresa empresa) {
        // Establecer fechas si no están establecidas
        if (empresa.getCreatedAt() == null) {
            empresa.setCreatedAt(new Date());
        }
        if (empresa.getUpdatedAt() == null) {
            empresa.setUpdatedAt(new Date());
        }
        
        EmpresaEntity entity = EmpresaMapper.toEntity(empresa);
        long id = empresaDao.insertar(entity);
        empresa.setId((int) id);
        return empresa;
    }
    
    @Override
    public Empresa obtenerEmpresaPorId(int id) {
        EmpresaEntity entity = empresaDao.obtenerPorId(id);
        return EmpresaMapper.toModel(entity);
    }
    
    @Override
    public List<Empresa> obtenerTodasLasEmpresas() {
        List<EmpresaEntity> entities = empresaDao.obtenerTodas();
        return convertirAListaModel(entities);
    }
    
    @Override
    public boolean actualizarEmpresa(Empresa empresa) {
        try {
            empresa.setUpdatedAt(new Date());
            EmpresaEntity entity = EmpresaMapper.toEntity(empresa);
            empresaDao.actualizar(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean eliminarEmpresa(int id) {
        try {
            empresaDao.eliminarPorId(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Convierte una lista de entidades a una lista de modelos
     */
    private List<Empresa> convertirAListaModel(List<EmpresaEntity> entities) {
        List<Empresa> empresas = new ArrayList<>();
        if (entities != null) {
            for (EmpresaEntity entity : entities) {
                empresas.add(EmpresaMapper.toModel(entity));
            }
        }
        return empresas;
    }
}

