package com.dsi.laboreos.dto;

public class LaboreoPorLoteRequest {
    private Integer numeroLote;
    private String[] laboreo;

    public LaboreoPorLoteRequest() {
    }

    public LaboreoPorLoteRequest(Integer numeroLote, String[] laboreo) {
        this.numeroLote = numeroLote;
        this.laboreo = laboreo;
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
}

