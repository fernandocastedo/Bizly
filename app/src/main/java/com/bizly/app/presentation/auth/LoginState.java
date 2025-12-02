package com.bizly.app.presentation.auth;

import com.bizly.app.presentation.base.BaseState;

/**
 * Estado específico para la pantalla de login
 */
public class LoginState extends BaseState {
    
    public static final int STATE_LOGIN_SUCCESS = 10;
    
    public LoginState(int currentState) {
        super(currentState);
    }
    
    public LoginState(int currentState, String message) {
        super(currentState, message);
    }
    
    public boolean isLoginSuccess() {
        return currentState == STATE_LOGIN_SUCCESS;
    }
}

