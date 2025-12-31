package com.dsi.laboreos.model;

public class TipoSuelo {
    private String nombre;
    private String descripcion;
    private Integer numero;

    public TipoSuelo(String nombre, String descripcion, Integer numero) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getNumero() {
        return numero;
    }
}

