#!/usr/bin/env python3
"""
Script para inicializar la base de datos in-memory y ejecutar el servidor Django.
Como usamos SQLite in-memory, necesitamos poblar los datos cada vez que iniciamos.
"""
import os
import sys
import django

# Configurar el entorno Django
os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'laboreos_project.settings')
django.setup()

# Ejecutar migraciones
print("Aplicando migraciones...")
from django.core.management import call_command
call_command('migrate', verbosity=0)

# Poblar datos iniciales
print("Poblando datos iniciales...")
from laboreos.models import (
    TipoSuelo, Estado, MomentoLaboreo, TipoLaboreo, Empleado,
    Cultivo, OrdenDeLaboreo, Lote, ProyectoDeCultivo, Campo
)
from datetime import date, timedelta

# Crear Estados
estado_preparacion = Estado.objects.create(
    nombre="En Preparación",
    descripcion="Proyecto en preparación",
    es_final=False
)
estado_vigente = Estado.objects.create(
    nombre="Vigente",
    descripcion="Proyecto vigente",
    es_final=False
)
estado_finalizado = Estado.objects.create(
    nombre="Finalizado",
    descripcion="Proyecto finalizado",
    es_final=True
)
estado_cancelado = Estado.objects.create(
    nombre="Cancelado",
    descripcion="Proyecto cancelado",
    es_final=True
)

# Crear Tipos de Suelo
suelo_arcilloso = TipoSuelo.objects.create(
    nombre="Arcilloso",
    descripcion="Suelo con alto contenido de arcilla",
    numero=1
)
suelo_arenoso = TipoSuelo.objects.create(
    nombre="Arenoso",
    descripcion="Suelo con alto contenido de arena",
    numero=2
)
suelo_limoso = TipoSuelo.objects.create(
    nombre="Limoso",
    descripcion="Suelo con alto contenido de limo",
    numero=3
)
suelo_franco = TipoSuelo.objects.create(
    nombre="Franco",
    descripcion="Suelo equilibrado con arcilla, arena y limo",
    numero=4
)
suelo_humifero = TipoSuelo.objects.create(
    nombre="Humífero",
    descripcion="Suelo rico en materia orgánica",
    numero=5
)

# Crear Momentos de Laboreo
momento_pre = MomentoLaboreo.objects.create(
    nombre="Pre-siembra",
    descripcion="Laboreo antes de la siembra"
)
momento_siembra = MomentoLaboreo.objects.create(
    nombre="Siembra",
    descripcion="Momento de siembra"
)
momento_post = MomentoLaboreo.objects.create(
    nombre="Post-siembra",
    descripcion="Laboreo después de la siembra"
)
momento_crecimiento = MomentoLaboreo.objects.create(
    nombre="Crecimiento",
    descripcion="Laboreo durante el crecimiento"
)
momento_cosecha = MomentoLaboreo.objects.create(
    nombre="Cosecha",
    descripcion="Momento de cosecha"
)

# Crear Tipos de Laboreo
tipo_arado = TipoLaboreo.objects.create(
    nombre="Arado",
    descripcion="Roturar la tierra"
)
tipo_rastrillado = TipoLaboreo.objects.create(
    nombre="Rastrillado",
    descripcion="Nivelar y desmenuzar la tierra"
)
tipo_siembra = TipoLaboreo.objects.create(
    nombre="Siembra",
    descripcion="Plantación del cultivo"
)
tipo_escardillado = TipoLaboreo.objects.create(
    nombre="Escardillado",
    descripcion="Remoción de malezas entre surcos"
)
tipo_cosecha = TipoLaboreo.objects.create(
    nombre="Cosecha",
    descripcion="Recolección del cultivo"
)
tipo_fumigacion = TipoLaboreo.objects.create(
    nombre="Fumigación",
    descripcion="Aplicación de agroquímicos"
)
tipo_riego = TipoLaboreo.objects.create(
    nombre="Riego",
    descripcion="Aplicación de agua al cultivo"
)
tipo_rolado = TipoLaboreo.objects.create(
    nombre="Rolado",
    descripcion="Aplastamiento de rastrojos"
)

# Crear Cultivos
soja = Cultivo.objects.create(nombre="Soja", tipo_suelo=suelo_arcilloso)
mani = Cultivo.objects.create(nombre="Maní", tipo_suelo=suelo_arcilloso)
girasol = Cultivo.objects.create(nombre="Girasol", tipo_suelo=suelo_arcilloso)
maiz = Cultivo.objects.create(nombre="Maíz", tipo_suelo=suelo_arenoso)

# Crear Órdenes de Laboreo para Soja
OrdenDeLaboreo.objects.create(numero_orden=1, tipo_laboreo=tipo_arado, momento_laboreo=momento_pre, cultivo=soja)
OrdenDeLaboreo.objects.create(numero_orden=2, tipo_laboreo=tipo_rastrillado, momento_laboreo=momento_pre, cultivo=soja)
OrdenDeLaboreo.objects.create(numero_orden=3, tipo_laboreo=tipo_siembra, momento_laboreo=momento_siembra, cultivo=soja)
OrdenDeLaboreo.objects.create(numero_orden=4, tipo_laboreo=tipo_escardillado, momento_laboreo=momento_post, cultivo=soja)
OrdenDeLaboreo.objects.create(numero_orden=5, tipo_laboreo=tipo_cosecha, momento_laboreo=momento_cosecha, cultivo=soja)

