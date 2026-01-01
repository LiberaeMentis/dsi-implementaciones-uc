package com.dsi.laboreos.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "lotes")
public class Lote {
    
    @Id
    @Column(nullable = false)
    private Integer numero;
    
    @Column(nullable = false)
    private Double cantidadHectareas;
    
    // RELACIÓN UNIDIRECCIONAL: Lote -> TipoSuelo (1)
    // TipoSuelo NO conoce sus Lotes
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_suelo_numero", nullable = false)
    private TipoSuelo tipoSuelo;
    
    // RELACIÓN UNIDIRECCIONAL: Lote -> ProyectoDeCultivo (0..*)
    // ProyectoDeCultivo NO conoce su Lote desde el dominio (solo el Lote busca proyectos)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "lote_numero")
    private List<ProyectoDeCultivo> proyectosCultivo = new ArrayList<>();

    public Lote() {
    }

    public Lote(Integer numero, Double cantidadHectareas, TipoSuelo tipoSuelo) {
        this.numero = numero;
        this.cantidadHectareas = cantidadHectareas;
        this.tipoSuelo = tipoSuelo;
        this.proyectosCultivo = new ArrayList<>();
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Double getCantidadHectareas() {
        return cantidadHectareas;
    }

    public void setCantidadHectareas(Double cantidadHectareas) {
        this.cantidadHectareas = cantidadHectareas;
    }

    public TipoSuelo getTipoSuelo() {
        return tipoSuelo;
    }

    public void setTipoSuelo(TipoSuelo tipoSuelo) {
        this.tipoSuelo = tipoSuelo;
    }

    public List<ProyectoDeCultivo> getProyectosCultivo() {
        return proyectosCultivo;
    }

    public void setProyectosCultivo(List<ProyectoDeCultivo> proyectosCultivo) {
        this.proyectosCultivo = proyectosCultivo;
    }

    public TipoSuelo conocerTipoSuelo() {
        return tipoSuelo;
    }

    public Boolean tieneProyectoCultivoVigente() {
        return proyectosCultivo.stream()
                .anyMatch(ProyectoDeCultivo::estaVigente);
    }

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

    // Recibe los datos de un laboreo ya casteados y delega al proyecto vigente
    public void crearLaboreos(LocalDateTime fechaInicio, LocalDateTime fechaFin, Empleado empleado, OrdenDeLaboreo orden) {
        ProyectoDeCultivo proyectoVigente = conocerProyectoDeCultivoVigente();
        if (proyectoVigente != null) {
            proyectoVigente.crearLaboreos(fechaInicio, fechaFin, empleado, orden);
        }
    }
}

