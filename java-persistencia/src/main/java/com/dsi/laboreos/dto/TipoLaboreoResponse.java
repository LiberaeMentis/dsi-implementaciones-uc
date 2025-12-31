package com.dsi.laboreos.dto;

public class TipoLaboreoResponse {
    private String nombreTipoLaboreo;
    private String nombreMomentoLaboreo;

    public TipoLaboreoResponse(String nombreTipoLaboreo, String nombreMomentoLaboreo) {
        this.nombreTipoLaboreo = nombreTipoLaboreo;
        this.nombreMomentoLaboreo = nombreMomentoLaboreo;
    }

    public String getNombreTipoLaboreo() {
        return nombreTipoLaboreo;
    }

    public String getNombreMomentoLaboreo() {
        return nombreMomentoLaboreo;
    }
}

