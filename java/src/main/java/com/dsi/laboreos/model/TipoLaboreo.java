package com.dsi.laboreos.model;

public class TipoLaboreo {
    private String nombre;
    private String descripcion;

    public TipoLaboreo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String mostrarTipoLaboreo() {
        return nombre;
    }

    public Boolean esSiembra() {
        return nombre.equals("Siembra");
    }

    public Boolean esCosecha() {
        return nombre.equals("Cosecha");
    }
}

