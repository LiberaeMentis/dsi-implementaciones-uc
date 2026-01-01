from django.db import models
from typing import List, Dict
from .Lote import Lote


class Campo(models.Model):
    nombre = models.CharField(max_length=100, primary_key=True)
    cantidad_hectareas = models.DecimalField(max_digits=10, decimal_places=2)
    habilitado = models.BooleanField(default=True)
    
    # RELACIÓN UNIDIRECCIONAL: Campo -> Lote (0..*)
    # Lote NO conoce su Campo
    lotes = models.ManyToManyField(Lote, related_name='+')
    
    class Meta:
        db_table = 'campos'
    
    def esta_habilitado(self):
        return self.habilitado
    
    def buscar_lotes_proy_cultivo(self):
        return [
            lote for lote in self.lotes.all()
            if lote.conocer_proyecto_de_cultivo_vigente() is not None
        ]
    
    def buscar_lote(self, numero_lote):
        return self.lotes.filter(numero=numero_lote).first()
    
    def mostrar_cultivo(self, numero_lote=None, lote=None):
        # Si se pasa un objeto Lote directamente (como primer argumento posicional)
        if isinstance(numero_lote, Lote):
            return numero_lote.mostrar_cultivo()
        # Si se pasa lote como keyword argument
        elif lote is not None:
            return lote.mostrar_cultivo()
        # Si se pasa numero_lote como int
        elif numero_lote is not None:
            lote_obj = self.buscar_lote(numero_lote)
            if not lote_obj:
                return None
            proyecto = lote_obj.conocer_proyecto_de_cultivo_vigente()
            return proyecto.cultivo.nombre if proyecto else None
        return None
    
    def buscar_laboreos_realizados(self, numero_lote=None, lote=None):
        # Si se pasa un objeto Lote directamente (como primer argumento posicional)
        if isinstance(numero_lote, Lote):
            lote_obj = numero_lote
        # Si se pasa lote como keyword argument
        elif lote is not None:
            lote_obj = lote
        # Si se pasa numero_lote como int
        elif numero_lote is not None:
            lote_obj = self.buscar_lote(numero_lote)
        else:
            return []
        
        if not lote_obj:
            return []
        proyecto = lote_obj.conocer_proyecto_de_cultivo_vigente()
        return proyecto.buscar_laboreos_realizados() if proyecto else []
    
    def buscar_tipo_laboreos_para_cultivo(self, lote):
        if not lote:
            return []
        return lote.buscar_laboreos_para_cultivo()
    
    def crear_laboreos_para_proyecto(self, laboreos_por_lote: Dict[Lote, List[tuple]]):
        """
        Recibe laboreos individuales por lote como tuplas.
        Cada tupla contiene: (fecha_inicio, fecha_fin, empleado, orden)
        Hace el desempaquetado a los tipos correctos y delega al lote la creación.
        
        Args:
            laboreos_por_lote: Diccionario donde cada clave es un Lote y el valor es una lista
                              de tuplas con la información de cada laboreo a crear
        """
        # CICLO EXTERNO: Iterar sobre cada lote y su lista de laboreos
        for lote, laboreos in laboreos_por_lote.items():
            # CICLO INTERNO: Para cada lote, procesar todos sus laboreos
            # Cada laboreo viene como una tupla que necesita ser desempaquetada
            for laboreo_tuple in laboreos:
                # Desempaquetar la tupla a sus componentes
                # La tupla tiene la estructura: (fecha_inicio, fecha_fin, empleado, orden)
                fecha_inicio, fecha_fin, empleado, orden = laboreo_tuple
                
                # Pasar los datos ya desempaquetados al lote para que delegue la creación del laboreo
                lote.crear_laboreos(fecha_inicio, fecha_fin, empleado, orden)

