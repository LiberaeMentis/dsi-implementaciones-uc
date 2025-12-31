package com.dsi.laboreos.model;

public class MomentoLaboreo {
    private String nombre;
    private String descripcion;

    public MomentoLaboreo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String mostrarMomentoLaboreo() {
        return nombre;
    }
}

