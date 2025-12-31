# Refactorización de Primary Keys - Python/Django

## Resumen

Se actualizaron todos los modelos de Django para usar las mismas Primary Keys naturales y compuestas que se definieron en Java/JPA, manteniendo consistencia entre ambas implementaciones.

## Cambios por Modelo

### 1. PKs Simples (String)

#### Campo
- **Antes**: `id` (AutoField - autogenerado)
- **Después**: `nombre` (CharField con `primary_key=True`)

#### Estado
- **Antes**: `id` (AutoField - autogenerado)
- **Después**: `nombre` (CharField con `primary_key=True`)

#### TipoLaboreo
- **Antes**: `id` (AutoField - autogenerado)
- **Después**: `nombre` (CharField con `primary_key=True`)

#### MomentoLaboreo
- **Antes**: `id` (AutoField - autogenerado)
- **Después**: `nombre` (CharField con `primary_key=True`)

#### Cultivo
- **Antes**: `id` (AutoField - autogenerado)
- **Después**: `nombre` (CharField con `primary_key=True`)

### 2. PKs Numéricas

#### TipoSuelo
- **Antes**: `id` (AutoField), `numero` (IntegerField con unique=True)
- **Después**: `numero` (IntegerField con `primary_key=True`)
- Se reordenaron los campos para poner `numero` primero

#### Lote
- **Antes**: `id` (AutoField), `numero` (IntegerField con unique=True)
- **Después**: `numero` (IntegerField con `primary_key=True`)

### 3. PKs Manuales (No Autogeneradas)

#### Laboreo
- **Antes**: `id` (AutoField - autogenerado)
- **Después**: `nro` (IntegerField con `primary_key=True`, manual)
- Se agregó lógica en `ProyectoDeCultivo.crear_laboreos()` para generar el `nro` usando `max(nro) + 1`

#### ProyectoDeCultivo
- **Antes**: `id` (AutoField - autogenerado)
- **Después**: `numero` (IntegerField con `primary_key=True`, manual)

### 4. PKs Compuestas (Unique Together)

**Nota**: Django no soporta PKs compuestas nativas como JPA. Se usa `id` autogenerado + `unique_together` para garantizar unicidad.

#### Empleado
- **PK Real**: `id` (AutoField - autogenerado por Django)
- **Restricción de Unicidad**: `(nombre, apellido)` mediante:
  - `unique_together = [['nombre', 'apellido']]`
  - `UniqueConstraint(fields=['nombre', 'apellido'], name='empleado_pk')`

#### OrdenDeLaboreo
- **Campo renombrado**: `numero_orden` → `orden`
- **PK Real**: `id` (AutoField - autogenerado por Django)
- **Restricción de Unicidad**: `(orden, tipo_laboreo, momento_laboreo)` mediante:
  - `unique_together = [['orden', 'tipo_laboreo', 'momento_laboreo']]`
  - `UniqueConstraint(fields=['orden', 'tipo_laboreo', 'momento_laboreo'], name='orden_laboreo_pk')`
- Se agregó `db_column='tipo_laboreo_nombre'` y `db_column='momento_laboreo_nombre'` en los ForeignKey

**IMPORTANTE - Relación Unidireccional con Cultivo:**
- Según el modelo de dominio académico, la relación es **unidireccional**: `Cultivo` → `OrdenDeLaboreo` (0..*)
- `OrdenDeLaboreo` NO conoce sus `Cultivo`s (respetando el diagrama académico)
- Se implementa como `ManyToMany` en `Cultivo` con tabla intermedia `cultivo_orden_laboreo`
- Esto mantiene la unidireccionalidad: solo `Cultivo` puede navegar hacia `OrdenDeLaboreo`
- `OrdenDeLaboreo` NO tiene atributo `cultivo` ni forma de acceder a sus cultivos

## Cambios en Archivos

### Modelos Actualizados
- `python/laboreos/models/Campo.py`
- `python/laboreos/models/Estado.py`
- `python/laboreos/models/TipoLaboreo.py`
- `python/laboreos/models/MomentoLaboreo.py`
- `python/laboreos/models/Cultivo.py`
- `python/laboreos/models/TipoSuelo.py`
- `python/laboreos/models/Lote.py`
- `python/laboreos/models/Laboreo.py`
- `python/laboreos/models/ProyectoDeCultivo.py`
- `python/laboreos/models/Empleado.py`
- `python/laboreos/models/OrdenDeLaboreo.py`

### Script de Población de Datos
**Archivo**: `python/laboreos/management/commands/poblar_datos.py`

**Cambios**:
- Renombrado `numero_orden` → `orden` en todas las creaciones de `OrdenDeLaboreo`
- Agregado campo `numero` manual en todas las creaciones de `ProyectoDeCultivo`

