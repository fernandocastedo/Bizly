package com.example.bizly1.data.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager {
    private static final String PREFS_NAME = "BizlyAuthPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_ROL = "user_rol";
    private static final String KEY_EMPRESA_ID = "empresa_id";
    private static final String KEY_EMPRESA_CONFIGURADA = "empresa_configurada";
    
    private SharedPreferences prefs;
    private static AuthManager instance;
    
    private AuthManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public static synchronized AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context.getApplicationContext());
        }
        return instance;
    }
    
    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }
    
    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }
    
    public void saveUserInfo(Integer userId, String userName, String userEmail, String userRol, Integer empresaId) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_EMAIL, userEmail);
        editor.putString(KEY_USER_ROL, userRol);
        if (empresaId != null) {
            editor.putInt(KEY_EMPRESA_ID, empresaId);
            editor.putBoolean(KEY_EMPRESA_CONFIGURADA, true);
        }
        editor.apply();
    }
    
    public Integer getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }
    
    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }
    
    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }
    
    public String getUserRol() {
        return prefs.getString(KEY_USER_ROL, null);
    }
    
    public Integer getEmpresaId() {
        return prefs.getInt(KEY_EMPRESA_ID, -1);
    }
    
    public boolean isEmpresaConfigurada() {
        return prefs.getBoolean(KEY_EMPRESA_CONFIGURADA, false);
    }
    
    public void setEmpresaConfigurada(boolean configurada) {
        prefs.edit().putBoolean(KEY_EMPRESA_CONFIGURADA, configurada).apply();
    }
    
    public boolean isLoggedIn() {
        return getToken() != null && !getToken().isEmpty();
    }
    
    public void logout() {
        prefs.edit().clear().apply();
    }
}

