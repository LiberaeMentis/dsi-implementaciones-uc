package com.dsi.laboreos.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class LaboreoId implements Serializable {
    
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String empleadoNombre;
    private String empleadoApellido;
    private Integer ordenOrden;
    private String ordenTipoLaboreoNombre;
    private String ordenMomentoLaboreoNombre;

    public LaboreoId() {
    }

    public LaboreoId(LocalDate fechaInicio, LocalDate fechaFin, LocalTime horaInicio, LocalTime horaFin,
                     String empleadoNombre, String empleadoApellido,
                     Integer ordenOrden, String ordenTipoLaboreoNombre, String ordenMomentoLaboreoNombre) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.empleadoNombre = empleadoNombre;
        this.empleadoApellido = empleadoApellido;
        this.ordenOrden = ordenOrden;
        this.ordenTipoLaboreoNombre = ordenTipoLaboreoNombre;
        this.ordenMomentoLaboreoNombre = ordenMomentoLaboreoNombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getEmpleadoNombre() {
        return empleadoNombre;
    }

    public void setEmpleadoNombre(String empleadoNombre) {
        this.empleadoNombre = empleadoNombre;
    }

    public String getEmpleadoApellido() {
        return empleadoApellido;
    }

    public void setEmpleadoApellido(String empleadoApellido) {
        this.empleadoApellido = empleadoApellido;
    }

    public Integer getOrdenOrden() {
        return ordenOrden;
    }

    public void setOrdenOrden(Integer ordenOrden) {
        this.ordenOrden = ordenOrden;
    }

    public String getOrdenTipoLaboreoNombre() {
        return ordenTipoLaboreoNombre;
    }

    public void setOrdenTipoLaboreoNombre(String ordenTipoLaboreoNombre) {
        this.ordenTipoLaboreoNombre = ordenTipoLaboreoNombre;
    }

    public String getOrdenMomentoLaboreoNombre() {
        return ordenMomentoLaboreoNombre;
    }

    public void setOrdenMomentoLaboreoNombre(String ordenMomentoLaboreoNombre) {
        this.ordenMomentoLaboreoNombre = ordenMomentoLaboreoNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaboreoId laboreoId = (LaboreoId) o;
        return Objects.equals(fechaInicio, laboreoId.fechaInicio) &&
               Objects.equals(fechaFin, laboreoId.fechaFin) &&
               Objects.equals(horaInicio, laboreoId.horaInicio) &&
               Objects.equals(horaFin, laboreoId.horaFin) &&
               Objects.equals(empleadoNombre, laboreoId.empleadoNombre) &&
               Objects.equals(empleadoApellido, laboreoId.empleadoApellido) &&
               Objects.equals(ordenOrden, laboreoId.ordenOrden) &&
               Objects.equals(ordenTipoLaboreoNombre, laboreoId.ordenTipoLaboreoNombre) &&
               Objects.equals(ordenMomentoLaboreoNombre, laboreoId.ordenMomentoLaboreoNombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fechaInicio, fechaFin, horaInicio, horaFin,
                           empleadoNombre, empleadoApellido,
                           ordenOrden, ordenTipoLaboreoNombre, ordenMomentoLaboreoNombre);
    }
}

