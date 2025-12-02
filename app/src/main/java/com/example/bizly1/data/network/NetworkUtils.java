package com.example.bizly1.data.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkUtils {
    
    /**
     * Verifica si hay conexión a internet disponible
     * @param context Contexto de la aplicación
     * @return true si hay conexión, false en caso contrario
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager == null) {
            return false;
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Para Android 6.0 (API 23) y superior
            android.net.Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = 
                connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            );
        } else {
            // Para versiones anteriores a Android 6.0
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
    }
    
    /**
     * Verifica si hay conexión WiFi
     * @param context Contexto de la aplicación
     * @return true si está conectado por WiFi
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager == null) {
            return false;
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.net.Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = 
                connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && 
                   capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        } else {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && 
                   activeNetwork.getType() == ConnectivityManager.TYPE_WIFI &&
                   activeNetwork.isConnected();
        }
    }
}

