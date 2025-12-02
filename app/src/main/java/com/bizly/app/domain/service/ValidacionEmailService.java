package com.bizly.app.domain.service;

import java.util.regex.Pattern;

/**
 * Servicio para validación de emails
 * Utilizado en registro de usuarios (RF-01)
 */
public class ValidacionEmailService {
    
    // Patrón regex para validar formato de email
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    /**
     * Valida si un email tiene un formato válido
     * @param email Email a validar
     * @return true si el email es válido, false en caso contrario
     */
    public static boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return pattern.matcher(email.trim()).matches();
    }
    
    /**
     * Valida si un email tiene un formato válido y lanza excepción si no es válido
     * @param email Email a validar
     * @throws IllegalArgumentException si el email no es válido
     */
    public static void validarEmail(String email) {
        if (!esEmailValido(email)) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }
    }
}

