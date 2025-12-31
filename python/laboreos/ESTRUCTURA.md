# Estructura del Paquete Laboreos

## Organización del Código

```
laboreos/
├── models/                  # Modelos de dominio (un archivo por modelo)
│   ├── __init__.py         # Exporta todos los modelos
│   ├── TipoSuelo.py
│   ├── Estado.py
│   ├── MomentoLaboreo.py
│   ├── TipoLaboreo.py
│   ├── Empleado.py
│   ├── Cultivo.py
│   ├── OrdenDeLaboreo.py
│   ├── Lote.py
│   ├── ProyectoDeCultivo.py
│   ├── Campo.py
│   └── Laboreo.py
│
├── config/                  # Configuración de la aplicación
│   ├── __init__.py
│   ├── admin.py            # Configuración del admin de Django
│   ├── middleware.py       # Middleware para inicializar BD in-memory
│   ├── tests.py            # Tests de la aplicación
│   └── urls.py             # Rutas de la API
│
├── controller/              # Controladores de casos de uso
│   ├── __init__.py
│   └── GestorLaboreos.py   # Gestor de casos de uso
│
├── boundaries/              # Límites de la aplicación (interfaces REST)
│   ├── __init__.py
│   └── PantAdmLaboreos.py  # Controlador REST (boundary)
│
├── management/              # Comandos de Django
│   └── commands/
│       └── poblar_datos.py # Comando para poblar datos iniciales
│
├── migrations/              # Migraciones de base de datos
│   └── 0001_initial.py
│
├── dtos.py                 # Data Transfer Objects
└── apps.py                 # Configuración de la app Django
```

## Cambios Realizados

### 1. Modelos Separados
- **Antes**: Un solo archivo `models.py` con todos los modelos
- **Ahora**: Paquete `models/` con un archivo por modelo
- **Beneficio**: Mejor organización, más fácil de navegar y mantener

### 2. Configuración Centralizada
- **Antes**: `admin.py`, `middleware.py`, `tests.py` en la raíz
- **Ahora**: Paquete `config/` con toda la configuración
- **Beneficio**: Separación clara entre lógica de negocio y configuración

### 3. Arquitectura por Capas
- **Antes**: Archivos en la raíz sin organización clara
- **Ahora**: Separación clara por responsabilidades:
  - `boundaries/` - Interfaces REST (límites de la aplicación)
  - `controller/` - Controladores de casos de uso
  - `config/` - Configuración (urls, admin, middleware, tests)
- **Beneficio**: Arquitectura limpia siguiendo principios de Clean Architecture

## Importaciones

### Importar Modelos
```python
from laboreos.models import Campo, Lote, Empleado
```

### Importar Gestor
```python
from laboreos.controller import GestorLaboreos
```

### Importar Boundary (Pantalla)
```python
from laboreos.boundaries import PantAdmLaboreos
# o importar funciones específicas
from laboreos.boundaries import iniciar_registro, seleccionar_campo
```

### Importar Middleware
```python
from laboreos.config.middleware import InitializeDatabaseMiddleware
```

### Importar URLs
```python
from laboreos.config.urls import urlpatterns
```

## Compatibilidad

La reorganización mantiene **100% de compatibilidad** con el código existente:
- Las importaciones desde `laboreos.models` funcionan igual
- Los endpoints de la API no cambiaron
- La funcionalidad es idéntica

## Ventajas de la Nueva Estructura

1. **Arquitectura Limpia**: Separación clara entre boundaries, controllers y modelos
2. **Mejor Organización**: Código agrupado por responsabilidad y capa
3. **Más Mantenible**: Archivos más pequeños y enfocados
4. **Escalable**: Fácil agregar nuevos modelos, controllers o boundaries
5. **Consistente**: Estructura similar a la implementación Java
6. **Profesional**: Sigue mejores prácticas de Django y Clean Architecture
7. **Configuración Centralizada**: Todo lo relacionado con configuración en `config/`

