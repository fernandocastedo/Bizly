package com.example.bizly1.models;

import com.google.gson.annotations.SerializedName;

public class Usuario {
    @SerializedName("id")
    private Integer id;
    
    @SerializedName("nombre")
    private String nombre;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("rol")
    private String rol;
    
    @SerializedName("empresa_id")
    private Integer empresaId;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRol() {
        return rol;
    }
    
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public Integer getEmpresaId() {
        return empresaId;
    }
    
    public void setEmpresaId(Integer empresaId) {
        this.empresaId = empresaId;
    }
}

