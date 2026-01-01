package com.dsi.laboreos.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dsi.laboreos.dto.CampoResponse;
import com.dsi.laboreos.dto.EmpleadoResponse;
import com.dsi.laboreos.dto.FechaHoraPorLoteRequest;
import com.dsi.laboreos.dto.LaboreoPorLoteRequest;
import com.dsi.laboreos.dto.LaboreoResponse;
import com.dsi.laboreos.dto.LoteInfoResponse;
import com.dsi.laboreos.dto.LoteResponse;
import com.dsi.laboreos.dto.SeleccionarEmpleadoPorLaboreoRequest;
import com.dsi.laboreos.dto.TipoLaboreoResponse;
import com.dsi.laboreos.model.Campo;
import com.dsi.laboreos.model.Cultivo;
import com.dsi.laboreos.model.Empleado;
import com.dsi.laboreos.model.Estado;
import com.dsi.laboreos.model.Lote;
import com.dsi.laboreos.model.MomentoLaboreo;
import com.dsi.laboreos.model.OrdenDeLaboreo;
import com.dsi.laboreos.model.ProyectoDeCultivo;
import com.dsi.laboreos.model.TipoLaboreo;
import com.dsi.laboreos.model.TipoSuelo;

import jakarta.annotation.PostConstruct;

@Service
public class GestorLaboreos {

    private List<Campo> campos;
    private List<Lote> lotes;
    private List<Empleado> empleados;
    private List<TipoLaboreo> tipoLaboreos;
    private List<Cultivo> cultivos;
    private List<Estado> estados;
    private List<TipoSuelo> tiposSuelo;
    private List<MomentoLaboreo> momentosLaboreo;
    private List<OrdenDeLaboreo> ordenesLaboreo;

    private Campo campoSeleccionado;
    private List<Lote> lotesSeleccionados;

    private Map<Integer, Cultivo> cultivoPorNumeroLote;

    private Map<Lote, List<OrdenDeLaboreo>> ordenesLaboreoPorLote;

    // Mapa donde la clave es "numeroLote|tipoLaboreo|momentoLaboreo" y el valor es [fechaHoraInicio, fechaHoraFin]
    private Map<String, LocalDateTime[]> fechasPorLaboreo;
    // Mapa donde la clave es "numeroLote|tipoLaboreo|momentoLaboreo" y el valor es el Empleado
    private Map<String, Empleado> empleadosPorLaboreo;

    public GestorLaboreos() {
        this.campos = new ArrayList<>();
        this.lotes = new ArrayList<>();
        this.empleados = new ArrayList<>();
        this.tipoLaboreos = new ArrayList<>();
        this.cultivos = new ArrayList<>();
        this.estados = new ArrayList<>();
        this.tiposSuelo = new ArrayList<>();
        this.momentosLaboreo = new ArrayList<>();
        this.ordenesLaboreo = new ArrayList<>();
        this.ordenesLaboreoPorLote = new HashMap<>();
        this.cultivoPorNumeroLote = new HashMap<>();
    }

    @PostConstruct
    public void poblarDatos() {
        crearEstados();
        crearTiposSuelo();
        crearMomentosLaboreo();
        crearTipoLaboreos();
        crearCultivos();
        crearOrdenesLaboreo();
        crearEmpleados();
        crearLotes();
        crearCampos();
    }

    private void crearEstados() {
        estados.add(new Estado("En Preparación", "Proyecto en preparación", false));
        estados.add(new Estado("Vigente", "Proyecto vigente", false));
        estados.add(new Estado("Finalizado", "Proyecto finalizado", true));
        estados.add(new Estado("Cancelado", "Proyecto cancelado", true));
    }

    private void crearTiposSuelo() {
        tiposSuelo.add(new TipoSuelo("Arcilloso", "Suelo con alto contenido de arcilla", 1));
        tiposSuelo.add(new TipoSuelo("Arenoso", "Suelo con alto contenido de arena", 2));
        tiposSuelo.add(new TipoSuelo("Limoso", "Suelo con alto contenido de limo", 3));
        tiposSuelo.add(new TipoSuelo("Franco", "Suelo equilibrado con arcilla, arena y limo", 4));
        tiposSuelo.add(new TipoSuelo("Humífero", "Suelo rico en materia orgánica", 5));
    }

