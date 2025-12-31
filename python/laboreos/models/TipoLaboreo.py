from django.db import models


class TipoLaboreo(models.Model):
    nombre = models.CharField(max_length=100, primary_key=True)
    descripcion = models.TextField()
    
    class Meta:
        db_table = 'tipos_laboreo'
    
    def mostrar_tipo_laboreo(self):
        return self.nombre
    
    def es_siembra(self):
        return self.nombre == "Siembra"
    
    def es_cosecha(self):
        return self.nombre == "Cosecha"

