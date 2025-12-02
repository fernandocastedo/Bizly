package com.bizly.app.data.local.entity;

/**
 * Clase de resultado para consultas de top vendedores
 * Usada por Room para mapear resultados de consultas SQL
 */
public class TopVendedorResult {
    public int usuarioId;
    public int totalVentas;
    public double totalMonto;
    
    public TopVendedorResult() {
    }
    
    public TopVendedorResult(int usuarioId, int totalVentas, double totalMonto) {
        this.usuarioId = usuarioId;
        this.totalVentas = totalVentas;
        this.totalMonto = totalMonto;
    }
}

