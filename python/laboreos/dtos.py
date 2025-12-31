from dataclasses import dataclass, asdict
from typing import List


@dataclass
class CampoResponse:
    nombre: str
    cantidadHectareas: float
    
    def to_dict(self):
        return asdict(self)


@dataclass
class LoteResponse:
    numero: int
    fechaInicioProyecto: str
    
    def to_dict(self):
        return asdict(self)


@dataclass
class LaboreoResponse:
    tipoLaboreo: str
    fecha: str
    
    def to_dict(self):
        return asdict(self)


@dataclass
class TipoLaboreoResponse:
    nombreTipoLaboreo: str
    nombreMomentoLaboreo: str
    
    def to_dict(self):
        return asdict(self)


@dataclass
class LoteInfoResponse:
    cultivoNombre: str
    laboreosRealizados: List[LaboreoResponse]
    tiposLaboreoDisponibles: List[TipoLaboreoResponse]
    
    def to_dict(self):
        return {
            'cultivoNombre': self.cultivoNombre,
            'laboreosRealizados': [lr.to_dict() for lr in self.laboreosRealizados],
            'tiposLaboreoDisponibles': [tl.to_dict() for tl in self.tiposLaboreoDisponibles]
        }


@dataclass
class EmpleadoResponse:
    nombre: str
    apellido: str
    
    def to_dict(self):
        return asdict(self)


@dataclass
class SeleccionarCampoRequest:
    nombreCampo: str


@dataclass
class SeleccionarLotesRequest:
    numerosLote: List[int]


@dataclass
class LaboreoPorLoteRequest:
    numeroLote: int
    laboreo: List[str]


@dataclass
class SeleccionarLaboreosRequest:
    laboreosPorLote: List[LaboreoPorLoteRequest]


@dataclass
class FechaHoraPorLaboreo:
    numeroLote: int
    laboreo: List[str]  # [tipoLaboreo, momentoLaboreo]
    fechaHoraInicio: str
    fechaHoraFin: str


@dataclass
class FechaHoraPorLoteRequest:
    fechasPorLaboreo: List[FechaHoraPorLaboreo]


@dataclass
class EmpleadoPorLaboreo:
    numeroLote: int
    laboreo: List[str]  # [tipoLaboreo, momentoLaboreo]
    nombreEmpleado: str
    apellidoEmpleado: str


@dataclass
class SeleccionarEmpleadoPorLaboreoRequest:
    empleadosPorLaboreo: List[EmpleadoPorLaboreo]

