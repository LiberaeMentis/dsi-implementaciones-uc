from django.db import models


class MomentoLaboreo(models.Model):
    nombre = models.CharField(max_length=100, primary_key=True)
    descripcion = models.TextField()
    
    class Meta:
        db_table = 'momentos_laboreo'
    
    def mostrar_momento_laboreo(self):
        return self.nombre

