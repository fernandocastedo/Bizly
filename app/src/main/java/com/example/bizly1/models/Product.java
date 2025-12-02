package com.example.bizly1.models;

public class Product {
    private String name;
    private double quantity;
    private String unit;
    private double unitPrice;
    private double totalPrice;
    private String categoria;

    public Product(String name, double quantity, String unit, double unitPrice, double totalPrice, String categoria) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.categoria = categoria != null ? categoria : "General";
    }

    public String getName() { 
        return name; 
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getQuantity() { 
        return quantity; 
    }
    
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    
    public String getUnit() { 
        return unit; 
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public double getUnitPrice() { 
        return unitPrice; 
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public double getTotalPrice() { 
        return totalPrice; 
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getCategoria() { 
        return categoria; 
    }
    
    public void setCategoria(String categoria) { 
        this.categoria = categoria != null ? categoria : "General"; 
    }
    
    // Método para convertir Product a Insumo
    public Insumo toInsumo(Integer empresaId) {
        Insumo insumo = new Insumo();
        insumo.setNombre(this.name);
        insumo.setCantidad(this.quantity);
        insumo.setUnidadMedida(this.unit);
        insumo.setPrecioUnitario(this.unitPrice);
        insumo.setPrecioTotal(this.totalPrice);
        insumo.setCategoria(this.categoria);
        insumo.setEmpresaId(empresaId);
        insumo.setSyncStatus("pending");
        return insumo;
    }
}

