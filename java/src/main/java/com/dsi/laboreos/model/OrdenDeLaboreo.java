package com.dsi.laboreos.model;

public class OrdenDeLaboreo {
    private Integer orden;
    private TipoLaboreo tipoLaboreo;
    private MomentoLaboreo momentoLaboreo;

    public OrdenDeLaboreo(Integer orden, TipoLaboreo tipoLaboreo, MomentoLaboreo momentoLaboreo) {
        this.orden = orden;
        this.tipoLaboreo = tipoLaboreo;
        this.momentoLaboreo = momentoLaboreo;
    }

    public Integer getOrden() {
        return orden;
    }

    public TipoLaboreo conocerTipoLaboreo() {
        return tipoLaboreo;
    }

    public MomentoLaboreo conocerMomentoLaboreo() {
        return momentoLaboreo;
    }

    public String[] mostrarLaboreoParaCultivo() {
        String nombreTipoLaboreo = tipoLaboreo.mostrarTipoLaboreo();
        String nombreMomentoLaboreo = momentoLaboreo.mostrarMomentoLaboreo();
        return new String[]{nombreTipoLaboreo, nombreMomentoLaboreo};
    }

    public Boolean esSiembra() {
        return tipoLaboreo.esSiembra();
    }

    public Boolean esCosecha() {
        return tipoLaboreo.esCosecha();
    }
}

