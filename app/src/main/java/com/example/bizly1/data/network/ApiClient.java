package com.example.bizly1.data.network;

import android.content.Context;

import com.example.bizly1.data.utils.AuthManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;
    private static Context appContext = null;
    
    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }
    
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            // Configurar logging interceptor para ver las peticiones
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Interceptor para agregar el token JWT a las peticiones
            Interceptor authInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    
                    // No agregar token a endpoints públicos
                    String url = original.url().toString();
                    if (url.contains("/api/auth/login") || 
                        url.contains("/api/auth/registro-emprendedor") || 
                        url.contains("/api/auth/test")) {
                        return chain.proceed(original);
                    }
                    
                    // Agregar token si existe
                    if (appContext != null) {
                        AuthManager authManager = AuthManager.getInstance(appContext);
                        String token = authManager.getToken();
                        
                        if (token != null && !token.isEmpty()) {
                            Request.Builder requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + token);
                            Request request = requestBuilder.build();
                            return chain.proceed(request);
                        }
                    }
                    
                    return chain.proceed(original);
                }
            };
            
            // Configurar OkHttpClient con timeout, logging y auth interceptor
            OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logging)
                .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();
            
            // Configurar Gson
            Gson gson = new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .create();
            
            retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        }
        return retrofit;
    }
    
    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getRetrofit().create(ApiService.class);
        }
        return apiService;
    }
}

