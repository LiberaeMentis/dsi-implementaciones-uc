from django.db import models


class Empleado(models.Model):
    nombre = models.CharField(max_length=100)
    apellido = models.CharField(max_length=100)
    
    class Meta:
        db_table = 'empleados'
        unique_together = [['nombre', 'apellido']]
        constraints = [
            models.UniqueConstraint(
                fields=['nombre', 'apellido'],
                name='empleado_pk'
            )
        ]
    
    def get_empleado(self):
        return [self.nombre, self.apellido]