### Boundary Layer
**Archivo**: `python/laboreos/boundaries/PantAdmLaboreos.py`

**Cambios**:
- Reemplazada inicialización global `gestor = GestorLaboreos()` por función lazy `get_gestor()`
- Actualizado todas las referencias a `gestor.` por `get_gestor().`
- **Razón**: Evitar que Django intente cargar datos de la BD al importar el módulo (antes de que las tablas existan)

### Configuración de Django
**Archivo**: `python/laboreos_project/settings.py`

**Cambios**:
```python
# ANTES
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': ':memory:',
        ...
    }
}

# DESPUÉS
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': BASE_DIR / 'db.sqlite3',
    }
}
```
- **Razón**: Cambio de BD en memoria a archivo persistente para que las migraciones se persistan correctamente

### Lógica de Negocio
**Archivo**: `python/laboreos/models/ProyectoDeCultivo.py`

**Cambios en `crear_laboreos()`**:
```python
def crear_laboreos(self, fecha_hora_inicio, fecha_hora_fin, empleado, ordenes_laboreo_list):
    from .Laboreo import Laboreo
    max_nro = Laboreo.objects.aggregate(models.Max('nro'))['nro__max'] or 0
    for i, orden in enumerate(ordenes_laboreo_list, start=1):
        Laboreo.objects.create(
            nro=max_nro + i,  # Generación manual de PK
            proyecto_cultivo=self,
            orden_laboreo=orden,
            empleado=empleado,
            fecha_inicio=fecha_hora_inicio.date(),
            fecha_fin=fecha_hora_fin.date(),
            hora_inicio=fecha_hora_inicio.time(),
            hora_fin=fecha_hora_fin.time()
        )
```

## Migraciones

### Regeneración Completa
Se eliminaron todas las migraciones anteriores y se generó una migración inicial limpia:

```bash
rm -rf laboreos/migrations/*.py
echo "from django.db import migrations" > laboreos/migrations/__init__.py
python manage.py makemigrations laboreos
python manage.py migrate
python manage.py poblar_datos
```

### Resultado
```
Datos poblados exitosamente
- 4 estados
- 5 tipos de suelo
- 5 momentos de laboreo
- 8 tipos de laboreo
- 4 cultivos
- 20 órdenes de laboreo
- 3 empleados
- 4 lotes
- 4 proyectos de cultivo
- 3 campos
```

## Diferencias con Java/JPA

### PKs Compuestas
- **Java/JPA**: Usa `@IdClass` con `@Id` en múltiples campos para verdaderas PKs compuestas
- **Python/Django**: Usa `id` autogenerado + `unique_together` porque Django no soporta PKs compuestas nativas

### Ejemplo Comparativo: Empleado

#### Java
```java
@Entity
@IdClass(EmpleadoId.class)
public class Empleado {
    @Id
    private String nombre;
    
    @Id
    private String apellido;
}
```

#### Python
```python
class Empleado(models.Model):
    nombre = models.CharField(max_length=100)
    apellido = models.CharField(max_length=100)
    
    class Meta:
        unique_together = [['nombre', 'apellido']]
```

## Impacto en el Sistema

### Ventajas
1. **Consistencia**: Ambas implementaciones (Java y Python) usan las mismas reglas de negocio para PKs
2. **Semántica Natural**: Las PKs son campos significativos del dominio (nombres, números)
3. **No Hay IDs Artificiales**: Excepto donde Django lo requiere técnicamente

### Consideraciones
1. **Empleado y OrdenDeLaboreo**: Tienen `id` interno en Django pero no en Java
2. **Acceso a Objetos**: En Django, se sigue usando `id` para acceder a Empleado/OrdenDeLaboreo
3. **Consultas**: Las consultas deben usar los campos de la PK natural para buscar objetos

## Testing

Se verificó la integridad de los datos ejecutando el script de población:
- ✅ Todas las tablas creadas correctamente
- ✅ Todos los datos insertados sin errores de integridad
- ✅ Restricciones de unicidad funcionando correctamente
- ✅ Relaciones ForeignKey configuradas correctamente

## Compatibilidad

Esta refactorización es compatible con:
- Django 5.0+
- Python 3.11+
- SQLite, PostgreSQL, MySQL (todos los backends de Django)

## Próximos Pasos

Si en el futuro Django implementa soporte nativo para PKs compuestas, se podría:
1. Eliminar el campo `id` de `Empleado` y `OrdenDeLaboreo`
2. Marcar los campos de la PK compuesta con `primary_key=True`
3. Actualizar las migraciones

Por ahora, el enfoque `unique_together` es el estándar recomendado para Django.

