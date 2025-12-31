from django.db import models


class Estado(models.Model):
    nombre = models.CharField(max_length=100, primary_key=True)
    descripcion = models.TextField()
    es_final = models.BooleanField(default=False)
    
    class Meta:
        db_table = 'estados'
    
    def __str__(self):
        return self.nombre

