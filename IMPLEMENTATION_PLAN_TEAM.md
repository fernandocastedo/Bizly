# Plan de Implementación - Bizly (Trabajo en Equipo - 3 Personas)

## 👥 División del Trabajo

Este plan está diseñado para que **3 personas trabajen en paralelo** minimizando conflictos y dependencias.

---

## 📋 PREREQUISITOS COMPARTIDOS (Ya Completados)

### ✅ Fase 1: Base de Datos - COMPLETADA
- ✅ Modelos de dominio
- ✅ Entidades Room
- ✅ TypeConverters
- ✅ AppDatabase
- ✅ DAOs (todos)
- ✅ Dependencias configuradas

### ✅ Fase 2: Repositorios Base - COMPLETADA
- ✅ Mappers: Usuario, Insumo, Venta, Empresa, Cliente
- ✅ Repositorios: UsuarioRepositoryLocal, InventarioRepositoryLocal, VentaRepositoryLocal

---

## 🟢 GRUPO 1: Autenticación, Configuración y Administración

**Responsable**: Persona 1  
**Rama Git**: `feature/grupo1-auth-config`  
**Módulos**: Autenticación, Configuración, Trabajadores, Sucursales

### 📦 Tareas del Grupo 1

#### 1. Completar Repositorios (Fase 2)
- [ ] Crear `EmpresaMapper`
- [ ] Crear `SucursalMapper`
- [ ] Crear `TrabajadorMapper`
- [ ] Crear `CategoriaMapper`
- [ ] Implementar `EmpresaRepositoryLocal`
- [ ] Implementar `SucursalRepositoryLocal`
- [ ] Implementar `TrabajadorRepositoryLocal`
- [ ] Implementar `CategoriaRepositoryLocal`

#### 2. Casos de Uso (Fase 3)
- [ ] `RegistrarUsuarioUseCase` (RF-01)
- [ ] `IniciarSesionUseCase` (RF-02, RF-49)
- [ ] `RegistrarEmprendimientoUseCase` (RF-03)
- [ ] `ActualizarEmprendimientoUseCase` (RF-05)
- [ ] `RegistrarTrabajadorUseCase` (RF-46)
- [ ] `CrearUsuarioTrabajadorUseCase` (RF-48)
- [ ] `DesactivarUsuarioUseCase` (RF-52)

#### 3. Servicios de Dominio (Fase 4)
- [ ] `ValidacionEmailService` (validar formato email)
- [ ] `HashPasswordService` (hash de contraseñas)

#### 4. UI - Autenticación (Fase 5)
- [ ] `LoginActivity` + `activity_login.xml`
- [ ] `LoginViewModel` (RF-02, RF-49)
- [ ] `RegisterActivity` + `activity_register.xml`
- [ ] `RegisterViewModel` (RF-01)
- [ ] Navegación entre Login y Register

#### 5. UI - Configuración (Fase 6)
- [ ] `OnboardingActivity` + `activity_onboarding.xml`
- [ ] `EmprendimientoActivity` + `activity_emprendimiento.xml`
- [ ] `EmprendimientoViewModel` (RF-03, RF-04, RF-05, RF-06)
- [ ] `DashboardActivity` + `activity_dashboard.xml`
- [ ] `DashboardViewModel` (RF-07)
- [ ] Selector de imagen para logo (RF-04)

#### 6. UI - Trabajadores (Fase 12)
- [ ] `TrabajadoresActivity` + `activity_trabajadores.xml`
- [ ] `TrabajadoresViewModel` (RF-46, RF-47)
- [ ] `CrearTrabajadorActivity` + `activity_crear_trabajador.xml`
- [ ] `CrearUsuarioTrabajadorActivity` + `activity_crear_usuario_trabajador.xml` (RF-48)
- [ ] `ReporteDesempenoActivity` + `activity_reporte_desempeno.xml` (RF-51)

#### 7. UI - Sucursales (Fase 13)
- [ ] `SucursalesActivity` + `activity_sucursales.xml`
- [ ] `SucursalesViewModel`
- [ ] `RegistrarSucursalActivity` + `activity_registrar_sucursal.xml`

