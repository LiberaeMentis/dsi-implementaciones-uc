package com.dsi.laboreos.dto;

public class CampoResponse {
    private String nombre;
    private Double cantidadHectareas;

    public CampoResponse(String nombre, Double cantidadHectareas) {
        this.nombre = nombre;
        this.cantidadHectareas = cantidadHectareas;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getCantidadHectareas() {
        return cantidadHectareas;
    }
}

