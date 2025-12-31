package com.dsi.laboreos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipos_laboreo")
public class TipoLaboreo {
    
    @Id
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;

    public TipoLaboreo() {
    }

    public TipoLaboreo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
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

