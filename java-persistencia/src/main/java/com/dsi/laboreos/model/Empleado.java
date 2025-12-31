package com.dsi.laboreos.model;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "empleados")
@IdClass(EmpleadoId.class)
public class Empleado {
    
    @Id
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Id
    @Column(nullable = false, length = 100)
    private String apellido;

    public Empleado() {
    }

    public Empleado(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public List<String> getEmpleado() {
        return Arrays.asList(getNombre(), getApellido());
    }
}

