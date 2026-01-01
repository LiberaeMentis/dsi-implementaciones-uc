from django.db import models
from .TipoSuelo import TipoSuelo


class Lote(models.Model):
    numero = models.IntegerField(primary_key=True)
    superficie_hectareas = models.DecimalField(max_digits=10, decimal_places=2)
    
    # RELACIÓN UNIDIRECCIONAL: Lote -> TipoSuelo (1)
    # TipoSuelo NO conoce sus Lotes
    tipo_suelo = models.ForeignKey(TipoSuelo, on_delete=models.CASCADE, related_name='+')
    
    class Meta:
        db_table = 'lotes'
    
    def conocer_proyecto_de_cultivo_vigente(self):
        proyectos = list(self.proyectodecultivo_set.all())
        for proyecto in proyectos:
            if proyecto.esta_vigente():
                return proyecto
        return None
    
    def mostrar_fecha_inicio_proy_vigente(self):
        proyecto = self.conocer_proyecto_de_cultivo_vigente()
        return proyecto.fecha_inicio if proyecto else None
    
    def buscar_laboreos_para_cultivo(self):
        proyecto = self.conocer_proyecto_de_cultivo_vigente()
        return proyecto.buscar_laboreos_para_cultivo() if proyecto else []
    
    def mostrar_cultivo(self):
        proyecto = self.conocer_proyecto_de_cultivo_vigente()
        return proyecto.mostrar_cultivo() if proyecto else None
    
    def buscar_laboreos_realizados(self):
        proyecto = self.conocer_proyecto_de_cultivo_vigente()
        return proyecto.buscar_laboreos_realizados() if proyecto else []
    
    # Recibe los datos de un laboreo ya casteados y delega al proyecto vigente
    def crear_laboreos(self, fecha_inicio, fecha_fin, empleado, orden):
        proyecto = self.conocer_proyecto_de_cultivo_vigente()
        if proyecto:
            proyecto.crear_laboreos(fecha_inicio, fecha_fin, empleado, orden)

