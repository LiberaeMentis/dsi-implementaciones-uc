from django.db import models


class TipoSuelo(models.Model):
    numero = models.IntegerField(primary_key=True)
    nombre = models.CharField(max_length=100)
    descripcion = models.TextField()
    
    class Meta:
        db_table = 'tipos_suelo'
        verbose_name_plural = 'Tipos de Suelo'
    
    def __str__(self):
        return f"{self.nombre} (Tipo {self.numero})"

