"""
Middleware para inicializar datos en la primera request.
Como usamos SQLite in-memory, los datos se cargan una vez al inicio del servidor.
"""

class InitializeDatabaseMiddleware:
    _initialized = False
    
    def __init__(self, get_response):
        self.get_response = get_response
    
    def __call__(self, request):
        if not InitializeDatabaseMiddleware._initialized:
            self._initialize_data()
            InitializeDatabaseMiddleware._initialized = True
        
        response = self.get_response(request)
        return response
    
    def _initialize_data(self):
        from django.core.management import call_command
        from laboreos.models import (
            TipoSuelo, Estado, MomentoLaboreo, TipoLaboreo, Empleado,
            Cultivo, OrdenDeLaboreo, Lote, ProyectoDeCultivo, Campo
        )
        from datetime import date, timedelta
        
        print("\n✓ Aplicando migraciones...")
        call_command('migrate', verbosity=0)
        
        if Estado.objects.exists():
            return
        
        print("✓ Poblando base de datos in-memory...")
        
        estado_vigente = Estado.objects.create(nombre="Vigente", descripcion="Proyecto vigente", es_final=False)
        Estado.objects.create(nombre="En Preparación", descripcion="Proyecto en preparación", es_final=False)
        Estado.objects.create(nombre="Finalizado", descripcion="Proyecto finalizado", es_final=True)
        Estado.objects.create(nombre="Cancelado", descripcion="Proyecto cancelado", es_final=True)
        
        suelo_arcilloso = TipoSuelo.objects.create(nombre="Arcilloso", descripcion="Suelo con alto contenido de arcilla", numero=1)
        suelo_arenoso = TipoSuelo.objects.create(nombre="Arenoso", descripcion="Suelo con alto contenido de arena", numero=2)
        TipoSuelo.objects.create(nombre="Limoso", descripcion="Suelo con alto contenido de limo", numero=3)
        TipoSuelo.objects.create(nombre="Franco", descripcion="Suelo equilibrado", numero=4)
        TipoSuelo.objects.create(nombre="Humífero", descripcion="Suelo rico en materia orgánica", numero=5)
        
        momento_pre = MomentoLaboreo.objects.create(nombre="Pre-siembra", descripcion="Antes de la siembra")
        momento_siembra = MomentoLaboreo.objects.create(nombre="Siembra", descripcion="Momento de siembra")
        momento_post = MomentoLaboreo.objects.create(nombre="Post-siembra", descripcion="Después de la siembra")
        momento_crecimiento = MomentoLaboreo.objects.create(nombre="Crecimiento", descripcion="Durante el crecimiento")
        momento_cosecha = MomentoLaboreo.objects.create(nombre="Cosecha", descripcion="Momento de cosecha")
        
        tipo_arado = TipoLaboreo.objects.create(nombre="Arado", descripcion="Roturar la tierra")
        tipo_rastrillado = TipoLaboreo.objects.create(nombre="Rastrillado", descripcion="Nivelar la tierra")
        tipo_siembra = TipoLaboreo.objects.create(nombre="Siembra", descripcion="Plantación del cultivo")
        tipo_escardillado = TipoLaboreo.objects.create(nombre="Escardillado", descripcion="Remoción de malezas")
        tipo_cosecha = TipoLaboreo.objects.create(nombre="Cosecha", descripcion="Recolección")
        tipo_fumigacion = TipoLaboreo.objects.create(nombre="Fumigación", descripcion="Aplicación de agroquímicos")
        tipo_riego = TipoLaboreo.objects.create(nombre="Riego", descripcion="Aplicación de agua")
        
        soja = Cultivo.objects.create(nombre="Soja", tipo_suelo=suelo_arcilloso)
        mani = Cultivo.objects.create(nombre="Maní", tipo_suelo=suelo_arcilloso)
        girasol = Cultivo.objects.create(nombre="Girasol", tipo_suelo=suelo_arcilloso)
        maiz = Cultivo.objects.create(nombre="Maíz", tipo_suelo=suelo_arenoso)
        
        for cultivo, tipos in [
            (soja, [(tipo_arado, momento_pre), (tipo_rastrillado, momento_pre), (tipo_siembra, momento_siembra), (tipo_escardillado, momento_post), (tipo_cosecha, momento_cosecha)]),
            (mani, [(tipo_arado, momento_pre), (tipo_rastrillado, momento_pre), (tipo_siembra, momento_siembra), (tipo_fumigacion, momento_post), (tipo_cosecha, momento_cosecha)]),
            (girasol, [(tipo_arado, momento_pre), (tipo_rastrillado, momento_pre), (tipo_siembra, momento_siembra), (tipo_riego, momento_crecimiento), (tipo_cosecha, momento_cosecha)]),
            (maiz, [(tipo_arado, momento_pre), (tipo_rastrillado, momento_pre), (tipo_siembra, momento_siembra), (tipo_fumigacion, momento_crecimiento), (tipo_cosecha, momento_cosecha)])
        ]:
            for i, (tipo, momento) in enumerate(tipos, 1):
                OrdenDeLaboreo.objects.create(numero_orden=i, tipo_laboreo=tipo, momento_laboreo=momento, cultivo=cultivo)
        
        Empleado.objects.create(nombre="Juan", apellido="Pérez")
        Empleado.objects.create(nombre="María", apellido="González")
        Empleado.objects.create(nombre="Carlos", apellido="Rodríguez")
        
        lote1 = Lote.objects.create(numero=1, superficie_hectareas=10.5, tipo_suelo=suelo_arcilloso)
        ProyectoDeCultivo.objects.create(cultivo=soja, estado=estado_vigente, fecha_inicio=date.today() - timedelta(days=60), lote=lote1)
        
        lote2 = Lote.objects.create(numero=2, superficie_hectareas=15.0, tipo_suelo=suelo_arenoso)
        ProyectoDeCultivo.objects.create(cultivo=maiz, estado=estado_vigente, fecha_inicio=date.today() - timedelta(days=45), lote=lote2)
        
        lote3 = Lote.objects.create(numero=3, superficie_hectareas=12.0, tipo_suelo=suelo_arcilloso)
        ProyectoDeCultivo.objects.create(cultivo=mani, estado=estado_vigente, fecha_inicio=date.today() - timedelta(days=50), lote=lote3)
        
        lote4 = Lote.objects.create(numero=4, superficie_hectareas=8.5, tipo_suelo=suelo_arcilloso)
        ProyectoDeCultivo.objects.create(cultivo=girasol, estado=estado_vigente, fecha_inicio=date.today() - timedelta(days=55), lote=lote4)
        
        campo_norte = Campo.objects.create(nombre="Campo Norte", cantidad_hectareas=25.5, habilitado=True)
        campo_norte.lotes.add(lote1, lote2)
        
        campo_sur = Campo.objects.create(nombre="Campo Sur", cantidad_hectareas=20.5, habilitado=True)
        campo_sur.lotes.add(lote3, lote4)
        
        Campo.objects.create(nombre="Campo Este", cantidad_hectareas=18.0, habilitado=False)
        
        print(f"✓ Datos cargados: {Campo.objects.count()} campos, {Lote.objects.count()} lotes, {Cultivo.objects.count()} cultivos")

