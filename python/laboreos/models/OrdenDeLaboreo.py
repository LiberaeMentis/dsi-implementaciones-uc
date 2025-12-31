from django.db import models
from .TipoLaboreo import TipoLaboreo
from .MomentoLaboreo import MomentoLaboreo


class OrdenDeLaboreo(models.Model):
    orden = models.IntegerField()
    
    # RELACIÓN UNIDIRECCIONAL: OrdenDeLaboreo -> TipoLaboreo (1)
    # TipoLaboreo NO conoce sus OrdenDeLaboreo
    tipo_laboreo = models.ForeignKey(
        TipoLaboreo, 
        on_delete=models.CASCADE, 
        db_column='tipo_laboreo_nombre',
        related_name='+'
    )
    
    # RELACIÓN UNIDIRECCIONAL: OrdenDeLaboreo -> MomentoLaboreo (1)
    # MomentoLaboreo NO conoce sus OrdenDeLaboreo
    momento_laboreo = models.ForeignKey(
        MomentoLaboreo, 
        on_delete=models.CASCADE, 
        db_column='momento_laboreo_nombre',
        related_name='+'
    )
    
    # NOTA: NO hay atributo 'cultivo' aquí porque la relación es UNIDIRECCIONAL
    # Cultivo -> OrdenDeLaboreo (ManyToMany con tabla intermedia)
    # OrdenDeLaboreo NO conoce sus Cultivos (siguiendo el diagrama académico)
    
    class Meta:
        db_table = 'ordenes_laboreo'
        unique_together = [['orden', 'tipo_laboreo', 'momento_laboreo']]
        constraints = [
            models.UniqueConstraint(
                fields=['orden', 'tipo_laboreo', 'momento_laboreo'],
                name='orden_laboreo_pk'
            )
        ]
    
    def mostrar_laboreo_para_cultivo(self):
        return [
            self.tipo_laboreo.mostrar_tipo_laboreo(),
            self.momento_laboreo.mostrar_momento_laboreo()
        ]
    
    def es_siembra(self):
        return self.tipo_laboreo.es_siembra()
    
    def es_cosecha(self):
        return self.tipo_laboreo.es_cosecha()

