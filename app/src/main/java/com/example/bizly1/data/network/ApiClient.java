package com.example.bizly1.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;
    
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            // Configurar logging interceptor para ver las peticiones
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Configurar OkHttpClient con timeout y logging
            OkHttpClient client = new OkHttpClient.Builder()
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