    private void crearMomentosLaboreo() {
        momentosLaboreo.add(new MomentoLaboreo("Pre-siembra", "Laboreo antes de la siembra"));
        momentosLaboreo.add(new MomentoLaboreo("Siembra", "Momento de siembra"));
        momentosLaboreo.add(new MomentoLaboreo("Post-siembra", "Laboreo después de la siembra"));
        momentosLaboreo.add(new MomentoLaboreo("Crecimiento", "Laboreo durante el crecimiento"));
        momentosLaboreo.add(new MomentoLaboreo("Cosecha", "Momento de cosecha"));
    }

    private void crearTipoLaboreos() {
        tipoLaboreos.add(new TipoLaboreo("Arado", "Roturar la tierra"));
        tipoLaboreos.add(new TipoLaboreo("Rastrillado", "Nivelar y desmenuzar la tierra"));
        tipoLaboreos.add(new TipoLaboreo("Siembra", "Plantación del cultivo"));
        tipoLaboreos.add(new TipoLaboreo("Escardillado", "Remoción de malezas entre surcos"));
        tipoLaboreos.add(new TipoLaboreo("Cosecha", "Recolección del cultivo"));
        tipoLaboreos.add(new TipoLaboreo("Fumigación", "Aplicación de agroquímicos"));
        tipoLaboreos.add(new TipoLaboreo("Riego", "Aplicación de agua al cultivo"));
        tipoLaboreos.add(new TipoLaboreo("Rolado", "Aplastamiento de rastrojos"));
    }

    private void crearCultivos() {
        TipoSuelo sueloTipo1 = tiposSuelo.get(0); // Arcilloso
        TipoSuelo sueloTipo2 = tiposSuelo.get(1); // Arenoso

        List<OrdenDeLaboreo> ordenesSoja = new ArrayList<>();
        Cultivo soja = new Cultivo("Soja", sueloTipo1, ordenesSoja);
        cultivos.add(soja);

        List<OrdenDeLaboreo> ordenesMani = new ArrayList<>();
        Cultivo mani = new Cultivo("Maní", sueloTipo1, ordenesMani);
        cultivos.add(mani);

        List<OrdenDeLaboreo> ordenesGirasol = new ArrayList<>();
        Cultivo girasol = new Cultivo("Girasol", sueloTipo1, ordenesGirasol);
        cultivos.add(girasol);

        List<OrdenDeLaboreo> ordenesMaiz = new ArrayList<>();
        Cultivo maiz = new Cultivo("Maíz", sueloTipo2, ordenesMaiz);
        cultivos.add(maiz);
    }

    private void crearOrdenesLaboreo() {
        TipoLaboreo arado = tipoLaboreos.get(0);
        TipoLaboreo rastrillado = tipoLaboreos.get(1);
        TipoLaboreo siembra = tipoLaboreos.get(2);
        TipoLaboreo escardillado = tipoLaboreos.get(3);
        TipoLaboreo cosecha = tipoLaboreos.get(4);
        TipoLaboreo fumigacion = tipoLaboreos.get(5);
        TipoLaboreo riego = tipoLaboreos.get(6);

        MomentoLaboreo preSiembra = momentosLaboreo.get(0);
        MomentoLaboreo momentoSiembra = momentosLaboreo.get(1);
        MomentoLaboreo postSiembra = momentosLaboreo.get(2);
        MomentoLaboreo crecimiento = momentosLaboreo.get(3);
        MomentoLaboreo momentoCosecha = momentosLaboreo.get(4);

        OrdenDeLaboreo orden1 = new OrdenDeLaboreo(1, arado, preSiembra);
        ordenesLaboreo.add(orden1);

        OrdenDeLaboreo orden2 = new OrdenDeLaboreo(2, rastrillado, preSiembra);
        ordenesLaboreo.add(orden2);

        OrdenDeLaboreo orden3 = new OrdenDeLaboreo(3, siembra, momentoSiembra);
        ordenesLaboreo.add(orden3);

        OrdenDeLaboreo orden4 = new OrdenDeLaboreo(4, escardillado, postSiembra);
        ordenesLaboreo.add(orden4);

        OrdenDeLaboreo orden5 = new OrdenDeLaboreo(5, cosecha, momentoCosecha);
        ordenesLaboreo.add(orden5);

        OrdenDeLaboreo orden6 = new OrdenDeLaboreo(6, fumigacion, postSiembra);
        ordenesLaboreo.add(orden6);

        OrdenDeLaboreo orden7 = new OrdenDeLaboreo(7, riego, crecimiento);
        ordenesLaboreo.add(orden7);

        Cultivo sojaCultivo = cultivos.get(0);
        sojaCultivo.conocerOrdenLaboreo().addAll(Arrays.asList(orden1, orden2, orden3, orden4, orden5));

        Cultivo maniCultivo = cultivos.get(1);
        maniCultivo.conocerOrdenLaboreo().addAll(Arrays.asList(orden1, orden2, orden3, orden6, orden5));

        Cultivo girasolCultivo = cultivos.get(2);
        girasolCultivo.conocerOrdenLaboreo().addAll(Arrays.asList(orden1, orden2, orden3, orden7, orden5));

        Cultivo maizCultivo = cultivos.get(3);
        maizCultivo.conocerOrdenLaboreo().addAll(Arrays.asList(
                orden1, // Arado - Pre-siembra
                orden2, // Rastrillado - Pre-siembra
                orden3, // Siembra - Siembra
                orden6, // Fumigación - Post-siembra
                orden7, // Riego - Crecimiento
                orden5  // Cosecha - Cosecha
        ));
    }

