package com.dsi.laboreos.dto;

import java.util.List;

public class SeleccionarLaboreosRequest {
    private List<LaboreoPorLoteRequest> laboreosPorLote;

    public SeleccionarLaboreosRequest() {
    }

    public SeleccionarLaboreosRequest(List<LaboreoPorLoteRequest> laboreosPorLote) {
        this.laboreosPorLote = laboreosPorLote;
    }

    public List<LaboreoPorLoteRequest> getLaboreosPorLote() {
        return laboreosPorLote;
    }

    public void setLaboreosPorLote(List<LaboreoPorLoteRequest> laboreosPorLote) {
        this.laboreosPorLote = laboreosPorLote;
    }
}

