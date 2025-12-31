from django.core.management.base import BaseCommand
from laboreos.models import (
    TipoSuelo, Estado, MomentoLaboreo, TipoLaboreo, Empleado,
    Cultivo, OrdenDeLaboreo, Lote, ProyectoDeCultivo, Campo, Laboreo
)
from datetime import date, timedelta


class Command(BaseCommand):
    help = 'Pobla la base de datos con datos iniciales'
    
    def handle(self, *args, **kwargs):
        self.stdout.write('Poblando datos iniciales...')
        
        # Limpiar datos existentes
        Laboreo.objects.all().delete()
        ProyectoDeCultivo.objects.all().delete()
        Campo.objects.all().delete()
        Lote.objects.all().delete()
        OrdenDeLaboreo.objects.all().delete()
        Cultivo.objects.all().delete()
        Empleado.objects.all().delete()
        TipoLaboreo.objects.all().delete()
        MomentoLaboreo.objects.all().delete()
        TipoSuelo.objects.all().delete()
        Estado.objects.all().delete()
        
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
        
        # Crear Tipos de Suelo (5)
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
        
        # Crear Momentos de Laboreo (5)
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
        
        # Crear Tipos de Laboreo (8)
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
        soja = Cultivo.objects.create(
            nombre="Soja",
            tipo_suelo=suelo_arcilloso
        )
        mani = Cultivo.objects.create(
            nombre="Maní",
            tipo_suelo=suelo_arcilloso
        )
        girasol = Cultivo.objects.create(
            nombre="Girasol",
            tipo_suelo=suelo_arcilloso
        )
        maiz = Cultivo.objects.create(
            nombre="Maíz",
            tipo_suelo=suelo_arenoso
        )
        
        # Crear Órdenes de Laboreo (genéricas, sin asociación a cultivos aún)
        # Órdenes comunes
        orden_1_arado_pre = OrdenDeLaboreo.objects.create(
            orden=1,
            tipo_laboreo=tipo_arado,
            momento_laboreo=momento_pre
        )
        orden_2_rastrillado_pre = OrdenDeLaboreo.objects.create(
            orden=2,
            tipo_laboreo=tipo_rastrillado,
            momento_laboreo=momento_pre
        )
        orden_3_siembra = OrdenDeLaboreo.objects.create(
            orden=3,
            tipo_laboreo=tipo_siembra,
            momento_laboreo=momento_siembra
        )
        orden_4_escardillado_post = OrdenDeLaboreo.objects.create(
            orden=4,
            tipo_laboreo=tipo_escardillado,
            momento_laboreo=momento_post
        )
        orden_4_fumigacion_post = OrdenDeLaboreo.objects.create(
            orden=4,
            tipo_laboreo=tipo_fumigacion,
            momento_laboreo=momento_post
        )
        orden_4_riego_crecimiento = OrdenDeLaboreo.objects.create(
            orden=4,
            tipo_laboreo=tipo_riego,
            momento_laboreo=momento_crecimiento
        )
        orden_4_fumigacion_crecimiento = OrdenDeLaboreo.objects.create(
            orden=4,
            tipo_laboreo=tipo_fumigacion,
            momento_laboreo=momento_crecimiento
        )
        orden_5_cosecha = OrdenDeLaboreo.objects.create(
            orden=5,
            tipo_laboreo=tipo_cosecha,
            momento_laboreo=momento_cosecha
        )
        
        # Asociar Órdenes de Laboreo con Cultivos (ManyToMany - unidireccional)
        # Soja: Arado, Rastrillado, Siembra, Escardillado, Cosecha
        soja.ordenes_laboreo.add(
            orden_1_arado_pre,
            orden_2_rastrillado_pre,
            orden_3_siembra,
            orden_4_escardillado_post,
            orden_5_cosecha
        )
        
        # Maní: Arado, Rastrillado, Siembra, Fumigación (post), Cosecha
        mani.ordenes_laboreo.add(
            orden_1_arado_pre,
            orden_2_rastrillado_pre,
            orden_3_siembra,
            orden_4_fumigacion_post,
            orden_5_cosecha
        )
        
        # Girasol: Arado, Rastrillado, Siembra, Riego, Cosecha
        girasol.ordenes_laboreo.add(
            orden_1_arado_pre,
            orden_2_rastrillado_pre,
            orden_3_siembra,
            orden_4_riego_crecimiento,
            orden_5_cosecha
        )
        
        # Maíz: Arado, Rastrillado, Siembra, Fumigación (crecimiento), Cosecha
        maiz.ordenes_laboreo.add(
            orden_1_arado_pre,
            orden_2_rastrillado_pre,
            orden_3_siembra,
            orden_4_fumigacion_crecimiento,
            orden_5_cosecha
        )
        
        # Crear Empleados (3)
        empleado1 = Empleado.objects.create(nombre="Juan", apellido="Pérez")
        empleado2 = Empleado.objects.create(nombre="María", apellido="González")
        empleado3 = Empleado.objects.create(nombre="Carlos", apellido="Rodríguez")
        
        # Crear Lotes con Proyectos
        lote1 = Lote.objects.create(
            numero=1,
            superficie_hectareas=10.5,
            tipo_suelo=suelo_arcilloso
        )
        proyecto1 = ProyectoDeCultivo.objects.create(
            numero=1,
            cultivo=soja,
            estado=estado_vigente,
            fecha_inicio=date.today() - timedelta(days=60),
            lote=lote1
        )
        
        lote2 = Lote.objects.create(
            numero=2,
            superficie_hectareas=15.0,
            tipo_suelo=suelo_arenoso
        )
        proyecto2 = ProyectoDeCultivo.objects.create(
            numero=2,
            cultivo=maiz,
            estado=estado_vigente,
            fecha_inicio=date.today() - timedelta(days=45),
            lote=lote2
        )
        
        lote3 = Lote.objects.create(
            numero=3,
            superficie_hectareas=12.0,
            tipo_suelo=suelo_arcilloso
        )
        proyecto3 = ProyectoDeCultivo.objects.create(
            numero=3,
            cultivo=mani,
            estado=estado_vigente,
            fecha_inicio=date.today() - timedelta(days=50),
            lote=lote3
        )
        
        lote4 = Lote.objects.create(
            numero=4,
            superficie_hectareas=8.5,
            tipo_suelo=suelo_arcilloso
        )
        proyecto4 = ProyectoDeCultivo.objects.create(
            numero=4,
            cultivo=girasol,
            estado=estado_vigente,
            fecha_inicio=date.today() - timedelta(days=55),
            lote=lote4
        )
        
        # Crear Campos con relaciones ManyToMany
        campo_norte = Campo.objects.create(
            nombre="Campo Norte",
            cantidad_hectareas=25.5,
            habilitado=True
        )
        campo_norte.lotes.add(lote1, lote2)
        
        campo_sur = Campo.objects.create(
            nombre="Campo Sur",
            cantidad_hectareas=20.5,
            habilitado=True
        )
        campo_sur.lotes.add(lote3, lote4)
        
        campo_este = Campo.objects.create(
            nombre="Campo Este",
            cantidad_hectareas=18.0,
            habilitado=False
        )
        
        self.stdout.write(self.style.SUCCESS('Datos poblados exitosamente'))
        self.stdout.write(f'- {Estado.objects.count()} estados')
        self.stdout.write(f'- {TipoSuelo.objects.count()} tipos de suelo')
        self.stdout.write(f'- {MomentoLaboreo.objects.count()} momentos de laboreo')
        self.stdout.write(f'- {TipoLaboreo.objects.count()} tipos de laboreo')
        self.stdout.write(f'- {Cultivo.objects.count()} cultivos')
        self.stdout.write(f'- {OrdenDeLaboreo.objects.count()} órdenes de laboreo')
        self.stdout.write(f'- {Empleado.objects.count()} empleados')
        self.stdout.write(f'- {Lote.objects.count()} lotes')
        self.stdout.write(f'- {ProyectoDeCultivo.objects.count()} proyectos de cultivo')
        self.stdout.write(f'- {Campo.objects.count()} campos')

