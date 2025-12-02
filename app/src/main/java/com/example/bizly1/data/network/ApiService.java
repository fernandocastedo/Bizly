package com.example.bizly1.data.network;

import com.example.bizly1.models.Cliente;
import com.example.bizly1.models.Insumo;
import com.example.bizly1.models.ProductoVenta;
import com.example.bizly1.models.Sucursal;
import com.example.bizly1.models.Venta;

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
    
    // ==================== ENDPOINTS CLIENTES ====================
    
    @GET("api/Clientes")
    Call<List<Cliente>> obtenerTodosClientes();
    
    @GET("api/Clientes/{id}")
    Call<Cliente> obtenerCliente(@Path("id") int id);
    
    @GET("api/Clientes/nit/{nit}")
    Call<Cliente> obtenerClientePorNit(@Path("nit") int nit);
    
    @POST("api/Clientes")
    Call<Cliente> crearCliente(@Body Cliente cliente);
    
    @PUT("api/Clientes/{id}")
    Call<Cliente> actualizarCliente(@Path("id") int id, @Body Cliente cliente);
    
    @DELETE("api/Clientes/{id}")
    Call<Void> eliminarCliente(@Path("id") int id);
    
    // ==================== ENDPOINTS VENTAS ====================
    
    @GET("api/Ventas")
    Call<List<Venta>> obtenerTodasVentas();
    
    @GET("api/Ventas/{id}")
    Call<Venta> obtenerVenta(@Path("id") int id);
    
    @GET("api/Ventas/pendientes")
    Call<List<Venta>> obtenerVentasPendientes();
    
    @POST("api/Ventas")
    Call<Venta> crearVenta(@Body Venta venta);
    
    @PUT("api/Ventas/{id}")
    Call<Venta> actualizarVenta(@Path("id") int id, @Body Venta venta);
    
    @DELETE("api/Ventas/{id}")
    Call<Void> eliminarVenta(@Path("id") int id);

    // ==================== ENDPOINTS SUCURSALES ====================
    @GET("api/Sucursales")
    Call<List<Sucursal>> obtenerTodasSucursales();

    @GET("api/Sucursales/{id}")
    Call<Sucursal> obtenerSucursal(@Path("id") int id);

    @POST("api/Sucursales")
    Call<Sucursal> crearSucursal(@Body Sucursal sucursal);

    @PUT("api/Sucursales/{id}")
    Call<Sucursal> actualizarSucursal(@Path("id") int id, @Body Sucursal sucursal);

    @DELETE("api/Sucursales/{id}")
    Call<Void> eliminarSucursal(@Path("id") int id);
}