    private void crearEmpleados() {
        empleados.add(new Empleado("Juan", "Pérez"));
        empleados.add(new Empleado("María", "González"));
        empleados.add(new Empleado("Carlos", "Rodríguez"));
    }

    private void crearLotes() {
        TipoSuelo sueloTipo1 = tiposSuelo.get(0);
        TipoSuelo sueloTipo2 = tiposSuelo.get(1);

        Estado estadoVigente = estados.get(1);

        Cultivo soja = cultivos.get(0);
        Cultivo mani = cultivos.get(1);
        Cultivo girasol = cultivos.get(2);
        Cultivo maiz = cultivos.get(3);

        Lote lote1 = new Lote(1, 10.5, sueloTipo1);
        ProyectoDeCultivo proyecto1 = new ProyectoDeCultivo(soja, estadoVigente, LocalDate.now().minusMonths(2), null, "");
        lote1.agregarProyectoCultivo(proyecto1);
        lotes.add(lote1);

        Lote lote2 = new Lote(2, 15.0, sueloTipo2);
        ProyectoDeCultivo proyecto2 = new ProyectoDeCultivo(maiz, estadoVigente, LocalDate.now().minusMonths(1), null, "");
        lote2.agregarProyectoCultivo(proyecto2);
        lotes.add(lote2);

        Lote lote3 = new Lote(3, 8.0, sueloTipo1);
        ProyectoDeCultivo proyecto3 = new ProyectoDeCultivo(mani, estadoVigente, LocalDate.now().minusDays(15), null, "");
        lote3.agregarProyectoCultivo(proyecto3);
        lotes.add(lote3);

        Lote lote4 = new Lote(4, 12.0, sueloTipo1);
        ProyectoDeCultivo proyecto4 = new ProyectoDeCultivo(girasol, estadoVigente, LocalDate.now().minusMonths(3), null, "");
        lote4.agregarProyectoCultivo(proyecto4);
        lotes.add(lote4);
    }

    private void crearCampos() {
        List<Lote> lotesCampo1 = Arrays.asList(lotes.get(0), lotes.get(1));
        campos.add(new Campo("Campo Norte", 25.5, true, lotesCampo1));

        List<Lote> lotesCampo2 = Arrays.asList(lotes.get(2), lotes.get(3));
        campos.add(new Campo("Campo Sur", 20.0, true, lotesCampo2));

        campos.add(new Campo("Campo Este", 15.0, false, new ArrayList<>()));
    }

    public List<CampoResponse> nuevoLaboreo() {
        return buscarCampos();
    }

    /**
     * Busca y devuelve una lista de campos habilitados.
     * 
     * Este método filtra los campos que están habilitados, y por cada campo crea un
     * objeto {@link CampoResponse} con su nombre y cantidad de hectáreas.
     * Devolvemos un DTO para evitar exponer la entidad Campo.
     *
     * @return una lista de {@link CampoResponse} de campos habilitados.
     */
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

