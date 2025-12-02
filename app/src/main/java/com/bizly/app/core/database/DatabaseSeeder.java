package com.bizly.app.core.database;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.bizly.app.data.repository.EmpresaRepository;
import com.bizly.app.data.repository.UsuarioRepository;
import com.bizly.app.data.repository.impl.EmpresaRepositoryLocal;
import com.bizly.app.data.repository.impl.UsuarioRepositoryLocal;
import com.bizly.app.domain.model.Empresa;
import com.bizly.app.domain.model.Usuario;
import com.bizly.app.domain.service.HashPasswordService;
import java.util.Date;

/**
 * Seeder para poblar la base de datos con datos de prueba
 * Útil para desarrollo y testing
 */
public class DatabaseSeeder {
    
    private final Context context;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    
    public DatabaseSeeder(Context context) {
        this.context = context;
    }
    
    /**
     * Pobla la base de datos con datos de prueba
     * Crea una empresa y un usuario emprendedor de prueba
     */
    public void seedDatabase() {
        executor.execute(() -> {
            try {
                seedEmpresaYUsuario();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Crea una empresa y usuario de prueba
     */
    private void seedEmpresaYUsuario() {
        EmpresaRepository empresaRepository = new EmpresaRepositoryLocal(context);
        UsuarioRepository usuarioRepository = new UsuarioRepositoryLocal(context);
        
        // Verificar si ya existe un usuario de prueba
        Usuario usuarioExistente = usuarioRepository.obtenerUsuarioPorEmail("test@bizly.com");
        if (usuarioExistente != null) {
            // Ya existe, no hacer nada
            return;
        }
        
        // Crear empresa de prueba
        Empresa empresa = new Empresa();
        empresa.setNombre("Mi Emprendimiento de Prueba");
        empresa.setRubro("Gastronomía");
        empresa.setDescripcion("Emprendimiento de prueba para desarrollo");
        empresa.setMargenGanancia(30.0); // 30% de margen
        empresa.setLogoUrl(null);
        empresa.setCreatedAt(new Date());
        empresa.setUpdatedAt(new Date());
        
        Empresa empresaCreada = empresaRepository.registrarEmpresa(empresa);
        
        // Crear usuario emprendedor de prueba
        Usuario usuario = new Usuario();
        usuario.setEmpresaId(empresaCreada.getId());
        usuario.setSucursalId(null);
        usuario.setTrabajadorId(null);
        usuario.setNombre("Usuario de Prueba");
        usuario.setEmail("test@bizly.com");
        usuario.setPassword(HashPasswordService.hashPassword("123456")); // Contraseña: 123456
        usuario.setTipoUsuario("EMPRENDEDOR");
        usuario.setActivo(true);
        usuario.setCreatedAt(new Date());
        usuario.setUpdatedAt(new Date());
        
        usuarioRepository.registrarUsuario(usuario);
    }
    
    /**
     * Limpia todos los datos de prueba (útil para resetear)
     * Elimina completamente la base de datos
     */
    public void clearDatabase() {
        executor.execute(() -> {
            try {
                // Cerrar la instancia actual si existe
                AppDatabase db = DatabaseHelper.getDatabase(context);
                if (db != null && db.isOpen()) {
                    db.close();
                }
                
                // Resetear la instancia singleton primero
                DatabaseHelper.resetInstance();
                
                // Eliminar la base de datos
                boolean deleted = context.deleteDatabase("bizly_database");
                
                handler.post(() -> {
                    // Notificar que se completó
                    android.util.Log.d("DatabaseSeeder", "Base de datos eliminada: " + deleted);
                });
            } catch (Exception e) {
                e.printStackTrace();
                handler.post(() -> {
                    android.util.Log.e("DatabaseSeeder", "Error al eliminar base de datos: " + e.getMessage());
                });
            }
        });
    }
}