### 📁 Archivos que trabajará Grupo 1
```
data/repository/impl/
  - EmpresaRepositoryLocal.java
  - SucursalRepositoryLocal.java
  - TrabajadorRepositoryLocal.java
  - CategoriaRepositoryLocal.java

core/mapper/
  - EmpresaMapper.java ✅ (ya existe)
  - SucursalMapper.java
  - TrabajadorMapper.java
  - CategoriaMapper.java

domain/usecase/
  - auth/
  - emprendimiento/
  - trabajadores/

domain/service/
  - ValidacionEmailService.java
  - HashPasswordService.java

presentation/auth/
  - LoginActivity.java
  - RegisterActivity.java
  - ViewModels

presentation/emprendimiento/
  - OnboardingActivity.java
  - EmprendimientoActivity.java
  - DashboardActivity.java
  - ViewModels

presentation/trabajadores/
  - TrabajadoresActivity.java
  - CrearTrabajadorActivity.java
  - CrearUsuarioTrabajadorActivity.java
  - ReporteDesempenoActivity.java
  - ViewModels

presentation/ubicaciones/
  - SucursalesActivity.java
  - RegistrarSucursalActivity.java
  - ViewModels
```

---

## 🔵 GRUPO 2: Inventario y Productos

**Responsable**: Persona 2  
**Rama Git**: `feature/grupo2-inventario-productos`  
**Módulos**: Inventario, Productos de Venta

### 📦 Tareas del Grupo 2

#### 1. Completar Repositorios (Fase 2)
- [ ] Crear `ProductoVentaMapper`
- [ ] Crear `InsumoProductoVentaMapper`
- [ ] Crear `RegistroInventarioMapper`
- [ ] Implementar `ProductoVentaRepositoryLocal`
- [ ] Implementar `InsumoProductoVentaRepositoryLocal`
- [ ] Implementar `RegistroInventarioRepositoryLocal`

#### 2. Casos de Uso (Fase 3)
- [ ] `RegistrarInsumoUseCase` (RF-08)
- [ ] `RegistrarInsumosDesdeFacturaUseCase` (RF-09)
- [ ] `ActualizarStockUseCase` (RF-12)
- [ ] `EliminarInsumoUseCase` (RF-13)
- [ ] `ObtenerInsumosStockBajoUseCase` (RF-14)
- [ ] `CrearProductoVentaUseCase` (RF-15, RF-16, RF-17)
- [ ] `EditarProductoVentaUseCase` (RF-18)
- [ ] `DeshabilitarProductoUseCase` (RF-19)

#### 3. Servicios de Dominio (Fase 4)
- [ ] `MLKitService` (RF-09) - Escaneo de facturas
- [ ] `CalculoPrecioService` (RF-17) - Cálculo de precio sugerido
- [ ] `ValidacionStockService` (RF-23) - Validar stock antes de venta
- [ ] `DescuentoInventarioService` (RF-21) - Descontar insumos al vender

#### 4. UI - Inventario (Fase 7)
- [ ] `InventarioActivity` + `activity_inventario.xml`
- [ ] `InventarioViewModel` (RF-11)
- [ ] `RegistrarInsumoActivity` + `activity_registrar_insumo.xml` (RF-08)
- [ ] `EscanearFacturaActivity` + `activity_escanear_factura.xml` (RF-09, RF-10)
- [ ] `EditarInsumoActivity` + `activity_editar_insumo.xml`
- [ ] Componente de alerta de stock bajo (RF-14)
- [ ] Integración con ML Kit para escaneo

#### 5. UI - Productos de Venta (Fase 8)
- [ ] `ProductosVentaActivity` + `activity_productos_venta.xml` (RF-20)
- [ ] `ProductosVentaViewModel`
- [ ] `CrearProductoActivity` + `activity_crear_producto.xml` (RF-15, RF-16, RF-17)
- [ ] `EditarProductoActivity` + `activity_editar_producto.xml` (RF-18)
- [ ] Selector de insumos para asociar (RF-16)
- [ ] Vista de precio sugerido calculado (RF-17)

### 📁 Archivos que trabajará Grupo 2
```
data/repository/impl/
  - ProductoVentaRepositoryLocal.java
  - InsumoProductoVentaRepositoryLocal.java
  - RegistroInventarioRepositoryLocal.java

core/mapper/
  - ProductoVentaMapper.java
  - InsumoProductoVentaMapper.java
  - RegistroInventarioMapper.java

domain/usecase/
  - inventario/
  - productos/

domain/service/
  - MLKitService.java
  - CalculoPrecioService.java
  - ValidacionStockService.java
  - DescuentoInventarioService.java

presentation/inventario/
  - InventarioActivity.java
  - RegistrarInsumoActivity.java
  - EscanearFacturaActivity.java
  - EditarInsumoActivity.java
  - ViewModels

presentation/productos/
  - ProductosVentaActivity.java
  - CrearProductoActivity.java
  - EditarProductoActivity.java
  - ViewModels
```

---

## 🟡 GRUPO 3: Ventas, Costos y Reportes

