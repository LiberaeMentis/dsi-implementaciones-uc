package com.dsi.laboreos.dto;

import java.time.LocalDateTime;

public class RegistrarLaboreoRequest {
    private Integer numeroLote;
    private String nombreTipoLaboreo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String nombreEmpleado;
    private String apellidoEmpleado;

    public Integer getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(Integer numeroLote) {
        this.numeroLote = numeroLote;
    }

    public String getNombreTipoLaboreo() {
        return nombreTipoLaboreo;
    }

    public void setNombreTipoLaboreo(String nombreTipoLaboreo) {
        this.nombreTipoLaboreo = nombreTipoLaboreo;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getApellidoEmpleado() {
        return apellidoEmpleado;
    }

    public void setApellidoEmpleado(String apellidoEmpleado) {
        this.apellidoEmpleado = apellidoEmpleado;
    }
}

