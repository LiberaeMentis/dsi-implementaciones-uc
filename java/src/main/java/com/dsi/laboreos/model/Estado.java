package com.dsi.laboreos.model;

public class Estado {
    private String nombre;
    private String descripcion;
    private Boolean estadoFinal;

    public Estado(String nombre, String descripcion, Boolean estadoFinal) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estadoFinal = estadoFinal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Boolean esFinal() {
        return estadoFinal;
    }
}

