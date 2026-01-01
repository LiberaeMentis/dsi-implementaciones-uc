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
import com.dsi.laboreos.model.TipoLaboreo;
import com.dsi.laboreos.repository.ICampoRepository;
import com.dsi.laboreos.repository.ICultivoRepository;
import com.dsi.laboreos.repository.IEmpleadoRepository;
import com.dsi.laboreos.repository.ILoteRepository;
import com.dsi.laboreos.repository.ITipoLaboreoRepository;

@Service
public class GestorLaboreos {

    private final ICampoRepository campoRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final ITipoLaboreoRepository tipoLaboreoRepository;
    private final ICultivoRepository cultivoRepository;
    private final ILoteRepository loteRepository;

    private List<Campo> campos;
    private List<Lote> lotes;
    private List<Empleado> empleados;
    private List<TipoLaboreo> tipoLaboreos;
    private List<Cultivo> cultivos;

    private Campo campoSeleccionado;
    private List<Lote> lotesSeleccionados;
    private Cultivo cultivoDeLaboreo;
    private final Map<Integer, List<OrdenDeLaboreo>> ordenesLaboreoPorLote;
    // Mapa donde la clave es "numeroLote|tipoLaboreo|momentoLaboreo" y el valor es [fechaHoraInicio, fechaHoraFin]
    private Map<String, LocalDateTime[]> fechasPorLaboreo;
    // Mapa donde la clave es "numeroLote|tipoLaboreo|momentoLaboreo" y el valor es el Empleado
    private Map<String, Empleado> empleadosPorLaboreo;

