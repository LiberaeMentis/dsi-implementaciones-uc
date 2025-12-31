package com.dsi.laboreos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProyectoDeCultivo {
    private Cultivo cultivo;
    private Estado estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String observaciones;
    private List<Laboreo> laboreos;

    public ProyectoDeCultivo(Cultivo cultivo, Estado estado, LocalDate fechaInicio, LocalDate fechaFin, String observaciones) {
        this.cultivo = cultivo;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.observaciones = observaciones;
        this.laboreos = new ArrayList<>();
    }

    public Cultivo getCultivo() {
        return cultivo;
    }

    public Estado conocerEstado() {
        return estado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Boolean estaVigente() {
        return estado != null && !estado.esFinal();
    }

    public List<Map<String, LocalDateTime>> buscarLaboreosRealizados() {
        return laboreos.stream()
                .map(Laboreo::mostrarLaboreo)
                .collect(Collectors.toList());
    }

    public List<OrdenDeLaboreo> buscarLaboreosPosibles() {
        return cultivo.conocerOrdenLaboreo();
    }

    public List<String[]> buscarLaboreosParaCultivo() {
        return cultivo.buscarTiposLaboreo();
    }

    public void agregarLaboreo(Laboreo laboreo) {
        this.laboreos.add(laboreo);
    }

    public String mostrarCultivo() {
        return cultivo.getNombre();
    }

    public void crearLaboreos(LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, Empleado empleado, List<OrdenDeLaboreo> ordenesLaboreo) {
        ordenesLaboreo.forEach(orden -> {
            Laboreo laboreo = new Laboreo(
                    fechaHoraInicio.toLocalDate(),
                    fechaHoraFin.toLocalDate(),
                    fechaHoraInicio.toLocalTime(),
                    fechaHoraFin.toLocalTime(),
                    empleado,
                    orden
            );
            this.laboreos.add(laboreo);
        });
    }
}

