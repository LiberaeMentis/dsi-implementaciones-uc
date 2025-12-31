package com.dsi.laboreos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lote {
    private Integer numero;
    private Double cantidadHectareas;
    private TipoSuelo tipoSuelo;
    private List<ProyectoDeCultivo> proyectosCultivo;

    public Lote(Integer numero, Double cantidadHectareas, TipoSuelo tipoSuelo) {
        this.numero = numero;
        this.cantidadHectareas = cantidadHectareas;
        this.tipoSuelo = tipoSuelo;
        this.proyectosCultivo = new ArrayList<>();
    }

    public Integer getNumero() {
        return numero;
    }

    public Double getCantidadHectareas() {
        return cantidadHectareas;
    }

    public TipoSuelo conocerTipoSuelo() {
        return tipoSuelo;
    }

    public Boolean tieneProyectoCultivoVigente() {
        return proyectosCultivo.stream()
                .anyMatch(ProyectoDeCultivo::estaVigente);
    }

    // En el diagrama, vamos directo al objeto vigente:ProyectoDeCultivo, pero programáticamente hablando, 
    // tenemos que buscar el proyecto de cultivo vigente (dado que no tenemos el puntero directo a ese objeto)
    public ProyectoDeCultivo conocerProyectoDeCultivoVigente() {
        return proyectosCultivo.stream()
                .filter(ProyectoDeCultivo::estaVigente)
                .findFirst()
                .orElse(null);
    }

    public LocalDate mostrarFechaInicioProyVigente() {
        ProyectoDeCultivo proyectoVigente = conocerProyectoDeCultivoVigente();
        return proyectoVigente != null ? proyectoVigente.getFechaInicio() : null;
    }

    public List<Map<String, LocalDateTime>> buscarLaboreosRealizados() {
        ProyectoDeCultivo proyectoVigente = conocerProyectoDeCultivoVigente();
        return proyectoVigente != null ? proyectoVigente.buscarLaboreosRealizados() : new ArrayList<>();
    }

    public List<String[]> buscarLaboreosParaCultivo() {
        ProyectoDeCultivo proyectoVigente = conocerProyectoDeCultivoVigente();
        return proyectoVigente != null ? proyectoVigente.buscarLaboreosParaCultivo() : new ArrayList<>();
    }

    public void agregarProyectoCultivo(ProyectoDeCultivo proyecto) {
        this.proyectosCultivo.add(proyecto);
    }

    public String mostrarCultivo() {
        ProyectoDeCultivo proyectoVigente = conocerProyectoDeCultivoVigente();
        return proyectoVigente != null ? proyectoVigente.mostrarCultivo() : null;
    }

    public void crearLaboreos(LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, Empleado empleado, List<OrdenDeLaboreo> ordenesLaboreo) {
        ProyectoDeCultivo proyectoVigente = conocerProyectoDeCultivoVigente();
        if (proyectoVigente != null) {
            proyectoVigente.crearLaboreos(fechaHoraInicio, fechaHoraFin, empleado, ordenesLaboreo);
        }
    }
}

