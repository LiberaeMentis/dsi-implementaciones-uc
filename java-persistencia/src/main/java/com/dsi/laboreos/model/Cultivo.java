package com.dsi.laboreos.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cultivos")
public class Cultivo {
    
    @Id
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
    
    // RELACIÓN UNIDIRECCIONAL: Cultivo -> TipoSuelo (1)
    // TipoSuelo NO conoce sus Cultivos
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_suelo_numero", nullable = false)
    private TipoSuelo tipoSuelo;
    
    // RELACIÓN UNIDIRECCIONAL: Cultivo -> OrdenDeLaboreo (0..*)
    // OrdenDeLaboreo NO conoce sus Cultivos
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "cultivo_orden_laboreo",
        joinColumns = @JoinColumn(name = "cultivo_nombre"),
        inverseJoinColumns = {
            @JoinColumn(name = "orden_orden", referencedColumnName = "orden"),
            @JoinColumn(name = "orden_tipo_laboreo_nombre", referencedColumnName = "tipo_laboreo_nombre"),
            @JoinColumn(name = "orden_momento_laboreo_nombre", referencedColumnName = "momento_laboreo_nombre")
        }
    )
    private List<OrdenDeLaboreo> ordenLaboreo = new ArrayList<>();

    public Cultivo() {
    }

    public Cultivo(String nombre, TipoSuelo tipoSuelo, List<OrdenDeLaboreo> ordenLaboreo) {
        this.nombre = nombre;
        this.tipoSuelo = tipoSuelo;
        this.ordenLaboreo = ordenLaboreo != null ? ordenLaboreo : new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoSuelo getTipoSuelo() {
        return tipoSuelo;
    }

    public void setTipoSuelo(TipoSuelo tipoSuelo) {
        this.tipoSuelo = tipoSuelo;
    }

    public List<OrdenDeLaboreo> getOrdenLaboreo() {
        return ordenLaboreo;
    }

    public void setOrdenLaboreo(List<OrdenDeLaboreo> ordenLaboreo) {
        this.ordenLaboreo = ordenLaboreo;
    }

    public TipoSuelo conocerTipoSuelo() {
        return tipoSuelo;
    }

    public List<OrdenDeLaboreo> conocerOrdenLaboreo() {
        return ordenLaboreo;
    }

    public List<String[]> buscarTiposLaboreo() {
        return ordenLaboreo.stream()
                .map(OrdenDeLaboreo::mostrarLaboreoParaCultivo)
                .collect(Collectors.toList());
    }
}

