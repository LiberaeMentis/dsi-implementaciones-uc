package com.dsi.laboreos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ordenes_laboreo")
@IdClass(OrdenDeLaboreoId.class)
public class OrdenDeLaboreo {
    
    @Id
    @Column(nullable = false)
    private Integer orden;
    
    @Id
    @Column(name = "tipo_laboreo_nombre", nullable = false, length = 100)
    private String tipoLaboreoNombre;
    
    @Id
    @Column(name = "momento_laboreo_nombre", nullable = false, length = 100)
    private String momentoLaboreoNombre;
    
    // RELACIÓN UNIDIRECCIONAL: OrdenDeLaboreo -> TipoLaboreo (1)
    // TipoLaboreo NO conoce sus OrdenDeLaboreo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_laboreo_nombre", nullable = false, insertable = false, updatable = false)
    private TipoLaboreo tipoLaboreo;
    
    // RELACIÓN UNIDIRECCIONAL: OrdenDeLaboreo -> MomentoLaboreo (1)
    // MomentoLaboreo NO conoce sus OrdenDeLaboreo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "momento_laboreo_nombre", nullable = false, insertable = false, updatable = false)
    private MomentoLaboreo momentoLaboreo;

    public OrdenDeLaboreo() {
    }

    public OrdenDeLaboreo(Integer orden, TipoLaboreo tipoLaboreo, MomentoLaboreo momentoLaboreo) {
        this.orden = orden;
        this.tipoLaboreoNombre = tipoLaboreo.getNombre();
        this.momentoLaboreoNombre = momentoLaboreo.getNombre();
        this.tipoLaboreo = tipoLaboreo;
        this.momentoLaboreo = momentoLaboreo;
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

    public TipoLaboreo getTipoLaboreo() {
        return tipoLaboreo;
    }

    public void setTipoLaboreo(TipoLaboreo tipoLaboreo) {
        this.tipoLaboreo = tipoLaboreo;
        if (tipoLaboreo != null) {
            this.tipoLaboreoNombre = tipoLaboreo.getNombre();
        }
    }

    public MomentoLaboreo getMomentoLaboreo() {
        return momentoLaboreo;
    }

    public void setMomentoLaboreo(MomentoLaboreo momentoLaboreo) {
        this.momentoLaboreo = momentoLaboreo;
        if (momentoLaboreo != null) {
            this.momentoLaboreoNombre = momentoLaboreo.getNombre();
        }
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

