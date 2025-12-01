# Bizly - Aplicación de Gestión para Emprendedores

## 📱 Descripción

Bizly es una aplicación móvil Android diseñada para ayudar a emprendedores a gestionar su negocio de manera integral. La aplicación incluye módulos para inventario inteligente (con escaneo de facturas), ventas, costos, reportes financieros, trabajadores y más.

## 🏗️ Arquitectura

El proyecto sigue **Clean Architecture** con las siguientes capas:

- **Presentation**: UI, Activities, ViewModels, Components
- **Domain**: Modelos de negocio, Casos de uso, Servicios
- **Data**: Repositorios, API, Base de datos local, DataSources
- **Core**: Utilidades, Network, Database, Exceptions, Mappers

## 📂 Estructura del Proyecto

```
com.bizly.app/
├── presentation/    # Capa de presentación
│   ├── auth/       # Autenticación (RF-01, RF-02, RF-49)
│   ├── emprendimiento/  # Configuración (RF-03-RF-07)
│   ├── inventario/ # Inventario con ML Kit (RF-08-RF-14)
│   ├── productos/  # Productos de venta (RF-15-RF-21)
│   ├── ventas/     # Ventas y pedidos (RF-22-RF-31)
│   ├── costos/     # Costos y gastos (RF-32-RF-37)
│   ├── reportes/   # Reportes y métricas (RF-38-RF-45)
│   ├── trabajadores/ # Trabajadores (RF-46-RF-52)
│   └── base/       # Clases base
├── domain/         # Capa de dominio
│   ├── model/     # Modelos de negocio
│   ├── usecase/   # Casos de uso
│   └── service/   # Servicios de negocio
├── data/          # Capa de datos
│   ├── repository/ # Repositorios
│   ├── api/       # Interfaces API
│   ├── local/     # Base de datos local (Room)
│   ├── datasource/ # DataSources
│   └── dto/       # DTOs
└── core/          # Utilidades compartidas
    ├── util/      # Utilidades
    ├── network/   # Configuración de red
    ├── database/  # Configuración de BD
    ├── exception/ # Excepciones
    └── mapper/   # Mappers
```

## 📋 Requerimientos Funcionales

El proyecto implementa **52 Requerimientos Funcionales (RF-01 a RF-52)** organizados en 8 módulos:

1. **Módulo 1**: Autenticación (RF-01, RF-02, RF-49)
2. **Módulo 2**: Configuración del Emprendimiento (RF-03 a RF-07)
3. **Módulo 3**: Inventario Inteligente (RF-08 a RF-14)
4. **Módulo 4**: Productos de Venta (RF-15 a RF-21)
5. **Módulo 5**: Ventas (RF-22 a RF-31)
6. **Módulo 6**: Costos y Gastos (RF-32 a RF-37)
7. **Módulo 7**: Reportes y Métricas (RF-38 a RF-45)
8. **Módulo 8**: Trabajadores (RF-46 a RF-52)

Para más detalles, consulta **PROJECT_GUIDE.md**.

## 🚀 Estado del Proyecto

### ✅ Completado

- [x] Estructura de carpetas creada
- [x] Clases base (BaseActivity, BaseViewModel, BaseState)
- [x] Sistema de excepciones
- [x] Modelos de dominio básicos (Empresa, Usuario, Insumo)
- [x] Interfaces de repositorios base
- [x] Guía del proyecto (PROJECT_GUIDE.md)

### ⏳ Pendiente

- [ ] Configurar dependencias (Room, Retrofit, ML Kit)
- [ ] Crear todos los modelos de dominio
- [ ] Implementar Room Database
- [ ] Configurar Retrofit para API
- [ ] Implementar módulo de autenticación
- [ ] Implementar módulo de inventario con ML Kit
- [ ] Implementar módulo de ventas
- [ ] Implementar módulo de reportes
- [ ] Testing

## 🔧 Tecnologías

- **Android SDK**: API 24+
- **Room**: Base de datos local
- **Retrofit**: Comunicación con API REST
- **ML Kit**: Escaneo de facturas (RF-09, RF-33)
- **Material Design**: UI moderna
- **ViewModel & LiveData**: Arquitectura MVVM
- **Navigation Component**: Navegación entre pantallas

## 📖 Documentación

- **PROJECT_GUIDE.md**: Guía completa del proyecto con todos los detalles
- **Diagrama BD Bizly.txt**: Esquema de base de datos
- **Lista de Requerimientos Final.txt**: Todos los requerimientos funcionales

## 👥 Desarrollo

Para comenzar a desarrollar:

1. Revisa **PROJECT_GUIDE.md** para entender la estructura completa
2. Consulta los requerimientos funcionales para entender cada módulo
3. Sigue la arquitectura Clean Architecture establecida
4. Implementa módulo por módulo según prioridad

## ❓ Preguntas

Si tienes dudas sobre:
- Atributos de las tablas
- Lógica de negocio
- Flujos de usuario
- Integraciones

Consulta con el equipo antes de implementar.

