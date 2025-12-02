package com.bizly.app.presentation.base;

/**
 * Clase base para manejar estados de la UI.
 * Las clases hijas pueden extender este estado según sus necesidades.
 */
public class BaseState {
    public static final int STATE_IDLE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_SUCCESS = 2;
    public static final int STATE_ERROR = 3;

    protected int currentState = STATE_IDLE;
    private String message;

    public BaseState(int currentState) {
        this.currentState = currentState;
    }

    public BaseState(int currentState, String message) {
        this.currentState = currentState;
        this.message = message;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLoading() {
        return currentState == STATE_LOADING;
    }

    public boolean isSuccess() {
        return currentState == STATE_SUCCESS;
    }

    public boolean isError() {
        return currentState == STATE_ERROR;
    }
}

