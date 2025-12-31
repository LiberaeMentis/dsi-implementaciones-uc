package com.dsi.laboreos.dto;

import java.time.LocalDateTime;

public class LaboreoResponse {
    private String tipoLaboreo;
    private LocalDateTime fecha;

    public LaboreoResponse(String tipoLaboreo, LocalDateTime fecha) {
        this.tipoLaboreo = tipoLaboreo;
        this.fecha = fecha;
    }

    public String getTipoLaboreo() {
        return tipoLaboreo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}

