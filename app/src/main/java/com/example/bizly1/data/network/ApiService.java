package com.example.bizly1.data.network;

import com.example.bizly1.models.Insumo;
import com.example.bizly1.models.ProductoVenta;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    
    String BASE_URL = "http://tu-api-url.com/"; // TODO: Cambiar por la URL real de tu API
    
    // ==================== ENDPOINTS INSUMOS ====================
    
    @GET("api/Insumos")
    Call<List<Insumo>> obtenerTodosInsumos();
    
    @GET("api/Insumos/{id}")
    Call<Insumo> obtenerInsumo(@Path("id") int id);
    
    @POST("api/Insumos")
    Call<Insumo> crearInsumo(@Body Insumo insumo);
    
    @PUT("api/Insumos/{id}")
    Call<Insumo> actualizarInsumo(@Path("id") int id, @Body Insumo insumo);
    
    @DELETE("api/Insumos/{id}")
    Call<Void> eliminarInsumo(@Path("id") int id);
    
    // ==================== ENDPOINTS PRODUCTOS VENTA ====================
    
    @GET("api/ProductosVenta")
    Call<List<ProductoVenta>> obtenerTodosProductosVenta();
    
    @GET("api/ProductosVenta/{id}")
    Call<ProductoVenta> obtenerProductoVenta(@Path("id") int id);
    
    @POST("api/ProductosVenta")
    Call<ProductoVenta> crearProductoVenta(@Body ProductoVenta productoVenta);
    
    @PUT("api/ProductosVenta/{id}")
    Call<ProductoVenta> actualizarProductoVenta(@Path("id") int id, @Body ProductoVenta productoVenta);
    
    @DELETE("api/ProductosVenta/{id}")
    Call<Void> eliminarProductoVenta(@Path("id") int id);
}

