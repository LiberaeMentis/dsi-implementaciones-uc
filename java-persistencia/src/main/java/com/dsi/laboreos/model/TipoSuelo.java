package com.dsi.laboreos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipos_suelo")
public class TipoSuelo {
    
    @Id
    @Column(nullable = false, unique = true)
    private Integer numero;
    
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;

    public TipoSuelo() {
    }

    public TipoSuelo(Integer numero, String nombre, String descripcion) {
        this.numero = numero;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
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
}

