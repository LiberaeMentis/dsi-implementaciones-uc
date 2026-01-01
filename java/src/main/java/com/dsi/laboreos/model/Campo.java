package com.dsi.laboreos.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Campo {
    private String nombre;
    private Double cantidadHectareas;
    private Boolean habilitado;
    private List<Lote> lotes;

    public Campo(String nombre, Double cantidadHectareas, Boolean habilitado, List<Lote> lotes) {
        this.nombre = nombre;
        this.cantidadHectareas = cantidadHectareas;
        this.habilitado = habilitado;
        this.lotes = lotes;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getCantidadHectareas() {
        return cantidadHectareas;
    }

    public Boolean estaHabilitado() {
        return habilitado;
    }

    public List<Lote> buscarLotes() {
        return lotes;
    }

    public List<Lote> buscarLotesProyCultivo() {
        return lotes.stream()
                .filter(Lote::tieneProyectoCultivoVigente)
                .collect(Collectors.toList());
    }

    public void mostrarLotes() { // SACAR
    }

    public String mostrarCultivo(Lote lote) {
        return lote != null ? lote.mostrarCultivo() : null;
    }

    public Lote buscarLote(Integer numeroLote) {
        return buscarLotes().stream()
                .filter(l -> l.getNumero().equals(numeroLote))
                .findFirst()
                .orElse(null);
    }


    public List<Map<String, LocalDateTime>> buscarLaboreosRealizados(Lote lote) {
        return lote != null ? lote.buscarLaboreosRealizados() : new ArrayList<>();
    }

    public List<String[]> buscarTiposLaboreoParaCultivo(Lote lote) {
        return lote != null ? lote.buscarLaboreosParaCultivo() : new ArrayList<>();
    }

    // fechasPorLaboreo: clave = "numeroLote|tipoLaboreo|momentoLaboreo", valor = [fechaHoraInicio, fechaHoraFin]
    // empleadosPorLaboreo: clave = "numeroLote|tipoLaboreo|momentoLaboreo", valor = Empleado
    public void crearLaboreosParaProyecto(Map<String, LocalDateTime[]> fechasPorLaboreo, Map<String, Empleado> empleadosPorLaboreo, Map<Lote, List<OrdenDeLaboreo>> ordenesLaboreoPorLote) {
        ordenesLaboreoPorLote.forEach((lote, ordenesLaboreo) -> {
            Integer numeroLote = lote.getNumero();
            
            // Agrupar órdenes por sus fechas y empleado (pueden tener fechas y empleados diferentes)
            Map<String, List<OrdenDeLaboreo>> ordenesPorFechaYEmpleado = new HashMap<>();
            Map<String, LocalDateTime[]> fechasPorGrupo = new HashMap<>();
            Map<String, Empleado> empleadosPorGrupo = new HashMap<>();
            
            for (OrdenDeLaboreo orden : ordenesLaboreo) {
                String clave = numeroLote + "|" + orden.conocerTipoLaboreo().getNombre() + "|" + orden.conocerMomentoLaboreo().getNombre();
                LocalDateTime[] fechas = fechasPorLaboreo.get(clave);
                Empleado empleado = empleadosPorLaboreo.get(clave);
                if (fechas != null && fechas.length == 2 && empleado != null) {
                    // Crear clave para agrupar por fechas y empleado (inicio, fin y empleado)
                    String claveFechaYEmpleado = fechas[0].toString() + "|" + fechas[1].toString() + "|" + empleado.getNombre() + "|" + empleado.getApellido();
                    ordenesPorFechaYEmpleado.computeIfAbsent(claveFechaYEmpleado, k -> new ArrayList<>()).add(orden);
                    fechasPorGrupo.putIfAbsent(claveFechaYEmpleado, fechas);
                    empleadosPorGrupo.putIfAbsent(claveFechaYEmpleado, empleado);
                }
            }
            
            // Crear laboreos agrupados por fechas y empleado usando el método existente
            ordenesPorFechaYEmpleado.forEach((claveFechaYEmpleado, ordenesGrupo) -> {
                LocalDateTime[] fechas = fechasPorGrupo.get(claveFechaYEmpleado);
                Empleado empleado = empleadosPorGrupo.get(claveFechaYEmpleado);
                lote.crearLaboreos(fechas[0], fechas[1], empleado, ordenesGrupo);
            });
        });
    }
}

