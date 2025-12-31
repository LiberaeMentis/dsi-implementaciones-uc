package com.dsi.laboreos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "estados")
public class Estado {
    
    @Id
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(nullable = false)
    private Boolean estadoFinal;

    public Estado() {
    }

    public Estado(String nombre, String descripcion, Boolean estadoFinal) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estadoFinal = estadoFinal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(Boolean estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public Boolean esFinal() {
        return estadoFinal;
    }
}

