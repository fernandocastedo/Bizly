package com.bizly.app.data.local.dao;

import androidx.room.*;
import com.bizly.app.data.local.entity.UsuarioEntity;
import java.util.List;

/**
 * DAO para la tabla usuarios
 * RF-01, RF-02, RF-49
 */
@Dao
public interface UsuarioDao {
    
    @Query("SELECT * FROM usuarios WHERE id = :id")
    UsuarioEntity obtenerPorId(int id);
    
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    UsuarioEntity obtenerPorEmail(String email);
    
    @Query("SELECT * FROM usuarios WHERE empresa_id = :empresaId")
    List<UsuarioEntity> obtenerPorEmpresa(int empresaId);
    
    @Query("SELECT * FROM usuarios WHERE empresa_id = :empresaId AND tipo_usuario = :tipoUsuario")
    List<UsuarioEntity> obtenerPorEmpresaYTipo(int empresaId, String tipoUsuario);
    
    @Query("SELECT * FROM usuarios WHERE trabajador_id = :trabajadorId")
    UsuarioEntity obtenerPorTrabajador(int trabajadorId);
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertar(UsuarioEntity usuario);
    
    @Update
    void actualizar(UsuarioEntity usuario);
    
    @Query("UPDATE usuarios SET activo = :activo WHERE id = :id")
    void actualizarEstado(int id, boolean activo);
    
    @Delete
    void eliminar(UsuarioEntity usuario);
}

