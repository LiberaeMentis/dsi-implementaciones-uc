# API Django - Registro de Laboreos en Lotes

Implementación de la API REST usando Django 5.0 con ORM y SQLite in-memory.

## Requisitos

- Python 3.11+
- pip3

## Instalación

```bash
pip3 install -r requirements.txt
```

## Ejecución

### Opción 1: Script de inicialización (Recomendado)

```bash
python3 iniciar_servidor.py
```

Este script automáticamente:
1. Aplica las migraciones
2. Puebla la base de datos con datos iniciales
3. Inicia el servidor en el puerto 8080

### Opción 2: Paso a paso

```bash
# 1. Aplicar migraciones
python3 manage.py migrate

# 2. Poblar datos (debe ejecutarse en la misma sesión que runserver para SQLite in-memory)
python3 manage.py poblar_datos

# 3. Iniciar servidor
python3 manage.py runserver 8080
```

## Endpoints Disponibles

Todos bajo `/laboreos/`:

- `POST /laboreos/iniciar` - Iniciar registro de laboreos
- `POST /laboreos/seleccionar-campo` - Seleccionar un campo
- `POST /laboreos/seleccionar-lotes` - Seleccionar lotes
- `POST /laboreos/seleccionar-laboreo` - Seleccionar laboreos por lote
- `POST /laboreos/fecha-hora` - Enviar fecha/hora inicio y fin
- `POST /laboreos/seleccionar-empleado` - Seleccionar empleado
- `POST /laboreos/confirmar-registro` - Confirmar registro
- `POST /laboreos/finalizar` - Finalizar registro

## Configuración

- Base de datos: SQLite in-memory (se resetea al reiniciar)
- Puerto: 8080
- CORS: Habilitado para todos los orígenes
- Autenticación: Deshabilitada

## Datos Precargados

- 4 Estados
- 5 Tipos de Suelo
- 5 Momentos de Laboreo
- 8 Tipos de Laboreo
- 4 Cultivos (Soja, Maní, Girasol, Maíz)
- 20 Órdenes de Laboreo
- 3 Empleados
- 4 Lotes con proyectos vigentes
- 3 Campos (2 habilitados, 1 deshabilitado)

## Prueba de la API

```bash
# Probar endpoint inicial
curl -X POST http://localhost:8080/laboreos/iniciar -H "Content-Type: application/json"
```

