from django.db import models
from .Cultivo import Cultivo
from .Estado import Estado


class ProyectoDeCultivo(models.Model):
    numero = models.IntegerField(primary_key=True)
    
    # RELACIÓN UNIDIRECCIONAL: ProyectoDeCultivo -> Cultivo (1)
    # Cultivo NO conoce sus ProyectosDeCultivo
    cultivo = models.ForeignKey(Cultivo, on_delete=models.CASCADE, related_name='+')
    
    # RELACIÓN UNIDIRECCIONAL: ProyectoDeCultivo -> Estado (1)
    # Estado NO conoce sus ProyectosDeCultivo
    estado = models.ForeignKey(Estado, on_delete=models.CASCADE, related_name='+')
    
    fecha_inicio = models.DateField()
    fecha_fin = models.DateField(null=True, blank=True)
    observaciones = models.TextField(blank=True)
    
    # RELACIÓN BIDIRECCIONAL: Lote <-> ProyectoDeCultivo
    # Lote necesita acceder a sus proyectos para buscar el vigente
    lote = models.ForeignKey('Lote', on_delete=models.CASCADE)
    
    class Meta:
        db_table = 'proyectos_cultivo'
    
    def esta_vigente(self):
        return self.estado is not None and not self.estado.es_final
    
    def buscar_laboreos_realizados(self):
        from .Laboreo import Laboreo
        laboreos = list(Laboreo.objects.filter(proyecto_cultivo=self))
        return [laboreo.mostrar_laboreo() for laboreo in laboreos]
    
    def buscar_laboreos_para_cultivo(self):
        ordenes = list(self.cultivo.ordenes_laboreo.all())
        return [orden.mostrar_laboreo_para_cultivo() for orden in ordenes]
    
    def mostrar_cultivo(self):
        return self.cultivo.nombre
    
    def crear_laboreos(self, fecha_hora_inicio, fecha_hora_fin, empleado, ordenes_laboreo_list):
        from .Laboreo import Laboreo
        for orden in ordenes_laboreo_list:
            Laboreo.objects.create(
                proyecto_cultivo=self,
                orden_laboreo=orden,
                empleado=empleado,
                fecha_inicio=fecha_hora_inicio.date(),
                fecha_fin=fecha_hora_fin.date(),
                hora_inicio=fecha_hora_inicio.time(),
                hora_fin=fecha_hora_fin.time()
            )