    /**
     * Busca los lotes del campo que tienen un proyecto de cultivo vigente y
     * devuelve una lista de LoteResponse.
     *
     * @param campo el campo del cual se buscan los lotes con proyecto de cultivo
     *              vigente
     * @return una lista de {@link LoteResponse} con el número de lote y la fecha de
     *         inicio del proyecto vigente
     */
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

    public void tomarSeleccLaboreo(List<LaboreoPorLoteRequest> laboreosPorLote) {
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
            String clave = generarClaveLaboreo(fechaHora.getNumeroLote(), fechaHora.getLaboreo());
            fechasMap.put(clave, new LocalDateTime[]{fechaHora.getFechaHoraInicio(), fechaHora.getFechaHoraFin()});
        }

        this.fechasPorLaboreo = fechasMap;

        return buscarEmpleados();
    }

    public void tomarEmpleado(List<SeleccionarEmpleadoPorLaboreoRequest.EmpleadoPorLaboreo> empleadosPorLaboreo) {
        if (empleadosPorLaboreo == null || empleadosPorLaboreo.isEmpty()) {
            return;
        }

        Map<String, Empleado> empleadosMap = new HashMap<>();

        for (SeleccionarEmpleadoPorLaboreoRequest.EmpleadoPorLaboreo empleadoPorLaboreo : empleadosPorLaboreo) {
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

    private Boolean validarTipoLaboreo() {
        if (ordenesLaboreoPorLote == null || ordenesLaboreoPorLote.isEmpty()) {
            return false;
        }

        return ordenesLaboreoPorLote.values().stream()
                .flatMap(List::stream)
                .allMatch(orden -> !orden.esSiembra() && !orden.esCosecha());
    }

    public Boolean tomarConfirmacion() {
        crearLaboreos();
        return validarTipoLaboreo();
    }

    /**
     * Pre-procesa las órdenes de laboreo por lote, asociando cada orden con sus fechas y empleado,
     * luego delega al Campo la creación de los laboreos.
     * 
     * El proceso es:
     * 1. Para cada lote, obtiene sus órdenes de laboreo seleccionadas
     * 2. Para cada orden, busca sus fechas y empleado asociados
     * 3. Crea una entrada individual por cada orden con sus fechas y empleado
     * 4. Pasa al Campo la información para que cree los laboreos
     */
    private void crearLaboreos() {
        // Mapa final: Lote -> Lista de arrays con información de laboreos a crear
        // Cada array contiene: [fechaInicio, fechaFin, empleado, orden]
        Map<Lote, List<Object[]>> laboreosPorLote = new HashMap<>();

        // Procesar cada lote con sus órdenes de laboreo seleccionadas
        ordenesLaboreoPorLote.forEach((lote, ordenesLaboreo) -> {
            Integer numeroLote = lote.getNumero();
            List<Object[]> laboreos = new ArrayList<>();

            // Para cada orden del lote, buscar sus fechas y empleado asociados
            for (OrdenDeLaboreo orden : ordenesLaboreo) {
                // Generar clave compuesta: "numeroLote|tipoLaboreo|momentoLaboreo"
                String clave = generarClaveLaboreo(numeroLote,
                        new String[]{orden.conocerTipoLaboreo().getNombre(), orden.conocerMomentoLaboreo().getNombre()});

                // Buscar las fechas y empleado asociados a esta orden
                LocalDateTime[] fechas = fechasPorLaboreo.get(clave);
                Empleado empleado = empleadosPorLaboreo.get(clave);

                // Si la orden tiene fechas y empleado válidos, crear array con la información
                // Array: [fechaInicio, fechaFin, empleado, orden]
                if (fechas != null && fechas.length == 2 && empleado != null) {
                    laboreos.add(new Object[]{fechas[0], fechas[1], empleado, orden});
                }
            }

            // Solo agregar el lote si tiene laboreos válidos para crear
            if (!laboreos.isEmpty()) {
                laboreosPorLote.put(lote, laboreos);
            }
        });

        // Le mandamos al Campo la información para que cree los laboreos
        campoSeleccionado.crearLaboreosParaProyecto(laboreosPorLote);
    }

    public void tomarOpcionFinalizar() {
        finCU();
    }

    private void finCU() {
    }
}
