# Plan de Implementación - Bizly

> ⚠️ **IMPORTANTE**: Este plan está diseñado para trabajo secuencial.  
> Para trabajo en equipo (3 personas), consulta **IMPLEMENTATION_PLAN_TEAM.md**

## 🎯 Orden Lógico de Implementación

### Fase 1: Base de Datos (Fundación) ✅ EMPEZAMOS AQUÍ
**Razón**: La base de datos es la columna vertebral del sistema. Sin ella, no podemos persistir ni recuperar datos.

1. ✅ Completar modelos de dominio (basados en diagrama BD)
2. ✅ Crear entidades Room (mapeo de tablas a entidades)
3. ✅ Crear TypeConverters (para fechas, enums, etc.)
4. ✅ Crear AppDatabase (configuración de Room)
5. ✅ Crear DAOs (Data Access Objects) para cada entidad
6. ✅ Configurar dependencias en build.gradle

### Fase 2: Repositorios (Capa de Datos) ✅ EN PROGRESO
**Razón**: Los repositorios abstraen el acceso a datos y permiten trabajar con la lógica de negocio.

1. ✅ Crear mappers para convertir Entity <-> Model (Usuario, Insumo, Venta, Empresa, Cliente)
2. ✅ Implementar repositorios locales principales (UsuarioRepositoryLocal, InventarioRepositoryLocal, VentaRepositoryLocal)
3. ⏳ Crear repositorios adicionales según necesidad (Empresa, Sucursal, Trabajador, Categoria, Cliente, ProductoVenta, CostoGasto)
4. ⏳ Crear DTOs para comunicación con API (futuro)
5. ⏳ Implementar DataSources (Local y Remote)

### Fase 3: Casos de Uso (Lógica de Negocio)
**Razón**: Encapsulan la lógica de negocio de forma reutilizable.

1. Casos de uso de autenticación (RF-01, RF-02)
2. Casos de uso de inventario (RF-08, RF-09, RF-12)
3. Casos de uso de ventas (RF-22, RF-24)
4. Casos de uso de reportes (RF-38, RF-41, RF-42)

### Fase 4: Servicios de Dominio
**Razón**: Lógica de negocio compleja (cálculos, validaciones).

1. CalculoPrecioService (RF-17)
2. ValidacionStockService (RF-23)
3. DescuentoInventarioService (RF-21)
4. MLKitService (RF-09, RF-33)

### Fase 5: Autenticación (Módulo Base)
**Razón**: Es el punto de entrada al sistema. Sin autenticación, no se puede usar la app.

1. LoginActivity / LoginViewModel
2. RegisterActivity / RegisterViewModel
3. Implementar RF-01, RF-02, RF-49

### Fase 6: Configuración Inicial
**Razón**: El usuario necesita configurar su emprendimiento antes de usar otras funciones.

1. OnboardingActivity
2. EmprendimientoActivity (RF-03, RF-04, RF-05, RF-06)
3. DashboardActivity (RF-07)

### Fase 7: Inventario (Funcionalidad Core)
**Razón**: Es una de las funciones principales. Permite gestionar stock.

1. InventarioActivity / InventarioViewModel (RF-11)
2. RegistrarInsumoActivity (RF-08)
3. EscanearFacturaActivity (RF-09, RF-10) - Con ML Kit
4. EditarInsumoActivity
5. Alertas de stock bajo (RF-14)

### Fase 8: Productos de Venta
**Razón**: Necesarios para poder realizar ventas.

1. ProductosVentaActivity (RF-20)
2. CrearProductoActivity (RF-15, RF-16, RF-17)
3. EditarProductoActivity (RF-18)
4. Asociación de insumos (RF-16)

### Fase 9: Ventas (Funcionalidad Core)
**Razón**: Funcionalidad principal del negocio.

1. VentasActivity / VentasViewModel
2. RegistrarVentaActivity (RF-22, RF-23, RF-24)
3. HistorialVentasActivity (RF-26)
4. PedidosPendientesActivity (RF-30, RF-31)
5. Top vendedores (RF-27)

### Fase 10: Costos y Gastos
**Razón**: Necesario para reportes financieros.

1. CostosGastosActivity (RF-36)
2. RegistrarCostoActivity (RF-32, RF-33, RF-34)
3. Alertas de costos (RF-37)

### Fase 11: Reportes
**Razón**: Análisis y métricas del negocio.

1. ReportesActivity (RF-38, RF-39)
2. Gráficas y visualizaciones
3. Cálculo de métricas (RF-41, RF-42, RF-43)
4. Exportación (RF-45)

### Fase 12: Trabajadores
**Razón**: Gestión de personal.

1. TrabajadoresActivity (RF-46, RF-47)
2. CrearUsuarioTrabajadorActivity (RF-48)
3. Reporte de desempeño (RF-51)

### Fase 13: Sucursales
**Razón**: Gestión de múltiples ubicaciones.

1. SucursalesActivity
2. RegistrarSucursalActivity

## 📝 Notas Importantes

- **Cada fase depende de la anterior**: No podemos hacer repositorios sin DAOs, ni ViewModels sin repositorios.
- **Testing incremental**: Probar cada fase antes de continuar.
- **Refactoring continuo**: Mejorar código según se avanza.

## 🚀 Comenzamos con Fase 1: Base de Datos

