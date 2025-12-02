package com.bizly.app.presentation.auth;

import com.bizly.app.presentation.base.BaseState;

/**
 * Estado específico para la pantalla de registro
 */
public class RegisterState extends BaseState {
    
    public static final int STATE_REGISTER_SUCCESS = 10;
    
    public RegisterState(int currentState) {
        super(currentState);
    }
    
    public RegisterState(int currentState, String message) {
        super(currentState, message);
    }
    
    public boolean isRegisterSuccess() {
        return currentState == STATE_REGISTER_SUCCESS;
    }
}

