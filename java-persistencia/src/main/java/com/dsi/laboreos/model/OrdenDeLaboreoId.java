package com.dsi.laboreos.model;

import java.io.Serializable;
import java.util.Objects;

public class OrdenDeLaboreoId implements Serializable {
    
    private Integer orden;
    
    private String tipoLaboreoNombre;
    
    private String momentoLaboreoNombre;

    public OrdenDeLaboreoId() {
    }

    public OrdenDeLaboreoId(Integer orden, String tipoLaboreoNombre, String momentoLaboreoNombre) {
        this.orden = orden;
        this.tipoLaboreoNombre = tipoLaboreoNombre;
        this.momentoLaboreoNombre = momentoLaboreoNombre;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getTipoLaboreoNombre() {
        return tipoLaboreoNombre;
    }

    public void setTipoLaboreoNombre(String tipoLaboreoNombre) {
        this.tipoLaboreoNombre = tipoLaboreoNombre;
    }

    public String getMomentoLaboreoNombre() {
        return momentoLaboreoNombre;
    }

    public void setMomentoLaboreoNombre(String momentoLaboreoNombre) {
        this.momentoLaboreoNombre = momentoLaboreoNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdenDeLaboreoId that = (OrdenDeLaboreoId) o;
        return Objects.equals(orden, that.orden) && 
               Objects.equals(tipoLaboreoNombre, that.tipoLaboreoNombre) && 
               Objects.equals(momentoLaboreoNombre, that.momentoLaboreoNombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orden, tipoLaboreoNombre, momentoLaboreoNombre);
    }
}

