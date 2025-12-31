from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
from ..controller import GestorLaboreos
from datetime import datetime

_gestor = None

def get_gestor():
    global _gestor
    if _gestor is None:
        _gestor = GestorLaboreos()
    return _gestor


@api_view(['POST'])
def iniciar_registro(request):
    campos = get_gestor().nuevo_laboreo()
    return Response([c.to_dict() for c in campos])


@api_view(['POST'])
def seleccionar_campo(request):
    nombre_campo = request.data.get('nombreCampo')
    
    if not nombre_campo:
        return Response(
            {'error': 'nombreCampo es requerido'},
            status=status.HTTP_400_BAD_REQUEST
        )
    
    gestor = get_gestor()
    lotes = gestor.tomar_seleccion_campo(nombre_campo)
    
    if not lotes:
        return Response(
            {'error': 'Campo no encontrado o sin lotes disponibles'},
            status=status.HTTP_404_NOT_FOUND
        )
    
    return Response([l.to_dict() for l in lotes])


@api_view(['POST'])
def seleccionar_lotes(request):
    numeros_lote = request.data.get('numerosLote')
    
    if not numeros_lote or not isinstance(numeros_lote, list):
        return Response(
            {'error': 'numerosLote debe ser una lista'},
            status=status.HTTP_400_BAD_REQUEST
        )
    
    gestor = get_gestor()
    lotes_info = gestor.tomar_seleccion_lotes(numeros_lote)
    
    if not lotes_info:
        return Response(
            {'error': 'No se encontró información para los lotes seleccionados'},
            status=status.HTTP_404_NOT_FOUND
        )
    
    return Response([li.to_dict() for li in lotes_info])


@api_view(['POST'])
def seleccionar_laboreo(request):
    laboreos_por_lote = request.data.get('laboreosPorLote')
    
    if not laboreos_por_lote or not isinstance(laboreos_por_lote, list):
        return Response(
            {'error': 'laboreosPorLote debe ser una lista'},
            status=status.HTTP_400_BAD_REQUEST
        )
    
    get_gestor().tomar_selecc_laboreo(laboreos_por_lote)
    
    return Response({'mensaje': 'Laboreos seleccionados correctamente'})


@api_view(['POST'])
def enviar_fecha_hora(request):
    fechas_por_laboreo = request.data.get('fechasPorLaboreo')
    
    if not fechas_por_laboreo or not isinstance(fechas_por_laboreo, list):
        return Response(
            {'error': 'fechasPorLaboreo debe ser una lista'},
            status=status.HTTP_400_BAD_REQUEST
        )
    
    # Convertir las fechas de string a datetime
    fechas_convertidas = []
    for item in fechas_por_laboreo:
    try:
            fecha_hora_inicio = datetime.fromisoformat(item['fechaHoraInicio'].replace('Z', '+00:00'))
            fecha_hora_fin = datetime.fromisoformat(item['fechaHoraFin'].replace('Z', '+00:00'))
            fechas_convertidas.append({
                'numeroLote': item['numeroLote'],
                'laboreo': item['laboreo'],
                'fechaHoraInicio': fecha_hora_inicio,
                'fechaHoraFin': fecha_hora_fin
            })
        except (ValueError, KeyError) as e:
        return Response(
                {'error': f'Formato de fecha inválido o datos faltantes: {str(e)}'},
            status=status.HTTP_400_BAD_REQUEST
        )
    
    gestor = get_gestor()
    empleados = gestor.tomar_fecha_hora_inicio_fin(fechas_convertidas)
    
    if not empleados:
        return Response(
            {'error': 'Las fechas no son válidas o faltan fechas para algún laboreo. La fecha de inicio debe ser anterior a la fecha de fin y ambas deben ser anteriores a la fecha actual.'},
            status=status.HTTP_400_BAD_REQUEST
        )
    
    return Response([e.to_dict() for e in empleados])


@api_view(['POST'])
def seleccionar_empleado(request):
    empleados_por_laboreo = request.data.get('empleadosPorLaboreo')
    
    if not empleados_por_laboreo or not isinstance(empleados_por_laboreo, list):
        return Response(
            {'error': 'empleadosPorLaboreo debe ser una lista'},
            status=status.HTTP_400_BAD_REQUEST
        )
    
    gestor = get_gestor()
    gestor.tomar_empleado(empleados_por_laboreo)
    
    return Response({'mensaje': 'Empleados seleccionados correctamente'})


@api_view(['POST'])
def confirmar_registro(request):
    gestor = get_gestor()
    es_valido = gestor.tomar_confirmacion()
    
    return Response({'esValido': es_valido})


@api_view(['POST'])
def finalizar_registro(request):
    get_gestor().tomar_opcion_finalizar()
    
    return Response({'mensaje': 'Registro finalizado correctamente'})

