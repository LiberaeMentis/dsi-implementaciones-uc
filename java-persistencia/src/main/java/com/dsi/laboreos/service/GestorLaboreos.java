package com.dsi.laboreos.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.dsi.laboreos.dto.CampoResponse;
import com.dsi.laboreos.dto.EmpleadoResponse;
import com.dsi.laboreos.dto.FechaHoraPorLoteRequest;
import com.dsi.laboreos.dto.LaboreoResponse;
import com.dsi.laboreos.dto.LoteInfoResponse;
import com.dsi.laboreos.dto.LoteResponse;
import com.dsi.laboreos.dto.TipoLaboreoResponse;
import com.dsi.laboreos.model.Campo;
import com.dsi.laboreos.model.Cultivo;
import com.dsi.laboreos.model.Empleado;
import com.dsi.laboreos.model.Lote;
import com.dsi.laboreos.model.OrdenDeLaboreo;
import com.dsi.laboreos.repository.ICampoRepository;
import com.dsi.laboreos.repository.ICultivoRepository;
import com.dsi.laboreos.repository.IEmpleadoRepository;
import com.dsi.laboreos.repository.ILoteRepository;

@Service
public class GestorLaboreos {

    private final ICampoRepository campoRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final ICultivoRepository cultivoRepository;
    private final ILoteRepository loteRepository;

    private List<Campo> campos;
    private List<Lote> lotes;
    private List<Empleado> empleados;
    private List<Cultivo> cultivos;

    private Campo campoSeleccionado;
    private List<Lote> lotesSeleccionados;

    private final Map<Integer, Cultivo> cultivoPorNumeroLote;

    private final Map<Lote, List<OrdenDeLaboreo>> ordenesLaboreoPorLote;
    private Map<String, LocalDateTime[]> fechasPorLaboreo;
    private Map<String, Empleado> empleadosPorLaboreo;

    public GestorLaboreos(
            ICampoRepository campoRepository,
            IEmpleadoRepository empleadoRepository,
            ICultivoRepository cultivoRepository,
            ILoteRepository loteRepository) {
        this.campoRepository = campoRepository;
        this.empleadoRepository = empleadoRepository;
        this.cultivoRepository = cultivoRepository;
        this.loteRepository = loteRepository;
        this.ordenesLaboreoPorLote = new HashMap<>();
        this.cultivoPorNumeroLote = new HashMap<>();
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        cargarDatos();
    }

    private void cargarDatos() {
        this.campos = campoRepository.findAll();
        this.lotes = loteRepository.findAll();
        this.empleados = empleadoRepository.findAll();
        this.cultivos = cultivoRepository.findAll();
    }

    public List<CampoResponse> nuevoLaboreo() {
        return buscarCampos();
    }

    public List<CampoResponse> buscarCampos() {
        return campos.stream()
                .filter(Campo::estaHabilitado)
                .map(campo -> new CampoResponse(campo.getNombre(), campo.getCantidadHectareas()))
                .collect(Collectors.toList());
    }

    public List<LoteResponse> tomarSeleccionCampo(String nombreCampo) {
        Campo campo = campos.stream()
                .filter(c -> c.getNombre().equals(nombreCampo))
                .findFirst()
                .orElse(null);

        if (campo == null) {
            return new ArrayList<>();
        }

        this.campoSeleccionado = campo;
        return buscarLotes(campo);
    }

    private List<LoteResponse> buscarLotes(Campo campo) {
        return campo.buscarLotesProyCultivo().stream()
                .map(lote -> {
                    LocalDate fechaInicio = lote.mostrarFechaInicioProyVigente();
                    return new LoteResponse(lote.getNumero(), fechaInicio);
                })
                .collect(Collectors.toList());
    }

    public List<LoteInfoResponse> tomarSeleccionLotes(List<Integer> numerosLote) {
        if (campoSeleccionado == null) {
            return new ArrayList<>();
        }

        this.lotesSeleccionados = lotes.stream()
                .filter(lote -> numerosLote.contains(lote.getNumero()))
                .collect(Collectors.toList());

        return buscarInfoProyectoVigente(lotesSeleccionados);
    }

