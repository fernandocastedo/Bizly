package com.bizly.app.domain.service;

import android.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Servicio para hash de contraseñas
 * Utilizado en registro y autenticación de usuarios (RF-01, RF-02)
 * 
 * Nota: En producción, se recomienda usar BCrypt o Argon2
 * Esta implementación usa SHA-256 con salt para desarrollo
 */
public class HashPasswordService {
    
    private static final String ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    
    /**
     * Genera un hash de la contraseña con salt
     * @param password Contraseña en texto plano
     * @return String con formato "salt:hash" codificado en Base64
     */
    public static String hashPassword(String password) {
        try {
            // Generar salt aleatorio
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Crear hash de la contraseña con salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hash = md.digest(password.getBytes());
            
            // Codificar salt y hash en Base64 (usando android.util.Base64 para compatibilidad con API 24+)
            String saltBase64 = Base64.encodeToString(salt, Base64.NO_WRAP);
            String hashBase64 = Base64.encodeToString(hash, Base64.NO_WRAP);
            
            // Retornar formato "salt:hash"
            return saltBase64 + ":" + hashBase64;
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash de contraseña", e);
        }
    }
    
    /**
     * Verifica si una contraseña coincide con el hash almacenado
     * @param password Contraseña en texto plano a verificar
     * @param storedHash Hash almacenado en formato "salt:hash"
     * @return true si la contraseña coincide, false en caso contrario
     */
    public static boolean verificarPassword(String password, String storedHash) {
        try {
            // Separar salt y hash del string almacenado
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            byte[] salt = Base64.decode(parts[0], Base64.NO_WRAP);
            byte[] storedHashBytes = Base64.decode(parts[1], Base64.NO_WRAP);
            
            // Crear hash de la contraseña proporcionada con el salt almacenado
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hash = md.digest(password.getBytes());
            
            // Comparar hashes
            return MessageDigest.isEqual(hash, storedHashBytes);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verifica si un hash necesita ser actualizado (para migración futura)
     * @param storedHash Hash almacenado
     * @return true si el hash necesita actualización
     */
    public static boolean necesitaActualizacion(String storedHash) {
        // En el futuro, se puede verificar si el hash usa un algoritmo obsoleto
        return false;
    }
}