    public GestorLaboreos(
            ICampoRepository campoRepository,
            IEmpleadoRepository empleadoRepository,
            ITipoLaboreoRepository tipoLaboreoRepository,
            ICultivoRepository cultivoRepository,
            ILoteRepository loteRepository) {
        this.campoRepository = campoRepository;
        this.empleadoRepository = empleadoRepository;
        this.tipoLaboreoRepository = tipoLaboreoRepository;
        this.cultivoRepository = cultivoRepository;
        this.loteRepository = loteRepository;
        this.ordenesLaboreoPorLote = new HashMap<>();
    }
    
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        cargarDatos();
    }

    private void cargarDatos() {
        this.campos = campoRepository.findAll();
        this.lotes = loteRepository.findAll();
        this.empleados = empleadoRepository.findAll();
        this.tipoLaboreos = tipoLaboreoRepository.findAll();
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

        // Buscar en los lotes del gestor (asumimos que ya son del campo seleccionado)
        this.lotesSeleccionados = lotes.stream()
                .filter(lote -> numerosLote.contains(lote.getNumero()))
                .collect(Collectors.toList());

        return buscarInfoProyectoVigente(lotesSeleccionados);
    }

    public List<LoteInfoResponse> buscarInfoProyectoVigente(List<Lote> lotes) {
        return lotes.stream()
                .map(lote -> {
                    String cultivoNombre = campoSeleccionado.mostrarCultivo(lote);
                    if (cultivoNombre == null) {
                        return null;
                    }

                    // Buscar el cultivo por nombre y guardarlo en el atributo
                    this.cultivoDeLaboreo = cultivos.stream()
                            .filter(c -> c.getNombre().equals(cultivoNombre))
                            .findFirst()
                            .orElse(null);

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
        List<String[]> tiposLaboreoInfo = campoSeleccionado.buscarTiposLaboreoParaCultivo(lote);
        return tiposLaboreoInfo.stream()
                .map(info -> new TipoLaboreoResponse(info[0], info[1]))
                .collect(Collectors.toList());
    }

    public void tomarSeleccLaboreo(List<com.dsi.laboreos.dto.LaboreoPorLoteRequest> laboreosPorLote) {
        ordenesLaboreoPorLote.clear();
        
        if (campoSeleccionado == null || lotesSeleccionados == null || lotesSeleccionados.isEmpty()) {
            return;
        }
        
        // Crear un mapa de lotes por número para acceso O(1) en lugar de O(n) por cada búsqueda
        Map<Integer, Lote> lotesPorNumero = lotesSeleccionados.stream()
                .collect(Collectors.toMap(Lote::getNumero, lote -> lote));
        
        laboreosPorLote.forEach(laboreoPorLote -> {
            Integer numeroLote = laboreoPorLote.getNumeroLote();
            String[] laboreo = laboreoPorLote.getLaboreo();
            
            // Buscar en el mapa de lotes seleccionados (acceso O(1))
            Lote lote = lotesPorNumero.get(numeroLote);
            
            if (lote == null || cultivoDeLaboreo == null) {
                return;
            }
            
            OrdenDeLaboreo orden = cultivoDeLaboreo.conocerOrdenLaboreo().stream()
                    .filter(o -> o.conocerTipoLaboreo().getNombre().equals(laboreo[0]) &&
                            o.conocerMomentoLaboreo().getNombre().equals(laboreo[1]))
                    .findFirst()
                    .orElse(null);
            
            if (orden != null) {
                ordenesLaboreoPorLote.computeIfAbsent(numeroLote, k -> new ArrayList<>()).add(orden);
            }
        });
    }

    public List<TipoLaboreoResponse> buscarTiposLaboreosParaCultivo(Lote lote) {
        if (campoSeleccionado == null || lote == null) {
            return new ArrayList<>();
        }

        List<String[]> tiposLaboreoInfo = campoSeleccionado.buscarTiposLaboreoParaCultivo(lote);
        return tiposLaboreoInfo.stream()
                .map(info -> new TipoLaboreoResponse(info[0], info[1]))
                .collect(Collectors.toList());
    }

    // Método legacy - mantener por compatibilidad si se usa en algún lugar
    public List<TipoLaboreoResponse> buscarTiposLaboreosParaCultivoLegacy(String nombreCultivo) {
        Cultivo cultivo = cultivos.stream()
                .filter(c -> c.getNombre().equals(nombreCultivo))
                .findFirst()
                .orElse(null);

        if (cultivo == null) {
            return new ArrayList<>();
        }

        return cultivo.buscarTiposLaboreo().stream()
                .map(info -> new TipoLaboreoResponse(info[0], info[1]))
                .collect(Collectors.toList());
    }

    public List<EmpleadoResponse> tomarFechaHoraInicioFin(List<FechaHoraPorLoteRequest.FechaHoraPorLaboreo> fechasPorLaboreo) {
        if (fechasPorLaboreo == null || fechasPorLaboreo.isEmpty()) {
            return new ArrayList<>();
        }

        // Validar que todas las combinaciones lote+laboreo seleccionadas tengan fechas
        Map<String, LocalDateTime[]> fechasMap = new HashMap<>();
        
        for (FechaHoraPorLoteRequest.FechaHoraPorLaboreo fechaHora : fechasPorLaboreo) {
            // Validar fechas
            if (!validarFechas(fechaHora.getFechaHoraInicio(), fechaHora.getFechaHoraFin())) {
                return new ArrayList<>();
            }
            
            // Crear clave compuesta: numeroLote|tipoLaboreo|momentoLaboreo
            String clave = generarClaveLaboreo(fechaHora.getNumeroLote(), fechaHora.getLaboreo());
            // Convertir a estructura simple: array con [fechaHoraInicio, fechaHoraFin]
            fechasMap.put(clave, new LocalDateTime[]{fechaHora.getFechaHoraInicio(), fechaHora.getFechaHoraFin()});
        }

        // Verificar que todas las órdenes de laboreo seleccionadas tengan fechas
        for (Map.Entry<Integer, List<OrdenDeLaboreo>> entry : ordenesLaboreoPorLote.entrySet()) {
            Integer numeroLote = entry.getKey();
            for (OrdenDeLaboreo orden : entry.getValue()) {
                String clave = generarClaveLaboreo(numeroLote, 
                    new String[]{orden.conocerTipoLaboreo().getNombre(), orden.conocerMomentoLaboreo().getNombre()});
                if (!fechasMap.containsKey(clave)) {
                    return new ArrayList<>(); // Falta fecha para algún laboreo seleccionado
                }
            }
        }

        tomarDuracionLaboreo(fechasMap);

        return buscarEmpleados();
    }

    private String generarClaveLaboreo(Integer numeroLote, String[] laboreo) {
        return numeroLote + "|" + laboreo[0] + "|" + laboreo[1];
    }

    private void tomarDuracionLaboreo(Map<String, LocalDateTime[]> fechasPorLaboreo) {
        this.fechasPorLaboreo = fechasPorLaboreo;
    }

    public List<EmpleadoResponse> tomarSeleccionEmpleado() {
        return buscarEmpleados();
    }

    public void tomarEmpleado(List<com.dsi.laboreos.dto.SeleccionarEmpleadoPorLaboreoRequest.EmpleadoPorLaboreo> empleadosPorLaboreo) {
        if (empleadosPorLaboreo == null || empleadosPorLaboreo.isEmpty()) {
            return;
        }

        Map<String, Empleado> empleadosMap = new HashMap<>();
        
        for (com.dsi.laboreos.dto.SeleccionarEmpleadoPorLaboreoRequest.EmpleadoPorLaboreo empleadoPorLaboreo : empleadosPorLaboreo) {
            // Buscar el empleado
            Empleado empleado = empleados.stream()
                    .filter(e -> e.getNombre().equals(empleadoPorLaboreo.getNombreEmpleado()) && 
                            e.getApellido().equals(empleadoPorLaboreo.getApellidoEmpleado()))
                    .findFirst()
                    .orElse(null);
            
            if (empleado != null) {
                // Crear clave compuesta: numeroLote|tipoLaboreo|momentoLaboreo
                String clave = generarClaveLaboreo(empleadoPorLaboreo.getNumeroLote(), empleadoPorLaboreo.getLaboreo());
                empleadosMap.put(clave, empleado);
            }
        }

        // Verificar que todas las órdenes de laboreo seleccionadas tengan empleado
        for (Map.Entry<Integer, List<OrdenDeLaboreo>> entry : ordenesLaboreoPorLote.entrySet()) {
            Integer numeroLote = entry.getKey();
            for (OrdenDeLaboreo orden : entry.getValue()) {
                String clave = generarClaveLaboreo(numeroLote, 
                    new String[]{orden.conocerTipoLaboreo().getNombre(), orden.conocerMomentoLaboreo().getNombre()});
                if (!empleadosMap.containsKey(clave)) {
                    return; // Falta empleado para algún laboreo seleccionado
                }
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

    public Boolean validarFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        LocalDateTime ahora = LocalDateTime.now();
        return fechaInicio.isBefore(fechaFin) &&
                fechaInicio.isBefore(ahora) &&
                fechaFin.isBefore(ahora);
    }

    public Boolean validarTipoLaboreo(String nombreTipoLaboreo) {
        TipoLaboreo tipo = tipoLaboreos.stream()
                .filter(t -> t.getNombre().equals(nombreTipoLaboreo))
                .findFirst()
                .orElse(null);

        if (tipo == null) {
            return false;
        }
        return !tipo.getNombre().equals("Siembra") && !tipo.getNombre().equals("Cosecha");
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
        if (campoSeleccionado == null || empleadosPorLaboreo == null || empleadosPorLaboreo.isEmpty() || ordenesLaboreoPorLote == null || ordenesLaboreoPorLote.isEmpty() || fechasPorLaboreo == null || fechasPorLaboreo.isEmpty() || lotesSeleccionados == null) {
            return;
        }
        campoSeleccionado.crearLaboreosParaProyecto(fechasPorLaboreo, empleadosPorLaboreo, ordenesLaboreoPorLote, lotesSeleccionados);
        campoRepository.save(campoSeleccionado);
    }

    public void tomarOpcionFinalizar() {
        finCU();
    }

    private void finCU() {
    }
}

