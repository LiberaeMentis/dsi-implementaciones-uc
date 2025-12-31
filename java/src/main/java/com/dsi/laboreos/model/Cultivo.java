package com.dsi.laboreos.model;

import java.util.List;
import java.util.stream.Collectors;

public class Cultivo {
    private String nombre;
    private TipoSuelo tipoSuelo;
    private List<OrdenDeLaboreo> ordenLaboreo;

    public Cultivo(String nombre, TipoSuelo tipoSuelo, List<OrdenDeLaboreo> ordenLaboreo) {
        this.nombre = nombre;
        this.tipoSuelo = tipoSuelo;
        this.ordenLaboreo = ordenLaboreo;
    }

    public String getNombre() {
        return nombre;
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

