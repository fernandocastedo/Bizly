package com.bizly.app.core.exception;

/**
 * Excepción para errores de red.
 */
public class NetworkException extends AppException {
    public NetworkException(String message) {
        super(message);
    }

    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}