# Crear Órdenes de Laboreo para Maní
OrdenDeLaboreo.objects.create(numero_orden=1, tipo_laboreo=tipo_arado, momento_laboreo=momento_pre, cultivo=mani)
OrdenDeLaboreo.objects.create(numero_orden=2, tipo_laboreo=tipo_rastrillado, momento_laboreo=momento_pre, cultivo=mani)
OrdenDeLaboreo.objects.create(numero_orden=3, tipo_laboreo=tipo_siembra, momento_laboreo=momento_siembra, cultivo=mani)
OrdenDeLaboreo.objects.create(numero_orden=4, tipo_laboreo=tipo_fumigacion, momento_laboreo=momento_post, cultivo=mani)
OrdenDeLaboreo.objects.create(numero_orden=5, tipo_laboreo=tipo_cosecha, momento_laboreo=momento_cosecha, cultivo=mani)

# Crear Órdenes de Laboreo para Girasol
OrdenDeLaboreo.objects.create(numero_orden=1, tipo_laboreo=tipo_arado, momento_laboreo=momento_pre, cultivo=girasol)
OrdenDeLaboreo.objects.create(numero_orden=2, tipo_laboreo=tipo_rastrillado, momento_laboreo=momento_pre, cultivo=girasol)
OrdenDeLaboreo.objects.create(numero_orden=3, tipo_laboreo=tipo_siembra, momento_laboreo=momento_siembra, cultivo=girasol)
OrdenDeLaboreo.objects.create(numero_orden=4, tipo_laboreo=tipo_riego, momento_laboreo=momento_crecimiento, cultivo=girasol)
OrdenDeLaboreo.objects.create(numero_orden=5, tipo_laboreo=tipo_cosecha, momento_laboreo=momento_cosecha, cultivo=girasol)

# Crear Órdenes de Laboreo para Maíz
OrdenDeLaboreo.objects.create(numero_orden=1, tipo_laboreo=tipo_arado, momento_laboreo=momento_pre, cultivo=maiz)
OrdenDeLaboreo.objects.create(numero_orden=2, tipo_laboreo=tipo_rastrillado, momento_laboreo=momento_pre, cultivo=maiz)
OrdenDeLaboreo.objects.create(numero_orden=3, tipo_laboreo=tipo_siembra, momento_laboreo=momento_siembra, cultivo=maiz)
OrdenDeLaboreo.objects.create(numero_orden=4, tipo_laboreo=tipo_fumigacion, momento_laboreo=momento_crecimiento, cultivo=maiz)
OrdenDeLaboreo.objects.create(numero_orden=5, tipo_laboreo=tipo_cosecha, momento_laboreo=momento_cosecha, cultivo=maiz)

# Crear Empleados
Empleado.objects.create(nombre="Juan", apellido="Pérez")
Empleado.objects.create(nombre="María", apellido="González")
Empleado.objects.create(nombre="Carlos", apellido="Rodríguez")

# Crear Lotes con Proyectos
lote1 = Lote.objects.create(numero=1, superficie_hectareas=10.5, tipo_suelo=suelo_arcilloso)
ProyectoDeCultivo.objects.create(
    cultivo=soja, estado=estado_vigente,
    fecha_inicio=date.today() - timedelta(days=60), lote=lote1
)

lote2 = Lote.objects.create(numero=2, superficie_hectareas=15.0, tipo_suelo=suelo_arenoso)
ProyectoDeCultivo.objects.create(
    cultivo=maiz, estado=estado_vigente,
    fecha_inicio=date.today() - timedelta(days=45), lote=lote2
)

lote3 = Lote.objects.create(numero=3, superficie_hectareas=12.0, tipo_suelo=suelo_arcilloso)
ProyectoDeCultivo.objects.create(
    cultivo=mani, estado=estado_vigente,
    fecha_inicio=date.today() - timedelta(days=50), lote=lote3
)

lote4 = Lote.objects.create(numero=4, superficie_hectareas=8.5, tipo_suelo=suelo_arcilloso)
ProyectoDeCultivo.objects.create(
    cultivo=girasol, estado=estado_vigente,
    fecha_inicio=date.today() - timedelta(days=55), lote=lote4
)

# Crear Campos con relaciones ManyToMany
campo_norte = Campo.objects.create(nombre="Campo Norte", cantidad_hectareas=25.5, habilitado=True)
campo_norte.lotes.add(lote1, lote2)

campo_sur = Campo.objects.create(nombre="Campo Sur", cantidad_hectareas=20.5, habilitado=True)
campo_sur.lotes.add(lote3, lote4)

Campo.objects.create(nombre="Campo Este", cantidad_hectareas=18.0, habilitado=False)

print(f"✓ Datos poblados exitosamente:")
print(f"  - {Estado.objects.count()} estados")
print(f"  - {TipoSuelo.objects.count()} tipos de suelo")
print(f"  - {MomentoLaboreo.objects.count()} momentos de laboreo")
print(f"  - {TipoLaboreo.objects.count()} tipos de laboreo")
print(f"  - {Cultivo.objects.count()} cultivos")
print(f"  - {OrdenDeLaboreo.objects.count()} órdenes de laboreo")
print(f"  - {Empleado.objects.count()} empleados")
print(f"  - {Lote.objects.count()} lotes")
print(f"  - {ProyectoDeCultivo.objects.count()} proyectos de cultivo")
print(f"  - {Campo.objects.count()} campos")
print("\n✓ Iniciando servidor Django en puerto 8080...")
print("✓ Accede a http://localhost:8080/laboreos/iniciar para comenzar\n")

# Iniciar servidor
call_command('runserver', '8080', verbosity=1)

