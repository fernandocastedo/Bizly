package com.bizly.app.presentation.emprendimiento;

import com.bizly.app.presentation.base.BaseState;

/**
 * Estado específico para la pantalla de emprendimiento
 */
public class EmprendimientoState extends BaseState {
    
    public static final int STATE_UPDATE_SUCCESS = 10;
    
    public EmprendimientoState(int currentState) {
        super(currentState);
    }
    
    public EmprendimientoState(int currentState, String message) {
        super(currentState, message);
    }
    
    public boolean isUpdateSuccess() {
        return currentState == STATE_UPDATE_SUCCESS;
    }
}

