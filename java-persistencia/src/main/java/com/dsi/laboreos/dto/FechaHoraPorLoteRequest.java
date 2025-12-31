package com.dsi.laboreos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class FechaHoraPorLoteRequest {
    // Lista de fechas asociadas a cada combinación de lote + laboreo
    private List<FechaHoraPorLaboreo> fechasPorLaboreo;

    public FechaHoraPorLoteRequest() {
    }

    public FechaHoraPorLoteRequest(List<FechaHoraPorLaboreo> fechasPorLaboreo) {
        this.fechasPorLaboreo = fechasPorLaboreo;
    }

    public List<FechaHoraPorLaboreo> getFechasPorLaboreo() {
        return fechasPorLaboreo;
    }

    public void setFechasPorLaboreo(List<FechaHoraPorLaboreo> fechasPorLaboreo) {
        this.fechasPorLaboreo = fechasPorLaboreo;
    }

    public static class FechaHoraPorLaboreo {
        private Integer numeroLote;
        private String[] laboreo; // [tipoLaboreo, momentoLaboreo]
        private LocalDateTime fechaHoraInicio;
        private LocalDateTime fechaHoraFin;

        public FechaHoraPorLaboreo() {
        }

        public FechaHoraPorLaboreo(Integer numeroLote, String[] laboreo, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {
            this.numeroLote = numeroLote;
            this.laboreo = laboreo;
            this.fechaHoraInicio = fechaHoraInicio;
            this.fechaHoraFin = fechaHoraFin;
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

        public LocalDateTime getFechaHoraInicio() {
            return fechaHoraInicio;
        }

        public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
            this.fechaHoraInicio = fechaHoraInicio;
        }

        public LocalDateTime getFechaHoraFin() {
            return fechaHoraFin;
        }

        public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
            this.fechaHoraFin = fechaHoraFin;
        }
    }
}
