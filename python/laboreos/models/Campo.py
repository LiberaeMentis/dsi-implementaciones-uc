from django.db import models
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
    
    def buscar_tipos_laboreo_para_cultivo(self, lote):
        if not lote:
            return []
        return lote.buscar_laboreos_para_cultivo()
    
    # fechas_por_laboreo: clave = "numeroLote|tipoLaboreo|momentoLaboreo", valor = [fechaHoraInicio, fechaHoraFin]
    # empleados_por_laboreo: clave = "numeroLote|tipoLaboreo|momentoLaboreo", valor = Empleado
    def crear_laboreos_para_proyecto(self, fechas_por_laboreo, empleados_por_laboreo, ordenes_laboreo_por_lote, lotes_seleccionados):
        for lote in lotes_seleccionados:
            numero_lote = lote.numero
            ordenes_list = ordenes_laboreo_por_lote.get(numero_lote)
            
            if ordenes_list:
                # Agrupar órdenes por sus fechas y empleado (pueden tener fechas y empleados diferentes)
                ordenes_por_fecha_y_empleado = {}
                fechas_por_grupo = {}
                empleados_por_grupo = {}
                
                for orden in ordenes_list:
                    clave = f"{numero_lote}|{orden.tipo_laboreo.nombre}|{orden.momento_laboreo.nombre}"
                    fechas = fechas_por_laboreo.get(clave)
                    empleado = empleados_por_laboreo.get(clave)
                    if fechas and len(fechas) == 2 and empleado:
                        # Crear clave para agrupar por fechas y empleado (inicio, fin y empleado)
                        clave_fecha_y_empleado = f"{fechas[0]}|{fechas[1]}|{empleado.nombre}|{empleado.apellido}"
                        if clave_fecha_y_empleado not in ordenes_por_fecha_y_empleado:
                            ordenes_por_fecha_y_empleado[clave_fecha_y_empleado] = []
                        ordenes_por_fecha_y_empleado[clave_fecha_y_empleado].append(orden)
                        if clave_fecha_y_empleado not in fechas_por_grupo:
                            fechas_por_grupo[clave_fecha_y_empleado] = fechas
                        if clave_fecha_y_empleado not in empleados_por_grupo:
                            empleados_por_grupo[clave_fecha_y_empleado] = empleado
                
                # Crear laboreos agrupados por fechas y empleado usando el método existente
                proyecto = lote.conocer_proyecto_de_cultivo_vigente()
                if proyecto:
                    for clave_fecha_y_empleado, ordenes_grupo in ordenes_por_fecha_y_empleado.items():
                        fechas = fechas_por_grupo[clave_fecha_y_empleado]
                        empleado = empleados_por_grupo[clave_fecha_y_empleado]
                        proyecto.crear_laboreos(fechas[0], fechas[1], empleado, ordenes_grupo)

