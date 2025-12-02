# Credenciales de Prueba - Bizly

## 🔐 Usuario de Prueba

El seeder crea automáticamente un usuario de prueba al iniciar la aplicación.

### Credenciales:
- **Email**: `test@bizly.com`
- **Contraseña**: `123456`
- **Tipo**: EMPRENDEDOR
- **Estado**: Activo

### Empresa de Prueba:
- **Nombre**: "Mi Emprendimiento de Prueba"
- **Rubro**: "Gastronomía"
- **Margen de Ganancia**: 30%
- **Descripción**: "Emprendimiento de prueba para desarrollo"

## 🧪 Cómo Probar el Login

### Opción 1: Usando IniciarSesionUseCase (Recomendado)

```java
IniciarSesionUseCase useCase = new IniciarSesionUseCase(context);
try {
    Usuario usuario = useCase.ejecutar("test@bizly.com", "123456");
    // Login exitoso
    Log.d("Login", "Usuario autenticado: " + usuario.getNombre());
} catch (AppException e) {
    // Error de autenticación
    Log.e("Login", "Error: " + e.getMessage());
}
```

### Opción 2: Usando UsuarioRepository directamente

```java
UsuarioRepository repository = new UsuarioRepositoryLocal(context);
Usuario usuario = repository.iniciarSesion("test@bizly.com", "123456");
if (usuario != null) {
    // Login exitoso
} else {
    // Credenciales incorrectas
}
```

## 📝 Notas

- El seeder se ejecuta automáticamente en `MainActivity.onCreate()`
- Solo crea datos si no existen (verifica por email)
- La contraseña está hasheada usando `HashPasswordService`
- Para resetear los datos, elimina la app y vuelve a instalarla, o llama a `seeder.clearDatabase()`

## 🔄 Resetear Datos

Si necesitas resetear los datos de prueba:

```java
DatabaseSeeder seeder = new DatabaseSeeder(context);
seeder.clearDatabase(); // Elimina la base de datos
seeder.seedDatabase();  // Vuelve a crear los datos
```

