package com.dsi.laboreos.dto;

import java.util.List;

public class SeleccionarLotesRequest {
    private List<Integer> numerosLote;

    public List<Integer> getNumerosLote() {
        return numerosLote;
    }

    public void setNumerosLote(List<Integer> numerosLote) {
        this.numerosLote = numerosLote;
    }
}

