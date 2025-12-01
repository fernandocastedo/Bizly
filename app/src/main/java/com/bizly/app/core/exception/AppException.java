package com.bizly.app.core.exception;

/**
 * Excepción base para la aplicación.
 * Permite manejar errores de manera centralizada.
 */
public class AppException extends Exception {

    private String errorCode;
    private String userMessage;

    public AppException(String message) {
        super(message);
        this.userMessage = message;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.userMessage = message;
    }

    public AppException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userMessage = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getUserMessage() {
        return userMessage;
    }
}

