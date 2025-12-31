from django.urls import path
from ..boundaries import PantAdmLaboreos

urlpatterns = [
    path('iniciar', PantAdmLaboreos.iniciar_registro, name='iniciar_registro'),
    path('seleccionar-campo', PantAdmLaboreos.seleccionar_campo, name='seleccionar_campo'),
    path('seleccionar-lotes', PantAdmLaboreos.seleccionar_lotes, name='seleccionar_lotes'),
    path('seleccionar-laboreo', PantAdmLaboreos.seleccionar_laboreo, name='seleccionar_laboreo'),
    path('fecha-hora', PantAdmLaboreos.enviar_fecha_hora, name='enviar_fecha_hora'),
    path('seleccionar-empleado', PantAdmLaboreos.seleccionar_empleado, name='seleccionar_empleado'),
    path('confirmar-registro', PantAdmLaboreos.confirmar_registro, name='confirmar_registro'),
    path('finalizar', PantAdmLaboreos.finalizar_registro, name='finalizar_registro'),
]

