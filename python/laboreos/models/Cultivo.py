from django.db import models
from .TipoSuelo import TipoSuelo


class Cultivo(models.Model):
    nombre = models.CharField(max_length=100, primary_key=True)
    
    # RELACIÓN UNIDIRECCIONAL: Cultivo -> TipoSuelo (1)
    # TipoSuelo NO conoce sus Cultivos
    tipo_suelo = models.ForeignKey(TipoSuelo, on_delete=models.CASCADE, related_name='+')
    
    # RELACIÓN UNIDIRECCIONAL: Cultivo -> OrdenDeLaboreo (0..*)
    # OrdenDeLaboreo NO conoce sus Cultivos (siguiendo el diagrama académico)
    # Se usa ManyToMany para mantener unidireccionalidad sin FK en OrdenDeLaboreo
    ordenes_laboreo = models.ManyToManyField(
        'OrdenDeLaboreo',
        db_table='cultivo_orden_laboreo',
        related_name='+'  # '+' evita la relación inversa
    )
    
    class Meta:
        db_table = 'cultivos'
    
    def buscar_tipos_laboreo(self):
        ordenes = list(self.ordenes_laboreo.all())
        return [orden.mostrar_laboreo_para_cultivo() for orden in ordenes]
    
    def conocer_orden_laboreo(self):
        return list(self.ordenes_laboreo.all())

