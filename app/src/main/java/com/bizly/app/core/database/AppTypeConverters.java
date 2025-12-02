package com.bizly.app.core.database;

import androidx.room.TypeConverter;
import java.util.Date;

/**
 * Convertidores de tipos para Room Database
 * Convierte tipos que Room no soporta nativamente (como Date) a tipos compatibles
 */
public class AppTypeConverters {

    /**
     * Convierte un Long (timestamp) a Date
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Convierte un Date a Long (timestamp)
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}