**Responsable**: Persona 3  
**Rama Git**: `feature/grupo3-ventas-costos-reportes`  
**Módulos**: Ventas, Costos y Gastos, Reportes

### 📦 Tareas del Grupo 3

#### 1. Completar Repositorios (Fase 2)
- [ ] Crear `ClienteMapper` ✅ (ya existe)
- [ ] Crear `DetalleVentaMapper`
- [ ] Crear `CostoGastoMapper`
- [ ] Implementar `ClienteRepositoryLocal`
- [ ] Implementar `DetalleVentaRepositoryLocal`
- [ ] Implementar `CostoGastoRepositoryLocal`

#### 2. Casos de Uso (Fase 3)
- [ ] `RegistrarVentaUseCase` (RF-22, RF-24)
- [ ] `ValidarStockVentaUseCase` (RF-23)
- [ ] `ObtenerHistorialVentasUseCase` (RF-26)
- [ ] `ObtenerTopVendedoresUseCase` (RF-27)
- [ ] `CancelarVentaUseCase` (RF-28)
- [ ] `RegistrarVentaConEnvioUseCase` (RF-29)
- [ ] `ObtenerPedidosPendientesUseCase` (RF-30)
- [ ] `ActualizarEstadoPedidoUseCase` (RF-31)
- [ ] `RegistrarCostoGastoUseCase` (RF-32, RF-33, RF-34)
- [ ] `ObtenerCostosGastosUseCase` (RF-36)
- [ ] `CalcularMargenGananciaUseCase` (RF-41)
- [ ] `CalcularPuntoEquilibrioUseCase` (RF-42)
- [ ] `ObtenerMetaMensualUseCase` (RF-43)

#### 3. Servicios de Dominio (Fase 4)
- [ ] `CalculoTotalVentaService` (RF-24)
- [ ] `CalculoMargenGananciaService` (RF-41)
- [ ] `CalculoPuntoEquilibrioService` (RF-42)
- [ ] `GeneracionReportesService` (RF-38, RF-39)
- [ ] `ExportacionPDFService` (RF-45)
- [ ] `AlertaCostosService` (RF-37)

#### 4. UI - Ventas (Fase 9)
- [ ] `VentasActivity` + `activity_ventas.xml`
- [ ] `VentasViewModel`
- [ ] `RegistrarVentaActivity` + `activity_registrar_venta.xml` (RF-22, RF-23, RF-24)
- [ ] `HistorialVentasActivity` + `activity_historial_ventas.xml` (RF-26)
- [ ] `PedidosPendientesActivity` + `activity_pedidos_pendientes.xml` (RF-30, RF-31)
- [ ] `TopVendedoresActivity` + `activity_top_vendedores.xml` (RF-27)
- [ ] Selector de productos para venta
- [ ] Calculadora de totales en tiempo real

#### 5. UI - Costos y Gastos (Fase 10)
- [ ] `CostosGastosActivity` + `activity_costos_gastos.xml` (RF-36)
- [ ] `CostosGastosViewModel`
- [ ] `RegistrarCostoActivity` + `activity_registrar_costo.xml` (RF-32, RF-33, RF-34)
- [ ] Integración con ML Kit para escanear facturas de costos (RF-33)
- [ ] Componente de alerta de incremento de costos (RF-37)

#### 6. UI - Reportes (Fase 11)
- [ ] `ReportesActivity` + `activity_reportes.xml` (RF-38, RF-39)
- [ ] `ReportesViewModel`
- [ ] Gráficas de ventas (RF-38)
- [ ] Gráficas de costos y gastos (RF-39)
- [ ] Vista de top clientes y productos (RF-40)
- [ ] Vista de margen de ganancia (RF-41)
- [ ] Vista de punto de equilibrio (RF-42)
- [ ] Progress bar de meta mensual (RF-43)
- [ ] Comparativa mensual (RF-44)
- [ ] Funcionalidad de exportación PDF (RF-45)

### 📁 Archivos que trabajará Grupo 3
```
data/repository/impl/
  - ClienteRepositoryLocal.java
  - DetalleVentaRepositoryLocal.java
  - CostoGastoRepositoryLocal.java

core/mapper/
  - ClienteMapper.java ✅ (ya existe)
  - DetalleVentaMapper.java
  - CostoGastoMapper.java

domain/usecase/
  - ventas/
  - costos/
  - reportes/

domain/service/
  - CalculoTotalVentaService.java
  - CalculoMargenGananciaService.java
  - CalculoPuntoEquilibrioService.java
  - GeneracionReportesService.java
  - ExportacionPDFService.java
  - AlertaCostosService.java

presentation/ventas/
  - VentasActivity.java
  - RegistrarVentaActivity.java
  - HistorialVentasActivity.java
  - PedidosPendientesActivity.java
  - TopVendedoresActivity.java
  - ViewModels

presentation/costos/
  - CostosGastosActivity.java
  - RegistrarCostoActivity.java
  - ViewModels

presentation/reportes/
  - ReportesActivity.java
  - ViewModels
  - Componentes de gráficas
```

