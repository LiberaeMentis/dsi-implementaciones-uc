package com.dsi.laboreos.dto;

import java.util.List;

public class SeleccionarEmpleadoPorLaboreoRequest {
    // Lista de empleados asociados a cada combinación de lote + laboreo
    private List<EmpleadoPorLaboreo> empleadosPorLaboreo;

    public SeleccionarEmpleadoPorLaboreoRequest() {
    }

    public SeleccionarEmpleadoPorLaboreoRequest(List<EmpleadoPorLaboreo> empleadosPorLaboreo) {
        this.empleadosPorLaboreo = empleadosPorLaboreo;
    }

    public List<EmpleadoPorLaboreo> getEmpleadosPorLaboreo() {
        return empleadosPorLaboreo;
    }

    public void setEmpleadosPorLaboreo(List<EmpleadoPorLaboreo> empleadosPorLaboreo) {
        this.empleadosPorLaboreo = empleadosPorLaboreo;
    }

    public static class EmpleadoPorLaboreo {
        private Integer numeroLote;
        private String[] laboreo; // [tipoLaboreo, momentoLaboreo]
        private String nombreEmpleado;
        private String apellidoEmpleado;

        public EmpleadoPorLaboreo() {
        }

        public EmpleadoPorLaboreo(Integer numeroLote, String[] laboreo, String nombreEmpleado, String apellidoEmpleado) {
            this.numeroLote = numeroLote;
            this.laboreo = laboreo;
            this.nombreEmpleado = nombreEmpleado;
            this.apellidoEmpleado = apellidoEmpleado;
        }

        public Integer getNumeroLote() {
            return numeroLote;
        }

        public void setNumeroLote(Integer numeroLote) {
            this.numeroLote = numeroLote;
        }

        public String[] getLaboreo() {
            return laboreo;
        }

        public void setLaboreo(String[] laboreo) {
            this.laboreo = laboreo;
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
}
