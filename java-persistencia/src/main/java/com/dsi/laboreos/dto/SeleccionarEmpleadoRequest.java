package com.dsi.laboreos.dto;

public class SeleccionarEmpleadoRequest {
    private String nombreEmpleado;
    private String apellidoEmpleado;

    public SeleccionarEmpleadoRequest() {
    }

    public SeleccionarEmpleadoRequest(String nombreEmpleado, String apellidoEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getApellidoEmpleado() {
        return apellidoEmpleado;
    }

    public void setApellidoEmpleado(String apellidoEmpleado) {
        this.apellidoEmpleado = apellidoEmpleado;
    }
}

