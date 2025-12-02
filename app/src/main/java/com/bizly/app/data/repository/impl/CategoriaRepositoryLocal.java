package com.bizly.app.data.repository.impl;

import android.content.Context;
import com.bizly.app.core.database.DatabaseHelper;
import com.bizly.app.core.mapper.CategoriaMapper;
import com.bizly.app.data.local.dao.CategoriaDao;
import com.bizly.app.data.local.entity.CategoriaEntity;
import com.bizly.app.data.repository.CategoriaRepository;
import com.bizly.app.domain.model.Categoria;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación local del repositorio de categorías usando Room
 */
public class CategoriaRepositoryLocal implements CategoriaRepository {
    
    private final CategoriaDao categoriaDao;
    
    public CategoriaRepositoryLocal(Context context) {
        this.categoriaDao = DatabaseHelper.getDatabase(context).categoriaDao();
    }
    
    @Override
    public Categoria registrarCategoria(Categoria categoria) {
        // Establecer fecha si no está establecida
        if (categoria.getCreatedAt() == null) {
            categoria.setCreatedAt(new Date());
        }
        
        CategoriaEntity entity = CategoriaMapper.toEntity(categoria);
        long id = categoriaDao.insertar(entity);
        categoria.setId((int) id);
        return categoria;
    }
    
    @Override
    public Categoria obtenerCategoriaPorId(int id) {
        CategoriaEntity entity = categoriaDao.obtenerPorId(id);
        return CategoriaMapper.toModel(entity);
    }
    
    @Override
    public List<Categoria> obtenerCategoriasPorEmpresa(int empresaId) {
        List<CategoriaEntity> entities = categoriaDao.obtenerPorEmpresa(empresaId);
        return convertirAListaModel(entities);
    }
    
    @Override
    public boolean actualizarCategoria(Categoria categoria) {
        try {
            CategoriaEntity entity = CategoriaMapper.toEntity(categoria);
            categoriaDao.actualizar(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean eliminarCategoria(int id) {
        try {
            CategoriaEntity categoria = categoriaDao.obtenerPorId(id);
            if (categoria != null) {
                categoriaDao.eliminar(categoria);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Convierte una lista de entidades a una lista de modelos
     */
    private List<Categoria> convertirAListaModel(List<CategoriaEntity> entities) {
        List<Categoria> categorias = new ArrayList<>();
        if (entities != null) {
            for (CategoriaEntity entity : entities) {
                categorias.add(CategoriaMapper.toModel(entity));
            }
        }
        return categorias;
    }
}