    public List<LoteInfoResponse> buscarInfoProyectoVigente(List<Lote> lotes) {
        cultivoPorNumeroLote.clear();

        return lotes.stream()
                .map(lote -> {
                    String cultivoNombre = campoSeleccionado.mostrarCultivo(lote);
                    if (cultivoNombre == null) {
                        return null;
                    }

                    Cultivo cultivo = cultivos.stream()
                            .filter(c -> c.getNombre().equals(cultivoNombre))
                            .findFirst()
                            .orElse(null);

                    if (cultivo != null) {
                        cultivoPorNumeroLote.put(lote.getNumero(), cultivo);
                    }

                    List<LaboreoResponse> laboreosRealizados = buscarLaboreosRealizados(lote);
                    List<TipoLaboreoResponse> tiposLaboreoDisponibles = buscarTiposLaboreoParaCultivo(lote);

                    return new LoteInfoResponse(cultivoNombre, laboreosRealizados, tiposLaboreoDisponibles);
                })
                .filter(info -> info != null)
                .collect(Collectors.toList());
    }

    private List<LaboreoResponse> buscarLaboreosRealizados(Lote lote) {
        List<Map<String, LocalDateTime>> laboreosInfo = campoSeleccionado.buscarLaboreosRealizados(lote);

        return laboreosInfo.stream()
                .map(info -> {
                    String nombreTipoLaboreo = info.keySet().iterator().next();
                    LocalDateTime fecha = info.get(nombreTipoLaboreo);
                    return new LaboreoResponse(nombreTipoLaboreo, fecha);
                })
                .collect(Collectors.toList());
    }

    private List<TipoLaboreoResponse> buscarTiposLaboreoParaCultivo(Lote lote) {
        List<String[]> tiposLaboreoInfo = campoSeleccionado.buscarTipoLaboreosParaCultivo(lote);
        return tiposLaboreoInfo.stream()
                .map(info -> new TipoLaboreoResponse(info[0], info[1]))
                .collect(Collectors.toList());
    }

    public void tomarSeleccLaboreo(List<com.dsi.laboreos.dto.LaboreoPorLoteRequest> laboreosPorLote) {
        ordenesLaboreoPorLote.clear();

        if (campoSeleccionado == null || lotesSeleccionados == null || lotesSeleccionados.isEmpty()) {
            return;
        }

        Map<Integer, Lote> lotesPorNumero = lotesSeleccionados.stream()
                .collect(Collectors.toMap(Lote::getNumero, lote -> lote));

        laboreosPorLote.forEach(laboreoPorLote -> {
            Integer numeroLote = laboreoPorLote.getNumeroLote();
            String[] laboreo = laboreoPorLote.getLaboreo();

            Lote lote = lotesPorNumero.get(numeroLote);
            Cultivo cultivo = cultivoPorNumeroLote.get(numeroLote);

            if (lote == null || cultivo == null) {
                return;
            }

            OrdenDeLaboreo orden = cultivo.conocerOrdenLaboreo().stream()
                    .filter(o -> o.conocerTipoLaboreo().getNombre().equals(laboreo[0]) &&
                            o.conocerMomentoLaboreo().getNombre().equals(laboreo[1]))
                    .findFirst()
                    .orElse(null);

            if (orden != null) {
                ordenesLaboreoPorLote.computeIfAbsent(lote, k -> new ArrayList<>()).add(orden);
            }
        });
    }

    private String generarClaveLaboreo(Integer numeroLote, String[] laboreo) {
        return numeroLote + "|" + laboreo[0] + "|" + laboreo[1];
    }

