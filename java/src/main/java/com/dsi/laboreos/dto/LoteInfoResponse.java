package com.dsi.laboreos.dto;

import java.util.List;

public class LoteInfoResponse {
    private String cultivoNombre;
    private List<LaboreoResponse> laboreosRealizados;
    private List<TipoLaboreoResponse> tiposLaboreoDisponibles;

    public LoteInfoResponse(String cultivoNombre, List<LaboreoResponse> laboreosRealizados, List<TipoLaboreoResponse> tiposLaboreoDisponibles) {
        this.cultivoNombre = cultivoNombre;
        this.laboreosRealizados = laboreosRealizados;
        this.tiposLaboreoDisponibles = tiposLaboreoDisponibles;
    }

    public String getCultivoNombre() {
        return cultivoNombre;
    }

    public List<LaboreoResponse> getLaboreosRealizados() {
        return laboreosRealizados;
    }

    public List<TipoLaboreoResponse> getTiposLaboreoDisponibles() {
        return tiposLaboreoDisponibles;
    }
}

