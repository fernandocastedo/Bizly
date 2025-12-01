# Guía del Proyecto Bizly

## 📋 Descripción General

Bizly es una aplicación móvil Android diseñada para ayudar a emprendedores a gestionar su negocio de manera integral. La aplicación incluye módulos para inventario, ventas, costos, reportes, trabajadores y más.

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una arquitectura **Clean Architecture** con separación en capas:

```
com.bizly.app/
├── presentation/     # Capa de presentación (UI, ViewModels, Activities)
├── domain/          # Capa de dominio (Modelos, Casos de uso, Servicios)
├── data/            # Capa de datos (Repositorios, API, Base de datos local)
└── core/            # Utilidades y componentes compartidos
```

## 📁 Estructura de Carpetas

### 🎨 Presentation Layer

#### Módulos de Presentación:

1. **auth/** - Autenticación y registro de usuarios
   - LoginActivity / LoginViewModel
   - RegisterActivity / RegisterViewModel
   - Implementa: RF-01, RF-02, RF-49

2. **emprendimiento/** - Configuración del emprendimiento
   - EmprendimientoActivity / EmprendimientoViewModel
   - ConfiguracionActivity
   - Implementa: RF-03, RF-04, RF-05, RF-06, RF-07

3. **inventario/** - Gestión de inventario con ML Kit
   - InventarioActivity / InventarioViewModel
   - EscanearFacturaActivity
   - EditarInsumoActivity
   - Implementa: RF-08, RF-09, RF-10, RF-11, RF-12, RF-13, RF-14

4. **productos/** - Productos de venta
   - ProductosVentaActivity / ProductosVentaViewModel
   - CrearProductoActivity
   - EditarProductoActivity
   - Implementa: RF-15, RF-16, RF-17, RF-18, RF-19, RF-20, RF-21

5. **ventas/** - Registro y gestión de ventas
   - VentasActivity / VentasViewModel
   - RegistrarVentaActivity
   - HistorialVentasActivity
   - PedidosPendientesActivity
   - Implementa: RF-22, RF-23, RF-24, RF-25, RF-26, RF-27, RF-28, RF-29, RF-30, RF-31

6. **costos/** - Gestión de costos y gastos
   - CostosGastosActivity / CostosGastosViewModel
   - RegistrarCostoActivity
   - Implementa: RF-32, RF-33, RF-34, RF-35, RF-36, RF-37

7. **reportes/** - Reportes y métricas
   - ReportesActivity / ReportesViewModel
   - GraficasActivity
   - Implementa: RF-38, RF-39, RF-40, RF-41, RF-42, RF-43, RF-44, RF-45

8. **trabajadores/** - Gestión de trabajadores
   - TrabajadoresActivity / TrabajadoresViewModel
   - RegistrarTrabajadorActivity
   - CrearUsuarioTrabajadorActivity
   - Implementa: RF-46, RF-47, RF-48, RF-50, RF-51, RF-52

9. **ubicaciones/** - Gestión de sucursales
   - SucursalesActivity / SucursalesViewModel
   - RegistrarSucursalActivity

10. **notificaciones/** - Sistema de notificaciones
    - NotificacionesActivity
    - Implementa alertas de stock bajo (RF-14), alertas de costos (RF-37)

11. **onboarding/** - Flujo de bienvenida
    - OnboardingActivity
    - Guía inicial para nuevos usuarios

12. **dashboard/** - Panel principal
    - DashboardActivity / DashboardViewModel
    - Vista resumen del negocio

13. **components/** - Componentes reutilizables
    - Adapters (RecyclerView, Spinner, etc.)
    - Dialogs personalizados
    - ViewHolders
    - Custom Views

14. **base/** - Clases base
    - BaseActivity
    - BaseViewModel
    - BaseState

### 🎯 Domain Layer

#### Modelos de Dominio (domain/model/):

Basados en el diagrama de BD, crear los siguientes modelos:

1. **Empresa.java** - Información del emprendimiento
   - id, nombre, rubro, descripcion, margen_ganancia, logo_url, created_at, updated_at

2. **Sucursal.java** - Sucursales del negocio
   - id, empresa_id, nombre, direccion, ciudad, latitud, longitud, departamento, telefono

3. **Usuario.java** - Usuarios del sistema
   - id, empresa_id, sucursal_id, trabajador_id, nombre, email, password, tipo_usuario, activo

4. **Trabajador.java** - Trabajadores
   - id, empresa_id, sucursal_id, nombre, cargo, sueldo_mensual, tipo_gasto

5. **Categoria.java** - Categorías de productos
   - id, empresa_id, nombre, descripcion

6. **Insumo.java** - Productos del inventario
   - id, empresa_id, sucursal_id, categoria_id, nombre, descripcion, cantidad, unidad_medida, precio_unitario, precio_total, stock_minimo, activo

7. **RegistroInventario.java** - Historial de movimientos de inventario
   - id, empresa_id, sucursal_id, usuario_id, insumo_id, tipo_movimiento, cantidad_anterior, cantidad_nueva, motivo

8. **ProductoVenta.java** - Productos terminados para venta
   - id, empresa_id, sucursal_id, categoria_id, nombre, descripcion, precio_venta, activo

9. **InsumoProductoVenta.java** - Relación insumo-producto
   - id, producto_venta_id, insumo_id, cantidad_utilizada

10. **Cliente.java** - Clientes
    - id, empresa_id, sucursal_id, nombre, nit, telefono, email, direccion

11. **Venta.java** - Ventas registradas
    - id, empresa_id, sucursal_id, usuario_id, cliente_id, fecha, metodo_pago, total, es_envio, estado_pago, estado_pedido

12. **DetalleVenta.java** - Detalle de productos vendidos
    - id, venta_id, producto_venta_id, cantidad, precio_unitario, subtotal

13. **CostoGasto.java** - Costos y gastos
    - id, empresa_id, sucursal_id, usuario_id, categoria_financiera, descripcion, monto, fecha, clasificacion, insumo_id, trabajador_id

#### Casos de Uso (domain/usecase/):

1. **RegistrarUsuarioUseCase.java** - RF-01
2. **IniciarSesionUseCase.java** - RF-02
3. **RegistrarEmprendimientoUseCase.java** - RF-03
4. **SubirLogotipoUseCase.java** - RF-04
5. **RegistrarInsumoUseCase.java** - RF-08
6. **EscanearFacturaUseCase.java** - RF-09 (ML Kit)
7. **ActualizarStockUseCase.java** - RF-12
8. **CrearProductoVentaUseCase.java** - RF-15
9. **CalcularPrecioSugeridoUseCase.java** - RF-17
10. **RegistrarVentaUseCase.java** - RF-22
11. **ValidarStockUseCase.java** - RF-23
12. **RegistrarCostoGastoUseCase.java** - RF-32
13. **RegistrarTrabajadorUseCase.java** - RF-46
14. **CrearUsuarioTrabajadorUseCase.java** - RF-48
15. **GenerarReporteVentasUseCase.java** - RF-38
16. **CalcularPuntoEquilibrioUseCase.java** - RF-42
17. **CalcularMargenGananciaUseCase.java** - RF-41

#### Servicios (domain/service/):

1. **CalculoPrecioService.java** - Lógica de cálculo de precios (RF-17)
2. **ValidacionStockService.java** - Validaciones de stock (RF-23)
3. **DescuentoInventarioService.java** - Descuento automático (RF-21)
4. **ReporteService.java** - Generación de reportes
5. **MLKitService.java** - Servicio para escaneo de facturas (RF-09, RF-33)
6. **NotificacionService.java** - Alertas y notificaciones (RF-14, RF-37)

### 💾 Data Layer

#### Repositorios (data/repository/):

1. **UsuarioRepository.java** - Operaciones de usuarios
2. **EmpresaRepository.java** - Operaciones de empresas
3. **InventarioRepository.java** - Operaciones de inventario
4. **ProductoVentaRepository.java** - Operaciones de productos
5. **VentaRepository.java** - Operaciones de ventas
6. **CostoGastoRepository.java** - Operaciones de costos
7. **TrabajadorRepository.java** - Operaciones de trabajadores
8. **ReporteRepository.java** - Operaciones de reportes

#### API (data/api/):

1. **ApiService.java** - Interfaz Retrofit principal
2. **AuthApi.java** - Endpoints de autenticación
3. **EmpresaApi.java** - Endpoints de empresas
4. **InventarioApi.java** - Endpoints de inventario
5. **VentaApi.java** - Endpoints de ventas
6. **ReporteApi.java** - Endpoints de reportes

#### Base de Datos Local (data/local/):

1. **AppDatabase.java** - Room Database principal
2. **DAOs:**
   - UsuarioDao.java
   - EmpresaDao.java
   - InsumoDao.java
   - ProductoVentaDao.java
   - VentaDao.java
   - CostoGastoDao.java
   - TrabajadorDao.java
   - ClienteDao.java
   - SucursalDao.java
   - CategoriaDao.java

#### DataSources (data/datasource/):

1. **LocalDataSource.java** - Fuente de datos local
2. **RemoteDataSource.java** - Fuente de datos remota
3. **InventarioLocalDataSource.java**
4. **VentaLocalDataSource.java**

#### DTOs (data/dto/):

Objetos de transferencia de datos para comunicación con API:
- UsuarioDTO.java
- EmpresaDTO.java
- InsumoDTO.java
- VentaDTO.java
- etc.

### 🔧 Core Layer

#### Utilidades (core/util/):

1. **DateUtils.java** - Utilidades de fechas
2. **CurrencyUtils.java** - Formateo de moneda
3. **ValidationUtils.java** - Validaciones comunes
4. **ImageUtils.java** - Manejo de imágenes
5. **FileUtils.java** - Manejo de archivos

#### Network (core/network/):

1. **ApiClient.java** - Cliente Retrofit
2. **NetworkInterceptor.java** - Interceptor de red
3. **ApiResponse.java** - Respuesta genérica de API

#### Database (core/database/):

1. **DatabaseModule.java** - Configuración de Room
2. **TypeConverters.java** - Convertidores de tipos

#### Exception (core/exception/):

1. **AppException.java** - Excepción base
2. **NetworkException.java** - Errores de red
3. **DatabaseException.java** - Errores de BD

#### Mapper (core/mapper/):

1. **EntityMapper.java** - Mapeo entre entidades y modelos
2. **DTOMapper.java** - Mapeo entre DTOs y modelos
3. **InsumoMapper.java**
4. **VentaMapper.java**
5. etc.

## 📱 Módulos y Requerimientos Funcionales

### Módulo 1: Autenticación (RF-01, RF-02, RF-49)
- ✅ Registro de emprendedor
- ✅ Inicio de sesión
- ✅ Inicio de sesión de trabajador

### Módulo 2: Configuración del Emprendimiento (RF-03 a RF-07)
- ✅ Registro de emprendimiento
- ✅ Subida de logotipo
- ✅ Edición de datos
- ✅ Selección de margen de ganancia
- ✅ Visualización del perfil

### Módulo 3: Inventario Inteligente (RF-08 a RF-14)
- ✅ Registro manual de insumos
- ✅ Registro mediante cámara (ML Kit)
- ✅ Edición de datos tras escaneo
- ✅ Visualización del inventario
- ✅ Actualización de stock
- ✅ Eliminación de productos
- ✅ Alertas de stock bajo

### Módulo 4: Productos de Venta (RF-15 a RF-21)
- ✅ Creación de productos
- ✅ Asociación de insumos
- ✅ Cálculo de precio sugerido
- ✅ Edición de productos
- ✅ Deshabilitación de productos
- ✅ Visualización de productos
- ✅ Descuento automático del inventario

### Módulo 5: Ventas (RF-22 a RF-31)
- ✅ Registro de venta
- ✅ Validación de disponibilidad
- ✅ Cálculo automático del total
- ✅ Registro del vendedor
- ✅ Historial de ventas
- ✅ Top de vendedores
- ✅ Cancelación de ventas
- ✅ Ventas con envío
- ✅ Pedidos pendientes
- ✅ Actualización de estado

### Módulo 6: Costos y Gastos (RF-32 a RF-37)
- ✅ Registro de costos y gastos
- ✅ Registro mediante cámara
- ✅ Clasificación fijo/variable
- ✅ Edición y eliminación
- ✅ Visualización de costos
- ✅ Alerta de incremento

### Módulo 7: Reportes y Métricas (RF-38 a RF-45)
- ✅ Reportes de ventas
- ✅ Reporte de costos
- ✅ Top clientes y productos
- ✅ Cálculo de margen de ganancia
- ✅ Cálculo de punto de equilibrio
- ✅ Meta mensual de ventas
- ✅ Comparativa mensual
- ✅ Exportación de reportes

### Módulo 8: Trabajadores (RF-46 a RF-52)
- ✅ Registro de trabajadores
- ✅ Edición y eliminación
- ✅ Creación de usuario vinculado
- ✅ Inicio de sesión trabajador
- ✅ Registro de ventas por trabajador
- ✅ Reporte de desempeño
- ✅ Desactivación de usuario

## 🗄️ Base de Datos

### Tablas Principales:

1. **empresas** - Información del emprendimiento
2. **sucursales** - Sucursales del negocio
3. **usuarios** - Usuarios del sistema
4. **trabajadores** - Trabajadores
5. **categorias** - Categorías de productos
6. **insumos** - Inventario de insumos
7. **registros_inventario** - Historial de movimientos
8. **productos_venta** - Productos terminados
9. **insumo_producto_venta** - Relación insumo-producto
10. **clientes** - Clientes
11. **ventas** - Ventas registradas
12. **detalle_ventas** - Detalle de ventas
13. **costos_gastos** - Costos y gastos

## 🔌 Integraciones

### ML Kit (Google)
- **RF-09**: Escaneo de facturas para registro de insumos
- **RF-33**: Escaneo de facturas para registro de costos
- Funcionalidad: Reconocimiento de texto en imágenes de facturas

### Room Database
- Base de datos local para almacenamiento offline
- Sincronización con servidor cuando haya conexión

### Retrofit
- Comunicación con API REST
- Manejo de autenticación y tokens

## 📦 Dependencias Necesarias

```gradle
// Room
implementation "androidx.room:room-runtime:2.6.1"
kapt "androidx.room:room-compiler:2.6.1"

// Retrofit
implementation "com.squareup.retrofit2:retrofit:2.9.0"
implementation "com.squareup.retrofit2:converter-gson:2.9.0"

// ML Kit
implementation "com.google.mlkit:text-recognition:16.0.0"

// ViewModel & LiveData
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0"
implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.7.0"

// Navigation
implementation "androidx.navigation:navigation-fragment:2.7.6"
implementation "androidx.navigation:navigation-ui:2.7.6"

// Material Design
implementation "com.google.android.material:material:1.13.0"

// Coroutines
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
```

## 🚀 Próximos Pasos

1. ✅ Estructura de carpetas creada
2. ⏳ Configurar dependencias en build.gradle
3. ⏳ Crear modelos de dominio
4. ⏳ Configurar Room Database
5. ⏳ Configurar Retrofit
6. ⏳ Implementar módulo de autenticación
7. ⏳ Implementar módulo de inventario
8. ⏳ Implementar ML Kit para escaneo
9. ⏳ Implementar módulo de ventas
10. ⏳ Implementar módulo de reportes
11. ⏳ Testing y optimización

## 📝 Notas Importantes

- El proyecto usa **Clean Architecture** para mantener el código organizado y testeable
- Se implementará **offline-first** con Room Database
- ML Kit se usará para escaneo de facturas (RF-09, RF-33)
- Los cálculos financieros (margen, punto de equilibrio) se harán en la capa de dominio
- Las notificaciones se implementarán para alertas de stock y costos

## ❓ Preguntas Pendientes

Si tienes dudas sobre:
- Atributos específicos de las tablas
- Lógica de negocio particular
- Flujos de usuario
- Integraciones adicionales

Por favor, pregunta para aclarar antes de implementar.

