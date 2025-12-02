# Coordinación de Equipo - Bizly

## 👥 División Rápida

| Grupo | Persona | Rama Git | Módulos Principales |
|-------|---------|----------|---------------------|
| 🟢 **Grupo 1** | Persona 1 | `feature/grupo1-auth-config` | Autenticación, Configuración, Trabajadores, Sucursales |
| 🔵 **Grupo 2** | Persona 2 | `feature/grupo2-inventario-productos` | Inventario, Productos de Venta |
| 🟡 **Grupo 3** | Persona 3 | `feature/grupo3-ventas-costos-reportes` | Ventas, Costos, Reportes |

## 📋 Estado de Implementación

### ✅ Completado (Compartido)
- [x] Base de datos completa (Fase 1)
- [x] Repositorios base: Usuario, Inventario, Venta
- [x] Mappers: Usuario, Insumo, Venta, Empresa, Cliente

### 🟢 Grupo 1 - Estado
- [ ] Repositorios adicionales (Empresa, Sucursal, Trabajador, Categoria)
- [ ] Casos de uso de autenticación y configuración
- [ ] UI de autenticación (Login, Register)
- [ ] UI de configuración (Onboarding, Emprendimiento, Dashboard)
- [ ] UI de trabajadores y sucursales

### 🔵 Grupo 2 - Estado
- [ ] Repositorios adicionales (ProductoVenta, InsumoProductoVenta, RegistroInventario)
- [ ] Casos de uso de inventario y productos
- [ ] Servicios: MLKit, CalculoPrecio, ValidacionStock, DescuentoInventario
- [ ] UI de inventario (con ML Kit)
- [ ] UI de productos de venta

### 🟡 Grupo 3 - Estado
- [ ] Repositorios adicionales (Cliente, DetalleVenta, CostoGasto)
- [ ] Casos de uso de ventas, costos y reportes
- [ ] Servicios: CalculoTotal, CalculoMargen, GeneracionReportes, ExportacionPDF
- [ ] UI de ventas
- [ ] UI de costos y gastos
- [ ] UI de reportes (con gráficas)

## 🔄 Dependencias entre Grupos

```
Grupo 1 (Auth/Config)
    ↓
    ├─→ Grupo 2 (Inventario) - Necesita autenticación para probar
    └─→ Grupo 3 (Ventas) - Necesita autenticación y productos

Grupo 2 (Inventario/Productos)
    ↓
    └─→ Grupo 3 (Ventas) - Necesita productos para vender
```

## 📁 Archivos Compartidos (Coordinación Necesaria)

### ⚠️ Requieren Coordinación
- `AndroidManifest.xml` - Cada grupo agrega Activities
- `build.gradle` - Dependencias (ML Kit, librerías de gráficas, etc.)
- `res/values/strings.xml` - Strings de UI
- `res/values/colors.xml` - Colores
- `res/values/dimens.xml` - Dimensiones
- `res/menu/` - Menús de navegación

### ✅ Sin Conflictos (Trabajo Independiente)
- Cada grupo trabaja en su propio paquete:
  - Grupo 1: `presentation/auth/`, `presentation/emprendimiento/`, etc.
  - Grupo 2: `presentation/inventario/`, `presentation/productos/`
  - Grupo 3: `presentation/ventas/`, `presentation/costos/`, `presentation/reportes/`

## 🚀 Comandos Git Rápidos

### Setup Inicial
```bash
# Cada persona ejecuta:
git checkout main
git pull origin main
git checkout -b feature/grupo[N]-[nombre]
```

### Trabajo Diario
```bash
git add .
git commit -m "feat: [descripción breve]"
git push origin feature/grupo[N]-[nombre]
```

### Integración (Coordinador)
```bash
git checkout main
git merge feature/grupo1-auth-config
git merge feature/grupo2-inventario-productos
git merge feature/grupo3-ventas-costos-reportes
# Resolver conflictos si los hay
git push origin main
```

## 📞 Puntos de Coordinación

### Semanal
- Revisar progreso de cada grupo
- Coordinar cambios en archivos compartidos
- Hacer merge a main si no hay conflictos

### Antes de Merge
- [ ] Verificar que no hay errores de compilación
- [ ] Revisar conflictos en archivos compartidos
- [ ] Actualizar documentación si es necesario

## 🎯 Prioridades por Grupo

### Grupo 1 (ALTA PRIORIDAD)
1. **Autenticación primero** - Otros grupos la necesitan para probar
2. Configuración de emprendimiento
3. Trabajadores y sucursales

### Grupo 2 (ALTA PRIORIDAD)
1. **Inventario primero** - Base para productos
2. Productos de venta - Grupo 3 los necesita

### Grupo 3 (MEDIA PRIORIDAD - Espera Grupo 2)
1. Ventas - Requiere productos de Grupo 2
2. Costos y gastos
3. Reportes

## 📝 Convenciones de Commits

Usar prefijos claros:
- `feat:` - Nueva funcionalidad
- `fix:` - Corrección de bug
- `refactor:` - Refactorización
- `docs:` - Documentación
- `test:` - Tests

Ejemplos:
```
feat: implementar LoginActivity y ViewModel (RF-02)
feat: agregar MLKitService para escaneo de facturas (RF-09)
fix: corregir cálculo de precio sugerido
```

## ✅ Checklist de Integración

Antes de merge a main:
- [ ] Código compila sin errores
- [ ] No hay warnings críticos
- [ ] RFs implementados documentados
- [ ] Comentarios Javadoc en métodos públicos
- [ ] Archivos compartidos coordinados

---

**Última actualización**: [Fecha]  
**Próxima reunión**: [Fecha]

