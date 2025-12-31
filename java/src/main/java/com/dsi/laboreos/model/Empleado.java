package com.dsi.laboreos.model;

import java.util.Arrays;
import java.util.List;

public class Empleado {
    private String nombre;
    private String apellido;

    public Empleado(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public List<String> getEmpleado() {
        return Arrays.asList(getNombre(), getApellido());
    }
}