    public List<EmpleadoResponse> tomarDuracionLaboreo(List<FechaHoraPorLoteRequest.FechaHoraPorLaboreo> fechasPorLaboreo) {
        if (fechasPorLaboreo == null || fechasPorLaboreo.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, LocalDateTime[]> fechasMap = new HashMap<>();

        for (FechaHoraPorLoteRequest.FechaHoraPorLaboreo fechaHora : fechasPorLaboreo) {
            // Crear clave compuesta: numeroLote|tipoLaboreo|momentoLaboreo
            String clave = generarClaveLaboreo(fechaHora.getNumeroLote(), fechaHora.getLaboreo());
            // Convertir a estructura simple: array con [fechaHoraInicio, fechaHoraFin]
            fechasMap.put(clave, new LocalDateTime[]{fechaHora.getFechaHoraInicio(), fechaHora.getFechaHoraFin()});
        }

        this.fechasPorLaboreo = fechasMap;

        return buscarEmpleados();
    }

    public void tomarEmpleado(List<com.dsi.laboreos.dto.SeleccionarEmpleadoPorLaboreoRequest.EmpleadoPorLaboreo> empleadosPorLaboreo) {
        if (empleadosPorLaboreo == null || empleadosPorLaboreo.isEmpty()) {
            return;
        }

        Map<String, Empleado> empleadosMap = new HashMap<>();

        for (com.dsi.laboreos.dto.SeleccionarEmpleadoPorLaboreoRequest.EmpleadoPorLaboreo empleadoPorLaboreo : empleadosPorLaboreo) {
            Empleado empleado = empleados.stream()
                    .filter(e -> e.getNombre().equals(empleadoPorLaboreo.getNombreEmpleado()) &&
                            e.getApellido().equals(empleadoPorLaboreo.getApellidoEmpleado()))
                    .findFirst()
                    .orElse(null);

            if (empleado != null) {
                String clave = generarClaveLaboreo(empleadoPorLaboreo.getNumeroLote(), empleadoPorLaboreo.getLaboreo());
                empleadosMap.put(clave, empleado);
            }
        }

        this.empleadosPorLaboreo = empleadosMap;
    }

    public List<EmpleadoResponse> buscarEmpleados() {
        return empleados.stream()
                .map(emp -> {
                    List<String> datosEmpleado = emp.getEmpleado();
                    return new EmpleadoResponse(datosEmpleado.get(0), datosEmpleado.get(1));
                })
                .collect(Collectors.toList());
    }

    public Boolean tomarConfirmacion() {
        crearLaboreos();
        return validarTipoLaboreo();
    }

    private Boolean validarTipoLaboreo() {
        if (ordenesLaboreoPorLote == null || ordenesLaboreoPorLote.isEmpty()) {
            return false;
        }

        return ordenesLaboreoPorLote.values().stream()
                .flatMap(List::stream)
                .allMatch(orden -> !orden.esSiembra() && !orden.esCosecha());
    }

    private void crearLaboreos() {
        if (campoSeleccionado == null
                || empleadosPorLaboreo == null || empleadosPorLaboreo.isEmpty()
                || ordenesLaboreoPorLote == null || ordenesLaboreoPorLote.isEmpty()
                || fechasPorLaboreo == null || fechasPorLaboreo.isEmpty()) {
            return;
        }

        // Pre-procesar: asociar cada orden con sus fechas y empleado
        // Convertir a arrays de Object: [fechaInicio, fechaFin, empleado, orden]
        Map<Lote, List<Object[]>> laboreosPorLote = new HashMap<>();

        ordenesLaboreoPorLote.forEach((lote, ordenesLaboreo) -> {
            Integer numeroLote = lote.getNumero();
            List<Object[]> laboreos = new ArrayList<>();

            for (OrdenDeLaboreo orden : ordenesLaboreo) {
                String clave = generarClaveLaboreo(numeroLote,
                        new String[]{orden.conocerTipoLaboreo().getNombre(), orden.conocerMomentoLaboreo().getNombre()});
                LocalDateTime[] fechas = fechasPorLaboreo.get(clave);
                Empleado empleado = empleadosPorLaboreo.get(clave);

                if (fechas != null && fechas.length == 2 && empleado != null) {
                    // Array: [fechaInicio, fechaFin, empleado, orden]
                    laboreos.add(new Object[]{fechas[0], fechas[1], empleado, orden});
                }
            }

            if (!laboreos.isEmpty()) {
                laboreosPorLote.put(lote, laboreos);
            }
        });

        campoSeleccionado.crearLaboreosParaProyecto(laboreosPorLote);
        campoRepository.save(campoSeleccionado);
    }

    public void tomarOpcionFinalizar() {
        finCU();
    }

    private void finCU() {
    }
}
