package com.bizly.app.data.repository;

import com.bizly.app.domain.model.Insumo;
import java.util.List;

/**
 * Interfaz del repositorio de inventario
 * Define las operaciones de acceso a datos para insumos
 * RF-08, RF-09, RF-11, RF-12, RF-13, RF-14
 */
public interface InventarioRepository {
    
    /**
     * Registra un nuevo insumo manualmente (RF-08)
     */
    Insumo registrarInsumo(Insumo insumo);
    
    /**
     * Registra insumos desde factura escaneada (RF-09)
     */
    List<Insumo> registrarInsumosDesdeFactura(List<Insumo> insumos);
    
    /**
     * Obtiene todos los insumos de una empresa/sucursal (RF-11)
     */
    List<Insumo> obtenerInsumos(int empresaId, int sucursalId);
    
    /**
     * Obtiene insumos con filtros (RF-11)
     */
    List<Insumo> obtenerInsumosFiltrados(int empresaId, int sucursalId, String nombre, Integer categoriaId);
    
    /**
     * Actualiza el stock de un insumo (RF-12)
     */
    boolean actualizarStock(int insumoId, double nuevaCantidad, String motivo);
    
    /**
     * Elimina (desactiva) un insumo (RF-13)
     */
    boolean eliminarInsumo(int insumoId);
    
    /**
     * Obtiene insumos con stock bajo (RF-14)
     */
    List<Insumo> obtenerInsumosStockBajo(int empresaId, int sucursalId);
    
    /**
     * Obtiene un insumo por ID
     */
    Insumo obtenerInsumoPorId(int id);
    
    /**
     * Busca insumos por nombre (para coincidencias en escaneo)
     */
    Insumo buscarInsumoPorNombre(int empresaId, int sucursalId, String nombre);
}

