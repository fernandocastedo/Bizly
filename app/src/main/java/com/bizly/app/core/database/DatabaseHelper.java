package com.bizly.app.core.database;

import android.content.Context;
import androidx.room.Room;

/**
 * Helper para crear y obtener la instancia de la base de datos
 */
public class DatabaseHelper {
    private static final String DATABASE_NAME = "bizly_database";
    private static AppDatabase instance;

    /**
     * Obtiene la instancia de la base de datos (Singleton)
     * @param context Contexto de la aplicación
     * @return Instancia de AppDatabase
     */
    public static synchronized AppDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME
            )
            .fallbackToDestructiveMigration() // En desarrollo: permite recrear la BD si cambia el esquema
            .build();
        }
        return instance;
    }
    
    /**
     * Resetea la instancia singleton (útil para testing o resetear la BD)
     */
    public static synchronized void resetInstance() {
        if (instance != null && instance.isOpen()) {
            instance.close();
        }
        instance = null;
    }
}

