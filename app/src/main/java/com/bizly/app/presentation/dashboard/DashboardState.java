package com.bizly.app.presentation.dashboard;

import com.bizly.app.presentation.base.BaseState;

/**
 * Estado específico para la pantalla de dashboard
 */
public class DashboardState extends BaseState {
    
    public static final int STATE_DATA_LOADED = 10;
    
    public DashboardState(int currentState) {
        super(currentState);
    }
    
    public DashboardState(int currentState, String message) {
        super(currentState, message);
    }
    
    public boolean isDataLoaded() {
        return currentState == STATE_DATA_LOADED;
    }
}