---

## 🔄 Flujo de Trabajo en Git

### 1. Setup Inicial (Una vez)
```bash
# Cada persona crea su rama desde main
git checkout main
git pull origin main
git checkout -b feature/grupo1-auth-config  # Persona 1
git checkout -b feature/grupo2-inventario-productos  # Persona 2
git checkout -b feature/grupo3-ventas-costos-reportes  # Persona 3
```

### 2. Trabajo Diario
```bash
# Cada persona trabaja en su rama
git checkout feature/grupo1-auth-config
# ... hacer cambios ...
git add .
git commit -m "feat: implementar LoginActivity y ViewModel"
git push origin feature/grupo1-auth-config
```

### 3. Integración (Cuando cada grupo termine su fase)
```bash
# Desde main
git checkout main
git pull origin main

# Merge de cada rama
git merge feature/grupo1-auth-config
git merge feature/grupo2-inventario-productos
git merge feature/grupo3-ventas-costos-reportes

# Resolver conflictos si los hay (pocos esperados)
git push origin main
```

---

## ⚠️ Puntos de Atención

### Dependencias entre Grupos

1. **Grupo 1 → Grupo 2 y 3**
   - Autenticación debe estar lista antes de que otros grupos prueben sus módulos
   - Dashboard debe estar disponible para navegación

2. **Grupo 2 → Grupo 3**
   - Productos de Venta deben estar listos antes de registrar ventas
   - Validación de stock (Grupo 2) se usa en Ventas (Grupo 3)

3. **Compartido**
   - Todos usan los mismos modelos de dominio (ya creados)
   - Todos usan la misma base de datos (ya configurada)
   - Navegación entre módulos se coordina al final

### Conflictos Potenciales

**Bajo riesgo de conflictos** porque:
- Cada grupo trabaja en diferentes paquetes/carpetas
- Diferentes archivos XML de layouts
- Diferentes ViewModels y Activities

**Posibles conflictos en**:
- `AndroidManifest.xml` (cada grupo agrega Activities)
- `build.gradle` (si se agregan dependencias)
- `strings.xml` / `colors.xml` (recursos compartidos)

**Solución**: Coordinar cambios en archivos compartidos o hacer merge frecuente.

---

## 📊 Orden de Prioridad por Grupo

### Grupo 1 (Prioridad ALTA)
1. Repositorios → Casos de Uso → Servicios → UI (Autenticación)
2. UI (Configuración)
3. UI (Trabajadores y Sucursales)

### Grupo 2 (Prioridad ALTA)
1. Repositorios → Casos de Uso → Servicios (ML Kit primero)
2. UI (Inventario)
3. UI (Productos de Venta)

### Grupo 3 (Prioridad MEDIA - depende de Grupo 2)
1. Repositorios → Casos de Uso → Servicios
2. UI (Ventas) - requiere Productos de Grupo 2
3. UI (Costos y Reportes)

---

## ✅ Checklist de Integración

Antes de hacer merge a main, cada grupo debe verificar:

- [ ] Todos los tests pasan (si se implementaron)
- [ ] No hay errores de compilación
- [ ] Código sigue las convenciones del proyecto
- [ ] Comentarios Javadoc en métodos públicos
- [ ] RFs implementados documentados en código

---

## 📝 Notas Finales

- **Comunicación**: Coordinar cambios en archivos compartidos (AndroidManifest, build.gradle)
- **Testing**: Cada grupo prueba su módulo antes de merge
- **Documentación**: Actualizar README cuando se agreguen nuevas features
- **Merge frecuente**: Hacer merge a main al menos una vez por semana para evitar divergencias grandes

---

## 🎯 Meta Final

Al completar los 3 grupos, tendremos:
- ✅ Sistema de autenticación completo
- ✅ Gestión de inventario con ML Kit
- ✅ Productos de venta con cálculo automático
- ✅ Sistema de ventas completo
- ✅ Gestión de costos y gastos
- ✅ Reportes y métricas
- ✅ Gestión de trabajadores y sucursales

¡Éxito en el desarrollo! 🚀

