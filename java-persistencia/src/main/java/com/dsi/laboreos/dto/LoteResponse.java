package com.dsi.laboreos.dto;

import java.time.LocalDate;

public class LoteResponse {
    private Integer numero;
    private LocalDate fechaInicioProyecto;

    public LoteResponse(Integer numero, LocalDate fechaInicioProyecto) {
        this.numero = numero;
        this.fechaInicioProyecto = fechaInicioProyecto;
    }

    public Integer getNumero() {
        return numero;
    }

    public LocalDate getFechaInicioProyecto() {
        return fechaInicioProyecto;
    }
}

