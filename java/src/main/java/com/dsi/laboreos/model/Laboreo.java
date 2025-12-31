package com.dsi.laboreos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Laboreo {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Empleado empleado;
    private OrdenDeLaboreo ordenLaboreo;

    public Laboreo(LocalDate fechaInicio, LocalDate fechaFin, LocalTime horaInicio, LocalTime horaFin, Empleado empleado, OrdenDeLaboreo ordenLaboreo) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.empleado = empleado;
        this.ordenLaboreo = ordenLaboreo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public Empleado conocerEmpleado() {
        return empleado;
    }

    public OrdenDeLaboreo conocerOrdenLaboreo() {
        return ordenLaboreo;
    }

    public Map<String, LocalDateTime> mostrarLaboreo() {
        LocalDateTime fechaCompleta = LocalDateTime.of(fechaFin, horaFin);
        Map<String, LocalDateTime> info = new HashMap<>();
        String nombreTipoLaboreo = ordenLaboreo.mostrarLaboreoParaCultivo()[0];
        info.put(nombreTipoLaboreo, fechaCompleta);
        return info;
    }
}

