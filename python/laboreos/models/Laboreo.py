from django.db import models
from datetime import datetime
from .ProyectoDeCultivo import ProyectoDeCultivo
from .OrdenDeLaboreo import OrdenDeLaboreo
from .Empleado import Empleado


class Laboreo(models.Model):
    # RELACIÓN UNIDIRECCIONAL: ProyectoDeCultivo -> Laboreo (0..*)
    # Laboreo NO conoce su ProyectoDeCultivo (FK técnica necesaria para la relación)
    # ProyectoDeCultivo accede a sus laboreos mediante query directo
    proyecto_cultivo = models.ForeignKey(ProyectoDeCultivo, on_delete=models.CASCADE, related_name='+')
    
    # RELACIÓN UNIDIRECCIONAL: Laboreo -> OrdenDeLaboreo (1)
    # OrdenDeLaboreo NO conoce sus Laboreos
    orden_laboreo = models.ForeignKey(OrdenDeLaboreo, on_delete=models.CASCADE, related_name='+')
    
    # RELACIÓN UNIDIRECCIONAL: Laboreo -> Empleado (1)
    # Empleado NO conoce sus Laboreos
    empleado = models.ForeignKey(Empleado, on_delete=models.CASCADE, related_name='+')
    
    fecha_inicio = models.DateField()
    fecha_fin = models.DateField()
    hora_inicio = models.TimeField()
    hora_fin = models.TimeField()
    
    class Meta:
        db_table = 'laboreos'
        unique_together = [
            ['fecha_inicio', 'fecha_fin', 'hora_inicio', 'hora_fin', 'empleado', 'orden_laboreo']
        ]
    
    def mostrar_laboreo(self):
        fecha_completa = datetime.combine(self.fecha_fin, self.hora_fin)
        nombre_tipo = self.orden_laboreo.mostrar_laboreo_para_cultivo()[0]
        return {nombre_tipo: fecha_completa}

