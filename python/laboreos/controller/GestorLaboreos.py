from datetime import datetime
from typing import Dict, List, Optional

from ..dtos import (
    CampoResponse,
    EmpleadoResponse,
    LaboreoResponse,
    LoteInfoResponse,
    LoteResponse,
    TipoLaboreoResponse,
)
from ..models import (
    Campo,
    Cultivo,
    Empleado,
    Estado,
    Lote,
    MomentoLaboreo,
    OrdenDeLaboreo,
    TipoLaboreo,
    TipoSuelo,
)


class GestorLaboreos:
    def __init__(self):
        self.campos: List[Campo] = []
        self.lotes: List[Lote] = []
        self.empleados: List[Empleado] = []
        self.tipo_laboreos: List[TipoLaboreo] = []
        self.cultivos: List[Cultivo] = []
        self.estados: List[Estado] = []
        self.tipos_suelo: List[TipoSuelo] = []
        self.momentos_laboreo: List[MomentoLaboreo] = []
        self.ordenes_laboreo: List[OrdenDeLaboreo] = []

        self.campo_seleccionado: Optional[Campo] = None
        self.lotes_seleccionados: List[Lote] = []

        self.cultivo_por_numero_lote: Dict[int, Cultivo] = {}
        self.ordenes_laboreo_por_lote: Dict[Lote, List[OrdenDeLaboreo]] = {}

        # Mapa donde la clave es "numeroLote|tipoLaboreo|momentoLaboreo" y el valor es [fechaHoraInicio, fechaHoraFin]
        self.fechas_por_laboreo: Dict[str, List[datetime]] = {}
        # Mapa donde la clave es "numeroLote|tipoLaboreo|momentoLaboreo" y el valor es el Empleado
        self.empleados_por_laboreo: Dict[str, Empleado] = {}

        self._cargar_datos()

    def _cargar_datos(self):
        self.estados = list(Estado.objects.all())
        self.tipos_suelo = list(TipoSuelo.objects.all())
        self.momentos_laboreo = list(MomentoLaboreo.objects.all())
        self.tipo_laboreos = list(TipoLaboreo.objects.all())
        self.cultivos = list(Cultivo.objects.all())

        self.ordenes_laboreo = list(
            OrdenDeLaboreo.objects.select_related(
                'tipo_laboreo', 'momento_laboreo', 'cultivo'
            ).all()
        )

        self.empleados = list(Empleado.objects.all())

        self.lotes = list(
            Lote.objects.select_related('tipo_suelo').prefetch_related(
                'proyectodecultivo_set__cultivo',
                'proyectodecultivo_set__estado',
                'proyectodecultivo_set__laboreo_set__tipo_laboreo',
                'proyectodecultivo_set__laboreo_set__momento_laboreo',
                'proyectodecultivo_set__laboreo_set__empleado',
                'proyectodecultivo_set__laboreo_set__orden_laboreo'
            ).all()
        )

        self.campos = list(
            Campo.objects.prefetch_related(
                'lotes__proyectodecultivo_set__cultivo',
                'lotes__proyectodecultivo_set__estado'
            ).all()
        )

    def nuevo_laboreo(self) -> List[CampoResponse]:
        return self.buscar_campos()

    def buscar_campos(self) -> List[CampoResponse]:
        campos_habilitados = [campo for campo in self.campos if campo.esta_habilitado()]
        return [
            CampoResponse(
                nombre=campo.nombre,
                cantidadHectareas=float(campo.cantidad_hectareas)
            )
            for campo in campos_habilitados
        ]

    def tomar_seleccion_campo(self, nombre_campo: str) -> List[LoteResponse]:
        campo = next((c for c in self.campos if c.nombre == nombre_campo), None)

        if campo is None:
            return []

        self.campo_seleccionado = campo
        return self.buscar_lotes(campo)

    def buscar_lotes(self, campo: Campo) -> List[LoteResponse]:
        lotes = campo.buscar_lotes_proy_cultivo()
        return [
            LoteResponse(
                numero=lote.numero,
                fechaInicioProyecto=lote.mostrar_fecha_inicio_proy_vigente().isoformat()
            )
            for lote in lotes
        ]

    def tomar_seleccion_lotes(self, numeros_lote: List[int]) -> List[LoteInfoResponse]:
        if not self.campo_seleccionado:
            return []
        
        # Buscar en los lotes del gestor (asumimos que ya son del campo seleccionado)
        self.lotes_seleccionados = [
            lote for lote in self.lotes
            if lote.numero in numeros_lote and lote.conocer_proyecto_de_cultivo_vigente() is not None
        ]

        return self.buscar_info_proyecto_vigente(self.lotes_seleccionados)

    def buscar_info_proyecto_vigente(self, lotes: List[Lote]) -> List[LoteInfoResponse]:
        resultado: List[LoteInfoResponse] = []

        self.cultivo_por_numero_lote.clear()

        for lote in lotes:
            cultivo_nombre = self.campo_seleccionado.mostrar_cultivo(lote)
            if not cultivo_nombre:
                continue

            cultivo = next((c for c in self.cultivos if c.nombre == cultivo_nombre), None)

            if cultivo is not None:
                self.cultivo_por_numero_lote[lote.numero] = cultivo

            laboreos_realizados = self.buscar_laboreos_realizados(lote)
            tipos_laboreo_disponibles = self.buscar_tipos_laboreo_para_cultivo(lote)

            resultado.append(LoteInfoResponse(
                cultivoNombre=cultivo_nombre,
                laboreosRealizados=laboreos_realizados,
                tiposLaboreoDisponibles=tipos_laboreo_disponibles
            ))

        return resultado

    def buscar_laboreos_realizados(self, lote: Lote) -> List[LaboreoResponse]:
        laboreos_info = self.campo_seleccionado.buscar_laboreos_realizados(lote)

        resultado: List[LaboreoResponse] = []
        for info in laboreos_info:
            nombre_tipo = list(info.keys())[0]
            fecha = info[nombre_tipo]
            resultado.append(LaboreoResponse(
                tipoLaboreo=nombre_tipo,
                fecha=fecha.isoformat()
            ))

        return resultado

    def buscar_tipos_laboreo_para_cultivo(self, lote: Lote) -> List[TipoLaboreoResponse]:
        tipos_info = self.campo_seleccionado.buscar_tipo_laboreos_para_cultivo(lote)

        return [
            TipoLaboreoResponse(
                nombreTipoLaboreo=info[0],
                nombreMomentoLaboreo=info[1]
            )
            for info in tipos_info
        ]

    def tomar_selecc_laboreo(self, laboreos_por_lote: List[dict]):
        self.ordenes_laboreo_por_lote.clear()

        if not self.campo_seleccionado or not self.lotes_seleccionados:
            return

        lotes_por_numero = {lote.numero: lote for lote in self.lotes_seleccionados}

        for item in laboreos_por_lote:
            numero_lote = item['numeroLote']
            laboreo_nombres = item['laboreo']

            lote = lotes_por_numero.get(numero_lote)
            cultivo = self.cultivo_por_numero_lote.get(numero_lote)

            if not lote or not cultivo:
                continue

            orden = next(
                (o for o in self.ordenes_laboreo
                 if o.cultivo_id == cultivo.id and
                 o.tipo_laboreo.nombre == laboreo_nombres[0] and
                 o.momento_laboreo.nombre == laboreo_nombres[1]),
                None
            )

            if orden:
                if lote not in self.ordenes_laboreo_por_lote:
                    self.ordenes_laboreo_por_lote[lote] = []
                self.ordenes_laboreo_por_lote[lote].append(orden)

    def _generar_clave_laboreo(self, numero_lote: int, laboreo: List[str]) -> str:
        return f"{numero_lote}|{laboreo[0]}|{laboreo[1]}"

    def tomar_duracion_laboreo(self, fechas_por_laboreo: List[dict]) -> List[EmpleadoResponse]:
        if not fechas_por_laboreo:
            return []

        fechas_map: Dict[str, List[datetime]] = {}

        for fecha_hora in fechas_por_laboreo:
            fecha_hora_inicio = fecha_hora['fechaHoraInicio']
            fecha_hora_fin = fecha_hora['fechaHoraFin']

            clave = self._generar_clave_laboreo(fecha_hora['numeroLote'], fecha_hora['laboreo'])
            fechas_map[clave] = [fecha_hora_inicio, fecha_hora_fin]

        self.fechas_por_laboreo = fechas_map

        return self.buscar_empleados()

    def buscar_empleados(self) -> List[EmpleadoResponse]:
        return [
            EmpleadoResponse(
                nombre=emp.nombre,
                apellido=emp.apellido
            )
            for emp in self.empleados
        ]

    def tomar_empleado(self, empleados_por_laboreo: List[dict]):
        if not empleados_por_laboreo:
            return

        empleados_map: Dict[str, Empleado] = {}

        for empleado_por_laboreo in empleados_por_laboreo:
            nombre_empleado = empleado_por_laboreo.get('nombreEmpleado')
            apellido_empleado = empleado_por_laboreo.get('apellidoEmpleado')

            empleado = next(
                (e for e in self.empleados
                 if e.nombre == nombre_empleado and e.apellido == apellido_empleado),
                None
            )

            if empleado:
                clave = self._generar_clave_laboreo(
                    empleado_por_laboreo['numeroLote'],
                    empleado_por_laboreo['laboreo']
                )
                empleados_map[clave] = empleado

        self.empleados_por_laboreo = empleados_map

    def tomar_confirmacion(self) -> bool:
        self.crear_laboreos()
        return self.validar_tipo_laboreo()

    def crear_laboreos(self):
        if not self.campo_seleccionado or not self.empleados_por_laboreo:
            return

        if not self.ordenes_laboreo_por_lote or not self.fechas_por_laboreo:
            return
        
        # Pre-procesar: asociar cada orden con sus fechas y empleado
        # Convertir a listas de tuplas: (fecha_inicio, fecha_fin, empleado, orden)
        laboreos_por_lote: Dict[Lote, List[tuple]] = {}

        for lote, ordenes_laboreo in self.ordenes_laboreo_por_lote.items():
            numero_lote = lote.numero
            laboreos = []

            for orden in ordenes_laboreo:
                clave = self._generar_clave_laboreo(
                    numero_lote,
                    [orden.tipo_laboreo.nombre, orden.momento_laboreo.nombre]
                )
                fechas = self.fechas_por_laboreo.get(clave)
                empleado = self.empleados_por_laboreo.get(clave)

                if fechas and len(fechas) == 2 and empleado:
                    # Tupla: (fecha_inicio, fecha_fin, empleado, orden)
                    laboreos.append((fechas[0], fechas[1], empleado, orden))

            if laboreos:
                laboreos_por_lote[lote] = laboreos

        self.campo_seleccionado.crear_laboreos_para_proyecto(laboreos_por_lote)

    def validar_tipo_laboreo(self) -> bool:
        if not self.ordenes_laboreo_por_lote:
            return False

        for ordenes_list in self.ordenes_laboreo_por_lote.values():
            for orden in ordenes_list:
                if orden.es_siembra() or orden.es_cosecha():
                    return False

        return True

    def tomar_opcion_finalizar(self):
        self.fin_cu()

    def fin_cu(self):
        pass
